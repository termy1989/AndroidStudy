package com.example.loaderweather.loader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import com.example.loaderweather.model.City;
import com.example.loaderweather.network.ApiFactory;

import java.io.IOException;

// класс лоадера для загрузки погоды в выбранном городе
public class WeatherLoader extends AsyncTaskLoader<City> {

    private final String mCityName;

    // конструктор, принимает название города
    public WeatherLoader(Context context, @NonNull String cityName) {
        super(context);
        mCityName = cityName;
    }

    // начало загрузки
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    // выполнение запроса о погоде в указанном городе
    @Override
    public City loadInBackground() {
        try {
            return ApiFactory.getWeatherService().getWeather(mCityName).execute().body();
        } catch (IOException e) {
            return null;
        }
    }
}
