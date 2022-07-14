package ru.oepak22.popularmoviesagera.screen.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import ru.oepak22.popularmoviesagera.R;
import ru.oepak22.popularmoviesagera.model.content.Movie;
import ru.oepak22.popularmoviesagera.model.response.MoviesResponse;
import ru.oepak22.popularmoviesagera.network.ApiFactory;
import ru.oepak22.popularmoviesagera.screen.adapters.MoviesAdapter;
import ru.oepak22.popularmoviesagera.screen.details.MovieDetailsActivity;
import ru.oepak22.popularmoviesagera.utils.AsyncExecutor;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener, Updatable {

    private Toolbar mToolbar;
    private RecyclerView mMoviesRecycler;
    private View mEmptyView;
    private MoviesAdapter mAdapter;

    private Repository<Result<List<Movie>>> mMoviesRepository;

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

        mMoviesRepository = Repositories.repositoryWithInitialValue(Result.<List<Movie>>absent())
                .observe()
                .onUpdatesPerLoop()
                .goTo(AsyncExecutor.EXECUTOR)
                .getFrom(() -> {
                    try {
                        return Objects.requireNonNull(ApiFactory.getMoviesService().popularMovies().execute().body());
                    } catch (IOException e) {
                        return new MoviesResponse();
                    }
                })
                .thenTransform(input -> Result.absentIfNull(input.getMovies()))
                .compile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoviesRepository.addUpdatable(this);
    }

    @Override
    protected void onPause() {
        mMoviesRepository.removeUpdatable(this);
        super.onPause();
    }

    @Override
    public void update() {
        mMoviesRepository.get()
                .ifSucceededSendTo(this::showMovies)
                .ifFailedSendTo(value -> showError());
    }

    @Override
    public void onItemClick(@NonNull View view, @NonNull Movie movie) {
        ImageView imageView = view.findViewById(R.id.image);
        MovieDetailsActivity.navigate(this, imageView, movie);
    }

    private void showError() {
        mMoviesRecycler.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showMovies(@NonNull List<Movie> movies) {
        mAdapter.changeDataSet(movies);
        mMoviesRecycler.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
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