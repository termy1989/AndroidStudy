package com.example.loaderweather.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loaderweather.R;
import com.example.loaderweather.model.City;

// класс показа данных элемента списка
public class CityHolder extends RecyclerView.ViewHolder {

    TextView mCityName;     // название города
    TextView mTemperature;  // температура

    // конструктор
    public CityHolder(@NonNull View itemView) {
        super(itemView);
        mCityName = itemView.findViewById(R.id.city_name);
        mTemperature = itemView.findViewById(R.id.temperature);
    }

    // размещение данных на элементе списка
    public void bind(@NonNull City city) {
        mCityName.setText(city.getName());
        if (city.getMain() != null) {
            String temp = mTemperature.getResources().getString(R.string.f_temperature, city.getMain().getTemp());
            mTemperature.setText(temp);
        }
    }
}
