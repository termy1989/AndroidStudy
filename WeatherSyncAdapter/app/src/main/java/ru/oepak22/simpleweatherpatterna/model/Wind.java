package ru.oepak22.simpleweatherpatterna.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// класс скорости ветра
public class Wind implements Serializable {

    // скорость ветра
    @SerializedName("speed")
    private double mSpeed;

    // GET и SET
    public int getSpeed() {
        return (int) mSpeed;
    }
    public void setSpeed(int speed) { mSpeed = speed; }
}
