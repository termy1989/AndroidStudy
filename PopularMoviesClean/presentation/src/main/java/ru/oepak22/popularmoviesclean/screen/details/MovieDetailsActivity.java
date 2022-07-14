package ru.oepak22.popularmoviesclean.screen.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.arturvasilov.rxloader.RxUtils;
import ru.oepak22.data.repository.RepositoryProvider;
import ru.oepak22.domain.model.Movie;
import ru.oepak22.domain.model.Review;
import ru.oepak22.domain.model.ReviewsAndVideos;
import ru.oepak22.domain.model.Video;
import ru.oepak22.domain.usecase.MoviesUseCase;
import ru.oepak22.domain.usecase.ReviewsUseCase;
import ru.oepak22.domain.usecase.VideosUseCase;
import ru.oepak22.popularmoviesclean.R;
import ru.oepak22.popularmoviesclean.screen.adapters.ReviewsAdapter;
import ru.oepak22.popularmoviesclean.screen.adapters.VideosAdapter;
import ru.oepak22.popularmoviesclean.screen.general.LoadingDialog;
import ru.oepak22.popularmoviesclean.screen.general.LoadingView;
import ru.oepak22.popularmoviesclean.screen.movies.MoviesPresenter;
import ru.oepak22.popularmoviesclean.screen.movies.MoviesView;
import ru.oepak22.popularmoviesclean.utils.Images;
import ru.oepak22.popularmoviesclean.utils.Videos;

public class MovieDetailsActivity extends AppCompatActivity implements ReviewsVideosView {

    private static final String MAXIMUM_RATING = "10";

    public static final String IMAGE = "image";
    public static final String EXTRA_MOVIE = "extraMovie";

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mImage;
    private TextView mTitleTextView;
    private TextView mOverviewTextView;
    private TextView mRatingTextView;
    private TextView mListTextView;
    private RecyclerView mRecycler;
    private ReviewsAdapter mReviewsAdapter;
    private VideosAdapter mVideosAdapter;
    private Button mButton;
    private LoadingView mLoadingView;

    public static void navigate(@NonNull AppCompatActivity activity, @NonNull View transitionImage,
                                @NonNull Movie movie) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mCollapsingToolbar = findViewById(R.id.toolbar_layout);
        mImage = findViewById(R.id.image);
        mTitleTextView = findViewById(R.id.title);
        mOverviewTextView = findViewById(R.id.overview);
        mRatingTextView = findViewById(R.id.rating);
        mListTextView = findViewById(R.id.list_title);
        mRecycler = findViewById(R.id.response_list);
        mButton = findViewById(R.id.button);

        setSupportActionBar(mToolbar);
        ViewCompat.setTransitionName(findViewById(R.id.app_bar), IMAGE);
        @SuppressLint("CutPasteId") Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // инициализация адаптеров для списков
        List<Review> list_reviews = new ArrayList<>();
        List<Video> list_videos = new ArrayList<>();
        Review review = new Review();
        Video video = new Video();
        review.setAuthor("No reviews");
        video.setName("No videos");
        list_reviews.add(review);
        list_videos.add(video);

        // выбор ревью из списка
        mReviewsAdapter = new ReviewsAdapter(list_reviews, new ReviewsAdapter.OnItemClick() {
            @Override
            public void onItemClick(@NonNull Review review) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MovieDetailsActivity.this);

                // set title
                alertDialogBuilder.setTitle(review.getAuthor());

                // set dialog message
                alertDialogBuilder
                        .setMessage(review.getContent())
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, id) -> dialog.cancel());

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        // выбор трейлера из списка
        mVideosAdapter = new VideosAdapter(list_videos, new VideosAdapter.OnItemClick() {
            @Override
            public void onItemClick(@NonNull Video video) {
                Videos.browseVideo(getApplicationContext(), video);
            }
        });

        /*if (TextUtils.equals(mButton.getText(), "Trailers")) {
            mReviewTextView.setText(getString(R.string.reviews_title));
            mRecycler.setAdapter(mReviewsAdapter);
        }
        else {
            mReviewTextView.setText(getString(R.string.videos_title));
            mRecycler.setAdapter(mVideosAdapter);
        }*/

        /*mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mListTextView.setText(getString(R.string.reviews_title));
        mRecycler.setAdapter(mReviewsAdapter);*/

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        if (TextUtils.equals(mButton.getText(), "Trailers")) {
            mListTextView.setText(getString(R.string.reviews_title));
            mRecycler.setAdapter(mReviewsAdapter);
        }
        else {
            mListTextView.setText(getString(R.string.videos_title));
            mRecycler.setAdapter(mVideosAdapter);
        }

        // отображение информации о фильме и списков
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        showMovie(movie);
    }

    // инициализация меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareWindowForAnimation() {
        Slide transition = new Slide();
        transition.excludeTarget(android.R.id.statusBarBackground, true);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setEnterTransition(transition);
        getWindow().setReturnTransition(transition);
    }

    private void showMovie(@NonNull Movie movie) {
        String title = getString(R.string.movie_details);
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        Images.loadMovie(mImage, movie, Images.WIDTH_780);

        String year = movie.getReleasedDate().substring(0, 4);
        mTitleTextView.setText(getString(R.string.movie_title, movie.getTitle(), year));
        mOverviewTextView.setText(movie.getOverview());

        String average = String.valueOf(movie.getVoteAverage());
        average = average.length() > 3 ? average.substring(0, 3) : average;
        average = average.length() == 3 && average.charAt(2) == '0' ? average.substring(0, 1) : average;
        mRatingTextView.setText(getString(R.string.rating, average, MAXIMUM_RATING));

        /*--------------------------------------------------*/

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this, getSupportLoaderManager());
        ReviewsUseCase reviewsUseCase = new ReviewsUseCase(RepositoryProvider.getMoviesRepository(), RxUtils.async());
        VideosUseCase videosUseCase = new VideosUseCase(RepositoryProvider.getMoviesRepository(), RxUtils.async());
        ReviewsVideosPresenter presenter = new ReviewsVideosPresenter(movie,
                reviewsUseCase,
                videosUseCase,
                this,
                lifecycleHandler);
        presenter.init();
    }

    // переключатель между списками
    public void onClickButton(View view) {

        if (TextUtils.equals(mButton.getText(), "Trailers")) {
            mListTextView.setText(getString(R.string.videos_title));
            mButton.setText("Reviews");
            mRecycler.setAdapter(mVideosAdapter);
        }
        else {
            mListTextView.setText(getString(R.string.reviews_title));
            mButton.setText("Trailers");
            mRecycler.setAdapter(mReviewsAdapter);
        }
    }

    // передача данных адаптерам списков
    @Override
    public void showReviewsAndVideos(@NonNull ReviewsAndVideos reviewsAndVideos) {
        mVideosAdapter.changeDataSet(reviewsAndVideos.getVideos());
        mReviewsAdapter.changeDataSet(reviewsAndVideos.getReviews());
    }

    // ошибка
    @Override
    public void showError() {
        Toast.makeText(this, "Network error",
                Toast.LENGTH_SHORT)
                .show();
    }

    // управление индикацией

    @Override
    public void showLoadingIndicator() {
        mLoadingView.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        mLoadingView.hideLoadingIndicator();
    }
}