package ru.oepak22.domain.usecase;

import java.util.List;

import ru.oepak22.domain.MoviesRepository;
import ru.oepak22.domain.model.Movie;
import ru.oepak22.domain.model.Review;
import rx.Observable;

// класс бизнес-логики (управление потоками)
public class ReviewsUseCase {

    //private final Movie mMovie;
    private final MoviesRepository mRepository;
    private final Observable.Transformer<List<Review>, List<Review>> mAsyncTransformer;

    // конструктор
    public ReviewsUseCase(/*Movie movie,*/ MoviesRepository repository,
                          Observable.Transformer<List<Review>, List<Review>> asyncTransformer) {

        //mMovie = movie;
        mRepository = repository;
        mAsyncTransformer = asyncTransformer;
    }

    // получение данных из репозитория
    public Observable<List<Review>> popularMovies(Movie movie) {
        return mRepository.popularReviews(movie).compose(mAsyncTransformer);
    }
}
