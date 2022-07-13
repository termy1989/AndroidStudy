package com.example.loaderweather.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// класс значений погоды
public class Main implements Serializable {

    // температура
    @SerializedName("temp")
    private double mTemp;

    // давление
    @SerializedName("pressure")
    private double mPressure;

    // влажность
    @SerializedName("humidity")
    private double mHumidity;

    // GET
    public int getTemp() {
        return (int) mTemp;
    }
    public int getPressure() {
        return (int) mPressure;
    }
    public int getHumidity() {
        return (int) mHumidity;
    }
}
