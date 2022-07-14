package ru.oepak22.popularmovies.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.oepak22.popularmovies.model.MoviesResponse;
import ru.oepak22.popularmovies.model.ReviewsResponse;
import ru.oepak22.popularmovies.model.VideosResponse;

// интерфейс запросов
public interface MovieService {

    // запрос на фильмы
    @GET("popular/")
    Observable<MoviesResponse> popularMovies();

    // запрос на ревью
    @GET("{id}/reviews")
    Observable<ReviewsResponse> popularReviews(@Path("id") int id);

    // запрос на трейлеры
    @GET("{id}/videos")
    Observable<VideosResponse> popularVideos(@Path("id") int id);
}
