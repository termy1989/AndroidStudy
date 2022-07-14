package ru.oepak22.simpleweatherpatterna.tables;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import org.sqlite.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.arturvasilov.sqlite.core.BaseTable;
import ru.arturvasilov.sqlite.core.Table;
import ru.arturvasilov.sqlite.utils.TableBuilder;
import ru.oepak22.simpleweatherpatterna.adapter.GsonHolder;
import ru.oepak22.simpleweatherpatterna.model.AllCities;
import ru.oepak22.simpleweatherpatterna.model.City;
import ru.oepak22.simpleweatherpatterna.model.Main;
import ru.oepak22.simpleweatherpatterna.model.Weather;
import ru.oepak22.simpleweatherpatterna.model.Wind;

public class AllCitiesTable extends BaseTable<AllCities> {

    public static final Table<AllCities> TABLE = new AllCitiesTable();

    public static final String CITY_LIST = "city_list";

    @Override
    public void onCreate(@NonNull SQLiteDatabase database) {
        TableBuilder.create(this)
                .textColumn(CITY_LIST)
                .execute(database);
    }

    @NonNull
    @Override
    public ContentValues toValues(@NonNull AllCities allCities) {
        ContentValues values = new ContentValues();
        values.put(CITY_LIST, GsonHolder.getGson().toJson(allCities.getList()));
        Log.v("JSON", GsonHolder.getGson().toJson(allCities.getList()));
        return values;
    }

    @SuppressLint("Range")
    @NonNull
    @Override
    public AllCities fromCursor(@NonNull Cursor cursor) {

        AllCities allCities = new AllCities();

        Type type = new TypeToken<ArrayList<City>>(){}.getType();
        List<City> cities = GsonHolder.getGson().fromJson(cursor.getString(cursor.getColumnIndex(CITY_LIST)), type);

        allCities.setList(cities);

        return allCities;
    }
}
