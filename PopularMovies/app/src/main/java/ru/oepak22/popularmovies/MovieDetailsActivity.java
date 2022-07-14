package ru.oepak22.popularmovies;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Slide;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.oepak22.popularmovies.adapter.MoviesAdapter;
import ru.oepak22.popularmovies.adapter.ReviewsAdapter;
import ru.oepak22.popularmovies.model.Movie;
import ru.oepak22.popularmovies.model.MoviesResponse;
import ru.oepak22.popularmovies.model.Review;
import ru.oepak22.popularmovies.model.ReviewsResponse;
import ru.oepak22.popularmovies.model.Video;
import ru.oepak22.popularmovies.model.VideosResponse;
import ru.oepak22.popularmovies.network.ApiFactory;
import ru.oepak22.popularmovies.utils.Images;
import ru.oepak22.popularmovies.utils.Videos;
import ru.oepak22.popularmovies.view.LoadingDialog;
import ru.oepak22.popularmovies.view.LoadingView;

public class MovieDetailsActivity extends AppCompatActivity implements ReviewsAdapter.OnItemClick {

    private static final String MAXIMUM_RATING = "10";

    public static final String IMAGE = "image";
    public static final String EXTRA_MOVIE = "extraMovie";

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ImageView mImage;
    private TextView mTitleTextView;
    private TextView mOverviewTextView;
    private TextView mRatingTextView;
    private TextView mReviewTextView;
    private RecyclerView mReviewsRecycler;
    private ReviewsAdapter mAdapter;
    private Button mButton;

    @Nullable
    private Disposable mReviewsSubscription;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void navigate(@NonNull AppCompatActivity activity, @NonNull View transitionImage,
                                @NonNull Movie movie) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareWindowForAnimation();
        setContentView(R.layout.activity_movie_details);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mCollapsingToolbar = findViewById(R.id.toolbar_layout);
        mImage = findViewById(R.id.image);
        mTitleTextView = findViewById(R.id.title);
        mOverviewTextView = findViewById(R.id.overview);
        mRatingTextView = findViewById(R.id.rating);
        mReviewTextView = findViewById(R.id.review);
        mReviewsRecycler = findViewById(R.id.review_list);
        mButton = findViewById(R.id.button);

        setSupportActionBar(mToolbar);
        ViewCompat.setTransitionName(findViewById(R.id.app_bar), IMAGE);
        @SuppressLint("CutPasteId") Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        showMovie(movie);
    }

    // активность на паузе
    @Override
    protected void onPause() {

        // отписка от источника данных
        if (mReviewsSubscription != null) {
            mReviewsSubscription.dispose();
        }
        super.onPause();
    }

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

        mReviewTextView.setText(getString(R.string.reviews_title));

        List<Review> list = new ArrayList<>();
        Review review = new Review();
        review.setAuthor("No reviews");
        list.add(review);
        mReviewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReviewsAdapter(list, this);
        mReviewsRecycler.setAdapter(mAdapter);

        LoadingView loadingView = LoadingDialog.view(getSupportFragmentManager());
        /*compositeDisposable.add(ApiFactory.getMoviesService()
                .popularVideos(movie.getId())
                .map(VideosResponse::getVideos)
                .flatMap(videos -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Video.class);
                        realm.insert(videos);
                    });
                    return Observable.just(videos);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Video> results = realm.where(Video.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));
                })
                .doOnSubscribe(disposable -> loadingView.showLoadingIndicator())
                .doAfterTerminate(loadingView::hideLoadingIndicator)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showTrailers, throwable -> showError())
        );
        compositeDisposable.add(ApiFactory.getMoviesService()
                .popularReviews(movie.getId())
                .map(ReviewsResponse::getReviews)
                .flatMap(reviews -> {                                                       // замена источника данных
                    Realm.getDefaultInstance().executeTransaction(realm -> {                // кэширование
                        realm.delete(Review.class);                                         // удаление из локальной базы старого списка
                        realm.insert(reviews);                                              // добавление в базу нового списка
                    });
                    return Observable.just(reviews);
                })
                .onErrorResumeNext(throwable -> {                                           // замена потока данных в случае ошибки
                    Realm realm = Realm.getDefaultInstance();                               // обращение к закэшированным данным
                    RealmResults<Review> results = realm.where(Review.class).findAll();     // извлечение данных из таблицы
                    return Observable.just(realm.copyFromRealm(results));                   // поток данных - локальная таблица
                })
                .doOnSubscribe(disposable -> loadingView.showLoadingIndicator())            // при подписке открывается индикатор загрузки
                .doAfterTerminate(loadingView::hideLoadingIndicator)                        // при ошибке или завершении потока закрывается индикатор загрузки
                .subscribeOn(Schedulers.io())                                               // получение данных - в дополнительном потоке
                .observeOn(AndroidSchedulers.mainThread())                                  // обработка данных - в основном потоке
                .subscribe(this::showReviews, throwable -> showError())                     // отображение полученных данных или ошибка
        );*/
        mReviewsSubscription = ApiFactory.getMoviesService()
                .popularReviews(movie.getId())
                .map(ReviewsResponse::getReviews)
                .flatMap(reviews -> {                                                       // замена источника данных
                    Realm.getDefaultInstance().executeTransaction(realm -> {                // кэширование
                        realm.delete(Review.class);                                         // удаление из локальной базы старого списка
                        realm.insert(reviews);                                              // добавление в базу нового списка
                    });
                    return Observable.just(reviews);
                })
                .onErrorResumeNext(throwable -> {                                           // замена потока данных в случае ошибки
                    Realm realm = Realm.getDefaultInstance();                               // обращение к закэшированным данным
                    RealmResults<Review> results = realm.where(Review.class).findAll();     // извлечение данных из таблицы
                    return Observable.just(realm.copyFromRealm(results));                   // поток данных - локальная таблица
                })
                .doOnSubscribe(disposable -> loadingView.showLoadingIndicator())            // при подписке открывается индикатор загрузки
                .doAfterTerminate(loadingView::hideLoadingIndicator)                        // при ошибке или завершении потока закрывается индикатор загрузки
                .subscribeOn(Schedulers.io())                                               // получение данных - в дополнительном потоке
                .observeOn(AndroidSchedulers.mainThread())                                  // обработка данных - в основном потоке
                .subscribe(this::showReviews, throwable -> showError());                    // отображение полученных данных или ошибка
    }

    private void showTrailers(@NonNull List<Video> videos) {

        Video video = new Video();
        video.setKey("bOYvq-Sj4Ls");
        Videos.browseVideo(this, video);
    }

    private void showReviews(@NonNull List<Review> reviews) {

        /*mReviewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReviewsAdapter(reviews, this);
        mReviewsRecycler.setAdapter(mAdapter);*/

        mAdapter.changeDataSet(reviews);
    }

    private void showError() {

        List<Review> reviews = new ArrayList<>();
        Review review = new Review();
        review.setAuthor("No reviews");
        reviews.add(review);

        mReviewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReviewsAdapter(reviews, this);
        mReviewsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(@NonNull Review review) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

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

    public void onClickButton(View view) {

        if (TextUtils.equals(mButton.getText(), "Trailers")) {
            mButton.setText("Reviews");
        }
        else {
            mButton.setText("Trailers");
        }
    }
}

/**
 * TODO : task
 *
 * Load movie trailers and reviews and display them
 *
 * 1) See http://docs.themoviedb.apiary.io/#reference/movies/movieidtranslations/get?console=1
 * http://docs.themoviedb.apiary.io/#reference/movies/movieidtranslations/get?console=1
 * for API documentation
 *
 * 2) Add requests to {@link ru.gdgkazan.popularmovies.network.MovieService} for trailers and videos
 *
 * 3) Execute requests in parallel and show loading progress until both of them are finished
 *
 * 4) Save trailers and videos to Realm and use cached version when error occurred
 *
 * 5) Handle lifecycle changes any way you like
 */

/*        mReviewsSubscription = ApiFactory.getMoviesService()
                .popularReviews(movie.getId())
                .map(ReviewsResponse::getReviews)
                .flatMap(reviews -> {                                                       // замена источника данных
                    Realm.getDefaultInstance().executeTransaction(realm -> {                // кэширование
                        realm.delete(Review.class);                                         // удаление из локальной базы старого списка
                        realm.insert(reviews);                                              // добавление в базу нового списка
                    });
                    return Observable.just(reviews);
                })
                .onErrorResumeNext(throwable -> {                                           // замена потока данных в случае ошибки
                    Realm realm = Realm.getDefaultInstance();                               // обращение к закэшированным данным
                    RealmResults<Review> results = realm.where(Review.class).findAll();     // извлечение данных из таблицы
                    return Observable.just(realm.copyFromRealm(results));                   // поток данных - локальная таблица
                })
                .doOnSubscribe(disposable -> loadingView.showLoadingIndicator())            // при подписке открывается индикатор загрузки
                .doAfterTerminate(loadingView::hideLoadingIndicator)                        // при ошибке или завершении потока закрывается индикатор загрузки
                .subscribeOn(Schedulers.io())                                               // получение данных - в дополнительном потоке
                .observeOn(AndroidSchedulers.mainThread())                                  // обработка данных - в основном потоке
                .subscribe(this::showReviews, throwable -> showError());                    // отображение полученных данных или ошибка
                */