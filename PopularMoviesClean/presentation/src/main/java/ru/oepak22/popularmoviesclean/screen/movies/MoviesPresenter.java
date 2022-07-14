package ru.oepak22.popularmoviesclean.screen.movies;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.domain.usecase.MoviesUseCase;
import ru.oepak22.popularmoviesclean.R;

// класс представления MVP
public class MoviesPresenter {

    private final MoviesView mMoviesView;
    private final MoviesUseCase mMoviesUseCase;
    private final LifecycleHandler mLifecycleHandler;

    // конструктор
    public MoviesPresenter(@NonNull MoviesView moviesView, @NonNull MoviesUseCase moviesUseCase,
                           @NonNull LifecycleHandler lifecycleHandler) {
        mMoviesView = moviesView;
        mMoviesUseCase = moviesUseCase;
        mLifecycleHandler = lifecycleHandler;
    }

    // инициализация
    @SuppressLint("CheckResult")
    public void init() {
        mMoviesUseCase.popularMovies()
                .doOnSubscribe(mMoviesView::showLoadingIndicator)
                .doAfterTerminate(mMoviesView::hideLoadingIndicator)
                .compose(mLifecycleHandler.load(R.id.movies_request_id))
                .subscribe(mMoviesView::showMovies, throwable -> mMoviesView.showError());
    }
}
