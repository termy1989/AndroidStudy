package ru.oepak22.simpleweatherpatterna;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ru.arturvasilov.sqlite.core.BasicTableObserver;
import ru.arturvasilov.sqlite.core.SQLite;
import ru.arturvasilov.sqlite.core.Where;
import ru.arturvasilov.sqlite.rx.RxSQLite;
import ru.oepak22.simpleweatherpatterna.adapter.CitiesAdapter;
import ru.oepak22.simpleweatherpatterna.model.AllCities;
import ru.oepak22.simpleweatherpatterna.model.City;
import ru.oepak22.simpleweatherpatterna.network.NetworkRequest;
import ru.oepak22.simpleweatherpatterna.network.NetworkService;
import ru.oepak22.simpleweatherpatterna.network.Request;
import ru.oepak22.simpleweatherpatterna.network.RequestStatus;
import ru.oepak22.simpleweatherpatterna.tables.AllCitiesTable;
import ru.oepak22.simpleweatherpatterna.tables.RequestTable;
import ru.oepak22.simpleweatherpatterna.view.LoadingDialog;
import ru.oepak22.simpleweatherpatterna.view.LoadingView;
import ru.oepak22.simpleweatherpatterna.view.SimpleDividerItemDecoration;

public class MainActivity extends AppCompatActivity implements CitiesAdapter.OnItemClick,
        BasicTableObserver {

    private static final String CITY_LIST = "cities";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CitiesAdapter mAdapter;
    private LoadingView mLoadingView;

    private AllCities allCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty);

        setSupportActionBar(mToolbar);

        // инициализация обновлялки
        mSwipeRefreshLayout = findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                loadCityList();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        // открытие индикатора загрузки
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        // загрузка и вывод результата
        if (savedInstanceState == null || !savedInstanceState.containsKey(CITY_LIST)) {
            loadCityList();
        } else {
            allCities = (AllCities) savedInstanceState.getSerializable(CITY_LIST);
            showCityList();
        }
    }

    // нажатие на элемент списка
    @Override
    public void onItemClick(@NonNull City city) {
        startActivity(WeatherActivity.makeIntent(this, city));
    }

    // отслеживание изменений в базе
    @SuppressLint("CheckResult")
    @Override
    public void onTableChanged() {
        Where where = Where.create().equalTo(RequestTable.REQUEST, NetworkRequest.CITY_WEATHER);
        RxSQLite.get().querySingle(RequestTable.TABLE, where)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Request, ObservableSource<AllCities>>() {
                    @Override
                    public ObservableSource<AllCities> apply(@NonNull Request request) throws Exception {

                        if (request.getStatus() == RequestStatus.IN_PROGRESS) {
                            mLoadingView.showLoadingIndicator();
                            return Observable.empty();
                        } else if (request.getStatus() == RequestStatus.ERROR) {
                            return Observable.error(new IOException(request.getError()));
                        }
                        return RxSQLite.get().querySingle(AllCitiesTable.TABLE)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(cities -> {
                    allCities = cities;
                    showCityList();
                    mLoadingView.hideLoadingIndicator();
                }, throwable -> {
                    showError();
                    mLoadingView.hideLoadingIndicator();
                });
    }

    // остановка активности
    @Override
    protected void onStop() {
        super.onStop();
        SQLite.get().unregisterObserver(this);
    }

    // сохранение состояния активности перед поворотом
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (allCities != null) {
            outState.putSerializable(CITY_LIST, allCities);
        }
    }

    // запрос на список городов через сервис
    void loadCityList() {
        SQLite.get().registerObserver(RequestTable.TABLE, this);
        Request request = new Request(NetworkRequest.CITY_WEATHER);
        NetworkService.start(this, request);
    }

    // вывод списка городов
    private void showCityList() {

        mLoadingView.hideLoadingIndicator();

        // проверка на ошибку в загруженной информации
        for (City city : allCities.getList()) {
            if (city == null
                    || city.getMain() == null
                    || city.getWeather() == null
                    || city.getWind() == null) {
                showError();
                return;
            }
        }

        // вывод списка
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, false));
        mAdapter = new CitiesAdapter(allCities.getList(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    // ошибка при загрузке информации
    private void showError() {
        mLoadingView.hideLoadingIndicator();
        Snackbar snackbar = Snackbar.make(mRecyclerView, R.string.error, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> loadCityList());
        snackbar.setDuration(4000);
        snackbar.show();
    }
}