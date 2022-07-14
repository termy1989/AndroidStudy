package ru.oepak22.domain.usecase;

import java.util.List;


import ru.oepak22.domain.MoviesRepository;
import ru.oepak22.domain.model.Movie;
import rx.Observable;

// класс бизнес-логики (управление потоками)
public class MoviesUseCase {

    private final MoviesRepository mRepository;
    private final Observable.Transformer<List<Movie>, List<Movie>> mAsyncTransformer;

    // конструктор
    public MoviesUseCase(MoviesRepository repository,
                         Observable.Transformer<List<Movie>, List<Movie>> asyncTransformer) {

        mRepository = repository;
        mAsyncTransformer = asyncTransformer;
    }

    // получение данных из репозитория
    public Observable<List<Movie>> popularMovies() {
        return mRepository.popularMovies().compose(mAsyncTransformer);
    }
}
