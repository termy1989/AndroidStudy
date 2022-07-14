package ru.oepak22.simpleweatherpatterna.network;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.oepak22.simpleweatherpatterna.model.City;

// интерфейс для запроса информации о погоде
public interface WeatherService {

    @GET("data/2.5/weather?units=metric")
    Call<City> getWeather(@NonNull @Query("q") String query);
}
