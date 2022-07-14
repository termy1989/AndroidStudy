package ru.oepak22.simpleweatherpatterna.sqlite;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

// развернутый класс для помещения в таблицу
public class CityTable {


    private int mId;        // идентификатор
    private String mName;   // название города
    private int mTemp;      // температура
    private int mPressure;  // давление
    private int mHumidity;  // влажность
    private String mMain;   // параметр погоды
    private int mSpeed;     // скорость ветра

    // GET
    public int getId() { return mId; }
    public String getName() {
        return mName;
    }
    public int getTemp() {
        return (int) mTemp;
    }
    public int getPressure() {
        return (int) mPressure;
    }
    public int getHumidity() {
        return (int) mHumidity;
    }
    public String getMain() {
        return mMain;
    }
    public int getSpeed() { return mSpeed; }

    // SET
    public void setId(int id) { mId = id; }
    public void setName(String name) {
        mName = name;
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
    public void setMain(String main) {
        mMain = main;
    }
    public void setWind(int speed) {
        mSpeed = speed;
    }


}
