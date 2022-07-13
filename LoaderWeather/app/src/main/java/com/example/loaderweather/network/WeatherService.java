package com.example.loaderweather.network;

import androidx.annotation.NonNull;

import com.example.loaderweather.model.City;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// интерфейс для запроса информации о погоде
public interface WeatherService {

    @GET("data/2.5/weather?units=metric")
    Call<City> getWeather(@NonNull @Query("q") String query);
}
