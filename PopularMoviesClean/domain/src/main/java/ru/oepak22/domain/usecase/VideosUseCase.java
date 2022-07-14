package ru.oepak22.domain.usecase;

import java.util.List;

import ru.oepak22.domain.MoviesRepository;
import ru.oepak22.domain.model.Movie;
import ru.oepak22.domain.model.Video;
import rx.Observable;

// класс бизнес-логики (управление потоками)
public class VideosUseCase {

    private final MoviesRepository mRepository;
    private final Observable.Transformer<List<Video>, List<Video>> mAsyncTransformer;

    // конструктор
    public VideosUseCase(MoviesRepository repository,
                         Observable.Transformer<List<Video>, List<Video>> asyncTransformer) {

        mRepository = repository;
        mAsyncTransformer = asyncTransformer;
    }

    // получение данных из репозитория
    public Observable<List<Video>> popularVideos(Movie movie) {
        return mRepository.popularVideos(movie).compose(mAsyncTransformer);
    }
}
