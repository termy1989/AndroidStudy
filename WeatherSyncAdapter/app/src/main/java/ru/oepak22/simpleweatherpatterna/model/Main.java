package ru.oepak22.simpleweatherpatterna.model;

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

    // GET и SET
    public int getTemp() {
        return (int) mTemp;
    }
    public int getPressure() {
        return (int) mPressure;
    }
    public int getHumidity() {
        return (int) mHumidity;
    }

    public void setTemp(int temp) {
        mTemp = temp;
    }
    public void setPressure(int pressure) {
        mPressure = pressure;
    }
    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }
}
