package ru.oepak22.simpleweatherpatterna.network;

import androidx.annotation.StringDef;

@StringDef({NetworkRequest.CITY_WEATHER})
public @interface NetworkRequest {
    String CITY_WEATHER = "city_weather";
}
