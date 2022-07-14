package ru.oepak22.popularmoviesclean.screen.details;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.domain.model.Movie;
import ru.oepak22.domain.model.ReviewsAndVideos;
import ru.oepak22.domain.usecase.ReviewsUseCase;
import ru.oepak22.domain.usecase.VideosUseCase;
import ru.oepak22.popularmoviesclean.R;
import rx.Observable;

// класс представления MVP
public class ReviewsVideosPresenter {

    private final Movie mMovie;
    private final ReviewsUseCase mReviewsUseCase;
    private final VideosUseCase mVideosUseCase;
    private final ReviewsVideosView mReviewsVideosView;
    private final LifecycleHandler mLifecycleHandler;


    // конструктор
    public ReviewsVideosPresenter(@NonNull Movie movie,
                                  @NonNull ReviewsUseCase reviewsUseCase,
                                  @NonNull VideosUseCase videosUseCase,
                                  @NonNull ReviewsVideosView reviewsVideosView,
                                  @NonNull LifecycleHandler lifecycleHandler) {
        mMovie = movie;
        mLifecycleHandler = lifecycleHandler;
        mReviewsUseCase = reviewsUseCase;
        mVideosUseCase = videosUseCase;
        mReviewsVideosView = reviewsVideosView;
    }

    // инициализация
    @SuppressLint("CheckResult")
    public void init() {

        Observable.zip(mReviewsUseCase.popularMovies(mMovie),
                        mVideosUseCase.popularVideos(mMovie),
                        ReviewsAndVideos::new)
                    .doOnSubscribe(mReviewsVideosView::showLoadingIndicator)
                    .doAfterTerminate(mReviewsVideosView::hideLoadingIndicator)
                    .compose(mLifecycleHandler.load(R.id.reviews_videos_request_id))
                    .subscribe(mReviewsVideosView::showReviewsAndVideos,
                                throwable -> mReviewsVideosView.showError());

        /*
                 mSubscription = Observable.zip(getReviews(movie), getVideos(movie), Result::new)
                                    .doOnSubscribe(disposable -> loadingView.showLoadingIndicator())
                                    .doAfterTerminate(loadingView::hideLoadingIndicator)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::showResults, throwable -> Toast.makeText(this, "Network error",
                                            Toast.LENGTH_SHORT)
                                            .show());*/
    }


}
