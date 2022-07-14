package ru.oepak22.popularmoviesclean.screen.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.arturvasilov.rxloader.RxUtils;
import ru.oepak22.data.repository.RepositoryProvider;
import ru.oepak22.domain.model.Movie;
import ru.oepak22.domain.usecase.MoviesUseCase;
import ru.oepak22.popularmoviesclean.R;
import ru.oepak22.popularmoviesclean.screen.adapters.MoviesAdapter;
import ru.oepak22.popularmoviesclean.screen.details.MovieDetailsActivity;
import ru.oepak22.popularmoviesclean.screen.general.LoadingDialog;
import ru.oepak22.popularmoviesclean.screen.general.LoadingView;

public class MoviesActivity extends AppCompatActivity implements MoviesView, MoviesAdapter.OnItemClickListener {

    private Toolbar mToolbar;
    private RecyclerView mMoviesRecycler;
    private View mEmptyView;
    private MoviesAdapter mAdapter;
    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

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

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        MoviesUseCase moviesUseCase = new MoviesUseCase(RepositoryProvider.getMoviesRepository(), RxUtils.async());
        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this, getSupportLoaderManager());
        MoviesPresenter presenter = new MoviesPresenter(this, moviesUseCase, lifecycleHandler);
        presenter.init();
    }

    @Override
    public void showMovies(@NonNull List<Movie> movies) {
        mAdapter.changeDataSet(movies);
        mMoviesRecycler.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingIndicator() {
        mLoadingView.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        mLoadingView.hideLoadingIndicator();
    }

    @Override
    public void onItemClick(@NonNull View view, @NonNull Movie movie) {
        ImageView imageView = view.findViewById(R.id.image);
        MovieDetailsActivity.navigate(this, imageView, movie);
    }

    @Override
    public void showError() {
        mMoviesRecycler.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

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