package ru.oepak22.popularmovies;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.oepak22.popularmovies.adapter.MoviesAdapter;
import ru.oepak22.popularmovies.model.Movie;
import ru.oepak22.popularmovies.model.MoviesResponse;
import ru.oepak22.popularmovies.network.ApiFactory;
import ru.oepak22.popularmovies.view.LoadingDialog;
import ru.oepak22.popularmovies.view.LoadingView;

// класс основной активности
public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {

    private Toolbar mToolbar;
    private RecyclerView mMoviesRecycler;
    private View mEmptyView;
    private MoviesAdapter mAdapter;

    @Nullable
    private Disposable mMoviesSubscription;

    // создание активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // компоненты активности
        mToolbar = findViewById(R.id.toolbar);
        mMoviesRecycler = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty);
        setSupportActionBar(mToolbar);

        // область списка (в две колонки)
        int columns = getResources().getInteger(R.integer.columns_count);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), columns);
        mMoviesRecycler.setLayoutManager(layoutManager);
        mAdapter = createAdapter();
        mMoviesRecycler.setAdapter(mAdapter);

        LoadingView loadingView = LoadingDialog.view(getSupportFragmentManager());

        // запрос через RX
        mMoviesSubscription = ApiFactory.getMoviesService()
                .popularMovies()                                                        // получение данных через запрос
                .map(MoviesResponse::getMovies)                                         // данные - список фильмов
                .flatMap(movies -> {                                                    // замена источника данных
                    Realm.getDefaultInstance().executeTransaction(realm -> {            // кэширование
                        realm.delete(Movie.class);                                      // удаление из локальной базы старого списка
                        realm.insert(movies);                                           // добавление в базу нового списка
                    });
                    return Observable.just(movies);                                     // список фильмов - в потоке данных
                })
                .onErrorResumeNext(throwable -> {                                       // замена потока данных в случае ошибки
                    Realm realm = Realm.getDefaultInstance();                           // обращение к закэшированным данным
                    RealmResults<Movie> results = realm.where(Movie.class).findAll();   // извлечение данных из таблицы
                    return Observable.just(realm.copyFromRealm(results));               // поток данных - локальная таблица
                })
                .doOnSubscribe(disposable -> loadingView.showLoadingIndicator())        // при подписке открывается индикатор загрузки
                .doAfterTerminate(loadingView::hideLoadingIndicator)                    // при ошибке или завершении потока закрывается индикатор загрузки
                .subscribeOn(Schedulers.io())                                           // получение данных - в дополнительном потоке
                .observeOn(AndroidSchedulers.mainThread())                              // обработка данных - в основном потоке
                .subscribe(this::showMovies, throwable -> showError());                 // отображение полученных данных или ошибка
    }

    // выбор элемента списка
    @Override
    public void onItemClick(@NonNull View view, @NonNull Movie movie) {
        ImageView imageView = view.findViewById(R.id.image);
        MovieDetailsActivity.navigate(this, imageView, movie);
    }

    // активность на паузе
    @Override
    protected void onPause() {

        // отписка от источника данных
        if (mMoviesSubscription != null) {
            mMoviesSubscription.dispose();
        }
        super.onPause();
    }

    // ошибка
    private void showError() {
        mMoviesRecycler.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    // отображение списка фильмов
    private void showMovies(@NonNull List<Movie> movies) {
        mAdapter.changeDataSet(movies);
        mMoviesRecycler.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    // инициализация адаптера для списка
    @NonNull
    private MoviesAdapter createAdapter() {
        TypedValue typedValue = new TypedValue();
        getResources().getValue(R.dimen.rows_count, typedValue, true);
        float rowsCount = typedValue.getFloat();
        int actionBarHeight = getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)
                ? TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics())
                : 0;
        int imageHeight = (int) ((getResources().getDisplayMetrics().heightPixels - actionBarHeight) / rowsCount);

        int columns = getResources().getInteger(R.integer.columns_count);
        int imageWidth = getResources().getDisplayMetrics().widthPixels / columns;

        return new MoviesAdapter(imageHeight, imageWidth, this);
    }
}