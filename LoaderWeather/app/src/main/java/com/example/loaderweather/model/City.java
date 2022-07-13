package com.example.loaderweather.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// класс города, для которого смотрится погода
public class City implements Serializable {

    // название города
    @SerializedName("name")
    private String mName;

    // список параметров погоды
    @SerializedName("weather")
    private List<Weather> mWeathers;

    // значения погоды погоды
    @SerializedName("main")
    private Main mMain;

    // скорость ветра
    @SerializedName("wind")
    private Wind mWind;

    // пустой конструктор
    public City() {}

    // конструктор с названием города
    public City(@NonNull String name) {
        mName = name;
    }

    // GET и SET названия города
    @NonNull
    public String getName() {
        return mName;
    }
    public void setName(@NonNull String name) {
        mName = name;
    }

    // получение параметра погоды
    @Nullable
    public Weather getWeather() {
        if (mWeathers == null || mWeathers.isEmpty()) {
            return null;
        }
        return mWeathers.get(0);
    }

    // получение значений погоды
    @Nullable
    public Main getMain() {
        return mMain;
    }

    // получение скорости ветра
    @Nullable
    public Wind getWind() {
        return mWind;
    }
}
