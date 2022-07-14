package ru.oepak22.popularmoviesagera.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.oepak22.popularmoviesagera.model.response.MoviesResponse;
import ru.oepak22.popularmoviesagera.model.response.ReviewsResponse;
import ru.oepak22.popularmoviesagera.model.response.VideosResponse;
import rx.Observable;

public interface MovieService {

    // запрос на фильмы
    @GET("popular/")
    Call<MoviesResponse> popularMovies();

    // запрос на ревью
    @GET("{id}/reviews")
    Call<ReviewsResponse> popularReviews(@Path("id") int id);

    // запрос на трейлеры
    @GET("{id}/videos")
    Call<VideosResponse> popularVideos(@Path("id") int id);
}
