package ru.oepak22.simpleweatherpatterna.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// класс параметра погоды
public class Weather implements Serializable {

    // название параметра
    @SerializedName("main")
    private String mMain;

    // иконка
    @SerializedName("icon")
    private String mIcon;

    // GET и SET низвания
    @NonNull
    public String getMain() {
        return mMain;
    }
    public void setMain(@NonNull String main) {
        mMain = main;
    }

    // GET и SET иконки
    @NonNull
    public String getIcon() {
        return mIcon;
    }
    public void setIcon(@NonNull String icon) {
        mIcon = icon;
    }
}
