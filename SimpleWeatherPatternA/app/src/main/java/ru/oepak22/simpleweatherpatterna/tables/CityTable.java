package ru.oepak22.simpleweatherpatterna.tables;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

import org.sqlite.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.arturvasilov.sqlite.core.BaseTable;
import ru.arturvasilov.sqlite.core.Table;
import ru.arturvasilov.sqlite.utils.TableBuilder;
import ru.oepak22.simpleweatherpatterna.adapter.GsonHolder;
import ru.oepak22.simpleweatherpatterna.model.City;
import ru.oepak22.simpleweatherpatterna.model.Main;
import ru.oepak22.simpleweatherpatterna.model.Weather;
import ru.oepak22.simpleweatherpatterna.model.Wind;

// таблица для городов (с погодой)
public class CityTable extends BaseTable<City> {

    public static final Table<City> TABLE = new CityTable();

    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";
    public static final String WEATHER = "weather";
    public static final String MAIN = "main";
    public static final String WIND = "wind";

    @Override
    public void onCreate(@NonNull SQLiteDatabase database) {
        TableBuilder.create(this)
                .primaryKey(CITY_ID)
                .textColumn(CITY_NAME)
                .textColumn(WEATHER)
                .textColumn(MAIN)
                .textColumn(WIND)
                .execute(database);
    }

    @NonNull
    @Override
    public ContentValues toValues(@NonNull City city) {
        ContentValues values = new ContentValues();
        values.put(CITY_ID, city.getCityId());
        values.put(CITY_NAME, city.getName());
        values.put(WEATHER, GsonHolder.getGson().toJson(city.getWeather()));
        values.put(MAIN, GsonHolder.getGson().toJson(city.getMain()));
        values.put(WIND, GsonHolder.getGson().toJson(city.getWind()));
        return values;
    }

    @SuppressLint("Range")
    @NonNull
    @Override
    public City fromCursor(@NonNull Cursor cursor) {
        City city = new City();

        city.setCityId(cursor.getInt(cursor.getColumnIndex(CITY_ID)));
        city.setName(cursor.getString(cursor.getColumnIndex(CITY_NAME)));

        Weather weather = GsonHolder.getGson().fromJson(cursor.getString(cursor.getColumnIndex(WEATHER)), Weather.class);
        List<Weather> weathers = new ArrayList<>();
        weathers.add(weather);
        city.setWeathers(weathers);

        city.setMain(GsonHolder.getGson().fromJson(cursor.getString(cursor.getColumnIndex(MAIN)), Main.class));
        city.setWind(GsonHolder.getGson().fromJson(cursor.getString(cursor.getColumnIndex(WIND)), Wind.class));

        return city;
    }
}
