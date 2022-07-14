package ru.oepak22.domain;

import java.util.List;

import ru.oepak22.domain.model.Movie;
import ru.oepak22.domain.model.Review;
import ru.oepak22.domain.model.Video;
import rx.Observable;

// интерфейс для репозитория (источника) данных
public interface MoviesRepository {
    Observable<List<Movie>> popularMovies();
    Observable<List<Review>> popularReviews(Movie movie);
    Observable<List<Video>> popularVideos(Movie movie);
}