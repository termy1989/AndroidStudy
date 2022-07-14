package ru.oepak22.simpleweatherpatterna.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// класс списка городов
public class AllCities implements Serializable {

    @SerializedName("message")
    private String mMessage;

    @SerializedName("cod")
    private String mCod;

    @SerializedName("count")
    private int mCount;

    @SerializedName("list")
    private List<City> mCities;

    // пустой конструктор
    public AllCities() {}

    // GET и SET списка городов
    @NonNull
    public List<City> getList() {
        return mCities;
    }
    public void setList(@NonNull List<City> list) {
        mCities = list;
    }

    // размер списка
    public int CityCount() {
        return mCities.size();
    }
}
