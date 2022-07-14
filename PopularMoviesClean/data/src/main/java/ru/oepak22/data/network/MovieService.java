package ru.oepak22.data.network;

import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.oepak22.data.model.response.MoviesResponse;
import ru.oepak22.data.model.response.ReviewsResponse;
import ru.oepak22.data.model.response.VideosResponse;
import rx.Observable;

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
