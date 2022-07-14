package ru.oepak22.simpleweatherpatterna.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// класс города, для которого смотрится погода
public class City implements Serializable {

    // идентификатор города
    @SerializedName("id")
    private int mCityId;

    // название города
    @SerializedName("name")
    private String mName;

    // список параметров погоды
    @SerializedName("weather")
    private List<Weather> mWeathers;

    // значения погоды
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

    // GET и SET первичного ключа
    public int getCityId() {
        return mCityId;
    }
    public void setCityId(int id) {
        mCityId = id;
    }

    // GET и SET названия города
    @NonNull
    public String getName() {
        return mName;
    }
    public void setName(@NonNull String name) {
        mName = name;
    }

    // GET и SET параметра погоды
    @Nullable
    public Weather getWeather() {
        if (mWeathers == null || mWeathers.isEmpty()) {
            return null;
        }
        return mWeathers.get(0);
    }
    public void setWeathers(@NonNull List<Weather> weathers) {
        mWeathers = weathers;
    }

    // GET и SET значений погоды
    @Nullable
    public Main getMain() {
        return mMain;
    }
    public void setMain(@NonNull Main main) {
        mMain = main;
    }

    // GET и SET скорости ветра
    @Nullable
    public Wind getWind() {
        return mWind;
    }
    public void setWind(@NonNull Wind wind) {
        mWind = wind;
    }
}
