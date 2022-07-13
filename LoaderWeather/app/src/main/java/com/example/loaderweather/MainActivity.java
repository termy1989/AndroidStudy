package com.example.loaderweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.loaderweather.adapter.CitiesAdapter;
import com.example.loaderweather.loader.WeatherLoader;
import com.example.loaderweather.model.City;
import com.example.loaderweather.view.LoadingDialog;
import com.example.loaderweather.view.LoadingView;
import com.example.loaderweather.view.SimpleDividerItemDecoration;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// класс основной активности
public class MainActivity extends AppCompatActivity implements CitiesAdapter.OnItemClick {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private CitiesAdapter mAdapter;

    private LoadingView mLoadingView;

    private final List<City> loadedCities = new ArrayList<>();
    private List<City> cities;

    // создание активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // инициализация компонентов интерфейса
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty);
        setSupportActionBar(mToolbar);

        // инициализация обновлялки
        mSwipeRefreshLayout = findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                //Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                load(cities, true);

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

        // инициализация списка городов
        cities = getInitialCities();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this,
                                                                    false));
        mAdapter = new CitiesAdapter(cities, this);
        mRecyclerView.setAdapter(mAdapter);
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        load(cities, false);
    }

    // выбор города из списка - вызов окна с отображением погоды
    @Override
    public void onItemClick(@NonNull City city) {
        startActivity(WeatherActivity.makeIntent(this, city.getName()));
    }

    // получение списка городов
    @NonNull
    private List<City> getInitialCities() {
        List<City> cities = new ArrayList<>();
        String[] initialCities = getResources().getStringArray(R.array.initial_cities);
        for (String city : initialCities) {
            cities.add(new City(city));
        }
        return cities;
    }

    // загрузка информации о погоде
    private void loadWeather(boolean restart, City city, String cityName, Integer id) {

        // открытие индикатора загрузки
        mLoadingView.showLoadingIndicator();

        // инициализация callback для загрузки информации о погоде
        LoaderManager.LoaderCallbacks<City> callbacks = new WeatherCallbacks(city, cityName);

        // рестарт загрузки
        // замена экземпляра LoaderCallbacks на новый и передача в его onLoadFInished загруженных данных
        if (restart) {
            getSupportLoaderManager().restartLoader(id, Bundle.EMPTY, callbacks);

        // инициализация загрузки
        } else {
            getSupportLoaderManager().initLoader(id, Bundle.EMPTY, callbacks);
        }
    }

    // загрузка температуры для каждого города по списку
    private void load(List<City> cities, boolean restart) {

        for (int i = 0; i < cities.size(); i++) {

            String cityName = cities.get(i).getName();
            loadWeather(restart, cities.get(i), cityName, i+1);
        }
    }

    // вывод информации о температуре в городе
    private void showWeather(@Nullable City city) {

        // проверка на ошибку в загруженной информации
        if (city == null
                || city.getMain() == null
                || city.getWeather() == null
                || city.getWind() == null) {
            showError();
            return;
        }

        // добавление в список городов загруженной информации
        loadedCities.add(city);

        // информация по всем городам готова
        if (loadedCities.size() >= cities.size()) {

            // закрытие индикатора загрузки
            mLoadingView.hideLoadingIndicator();

            // сортировка списка
            sortAllcities(loadedCities);

            // добавление информации в адаптер списка
            mAdapter.changeDataSet(loadedCities);

            // очистка массива
            loadedCities.clear();
        }
    }

    // сортировка массива
    private void sortAllcities(List<City> cities) {

        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    // ошибка при загрузке информации
    private void showError() {
        mLoadingView.hideLoadingIndicator();
        Snackbar snackbar = Snackbar.make(mRecyclerView, R.string.error, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, v -> load(cities, true));
        snackbar.setDuration(4000);
        snackbar.show();
    }



    // класс LoaderCallbacks для погодного лоадера
    private class WeatherCallbacks implements LoaderManager.LoaderCallbacks<City> {

        private City city;

        private String cityName;

        public WeatherCallbacks(City city, String cityName) {
            this.city = city;
            this.cityName = cityName;
        }

        // получение нужного лоадера в зависимости от id и параметров из bundle
        @NonNull
        @Override
        public Loader onCreateLoader(int id, Bundle args) {

            // создание лоадеров по числу городов из списка
            if (id <= cities.size()) {
                return new WeatherLoader(MainActivity.this, cityName);
            }
            return null;
        }

        // завершение загрузки
        @Override
        public void onLoadFinished(@NonNull Loader<City> loader, City city) {
            showWeather(city);
        }

        // прерывание загрузки, очистка данных, связанных с лоадером
        @Override
        public void onLoaderReset(@NonNull Loader<City> loader) {

        }
    }
}