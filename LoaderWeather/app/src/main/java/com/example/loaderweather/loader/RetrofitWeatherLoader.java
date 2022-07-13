package com.example.loaderweather.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;

import com.example.loaderweather.model.City;
import com.example.loaderweather.network.ApiFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// класс лоадера для загрузки погоды в выбранном городе
public class RetrofitWeatherLoader extends Loader<City> {

    // эземпляр вызова запроса
    private final Call<City> mCall;

    // экземпляр города с погодой
    @Nullable
    private City mCity;

    // конструктор, принимает название города
    public RetrofitWeatherLoader(Context context, @NonNull String cityName) {
        super(context);
        mCall = ApiFactory.getWeatherService().getWeather(cityName);
    }

    // начало загрузки
    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        // если результат уже есть - доставка
        if (mCity != null) {
            deliverResult(mCity);
        } else {
            forceLoad();
        }
    }


    // процесс загрузки
    @Override
    protected void onForceLoad() {
        super.onForceLoad();

        // доставка результата
        mCall.enqueue(new Callback<City>() {

            @Override
            public void onResponse(@NonNull Call<City> call, @NonNull Response<City> response) {
                mCity = response.body();
                deliverResult(mCity);
            }

            @Override
            public void onFailure(@NonNull Call<City> call, @NonNull Throwable t) {
                deliverResult(null);
            }
        });
    }

    // остановка загрузки
    @Override
    protected void onStopLoading() {

        // прерывание запроса
        mCall.cancel();
        super.onStopLoading();
    }
}
