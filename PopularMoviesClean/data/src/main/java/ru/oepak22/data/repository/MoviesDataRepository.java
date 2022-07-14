package ru.oepak22.data.repository;

import java.util.List;


import ru.oepak22.data.cache.MoviesCacheTransformer;
import ru.oepak22.data.cache.ReviewsCacheTransformer;
import ru.oepak22.data.cache.VideosCacheTransformer;
import ru.oepak22.data.mapper.MoviesMapper;
import ru.oepak22.data.mapper.ReviewsMapper;
import ru.oepak22.data.mapper.VideosMapper;
import ru.oepak22.data.model.response.MoviesResponse;
import ru.oepak22.data.model.response.ReviewsResponse;
import ru.oepak22.data.model.response.VideosResponse;
import ru.oepak22.data.network.ApiFactory;
import ru.oepak22.domain.MoviesRepository;
import ru.oepak22.domain.model.Movie;
import ru.oepak22.domain.model.Review;
import ru.oepak22.domain.model.Video;
import rx.Observable;

// класс репозитория данных
public class MoviesDataRepository implements MoviesRepository {

    @Override
    public Observable<List<Movie>> popularMovies() {

        return ApiFactory.getMoviesService()
                .popularMovies()
                .map(MoviesResponse::getMovies)
                .compose(new MoviesCacheTransformer())
                .flatMap(Observable::from)
                .map(new MoviesMapper())
                .toList();
    }

    @Override
    public Observable<List<Review>> popularReviews(Movie movie) {
        return ApiFactory.getMoviesService()
                .popularReviews(movie.getId())
                .map(ReviewsResponse::getReviews)
                .compose(new ReviewsCacheTransformer())
                .flatMap(Observable::from)
                .map(new ReviewsMapper())
                .toList();
    }

    @Override
    public Observable<List<Video>> popularVideos(Movie movie) {
        return ApiFactory.getMoviesService()
                .popularVideos(movie.getId())
                .map(VideosResponse::getVideos)
                .compose(new VideosCacheTransformer())
                .flatMap(Observable::from)
                .map(new VideosMapper())
                .toList();
    }

}
