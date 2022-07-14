package ru.oepak22.simpleweatherpatterna.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ru.oepak22.simpleweatherpatterna.model.City;
import ru.oepak22.simpleweatherpatterna.network.ApiFactory;
import ru.oepak22.simpleweatherpatterna.sqlite.CityContentProvider;
import ru.oepak22.simpleweatherpatterna.sqlite.CityTable;
import ru.oepak22.simpleweatherpatterna.sqlite.CityContract;

// класс синхронизации
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String KEY_CITY_ID = "ru.oepak22.simpleweatherpatterna.KEY_CITY_ID";

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    // выполнение синхронизации
    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {

        Log.v("XXXX", "onPerformSync...");

        final long cityId = bundle.getLong(KEY_CITY_ID, -1);

        if (cityId > 0) {
            syncData(contentProviderClient, syncResult,
                    CityContract.CITY_ID + "=?",
                    new String[]{String.valueOf(cityId)});
        } else {
            syncData(contentProviderClient, syncResult, null, null);
        }

    }

    // синхронизация
    private void syncData(ContentProviderClient provider,
                          SyncResult syncResult,
                          String where, String[] whereArgs) {

        try {
            try (Cursor cities = provider.query(CityContract.CONTENT_URI, new String[]{
                            CityContract.CITY_ID,
                            CityContract.NAME
                    },
                    where,
                    whereArgs,
                    null)) {
                if (cities.moveToFirst()) {
                    do {
                        requestData(cities.getString(0),
                                cities.getString(1),
                                provider, syncResult);
                    } while (cities.moveToNext());
                }
            }
        }
        catch (RemoteException e) {
            Log.e(SyncAdapter.class.getName(), e.getMessage(), e);
            ++syncResult.stats.numIoExceptions;
        }
    }


    // запрос данных с сервера
    private void requestData(String cityId, String cityName,
                             ContentProviderClient provider,
                             SyncResult syncResult) {

        try {
            City response = ApiFactory.getWeatherService()
                    .getWeather(cityName)
                    .execute()
                    .body();

            if (response != null) {
                final ContentValues channelValues = new ContentValues();

                channelValues.put(CityContract.NAME, response.getName());
                channelValues.put(CityContract.TEMP, String.valueOf(response.getMain().getTemp()));
                channelValues.put(CityContract.PRESSURE, String.valueOf(response.getMain().getPressure()));
                channelValues.put(CityContract.HUMIDITY, String.valueOf(response.getMain().getHumidity()));
                channelValues.put(CityContract.MAIN, response.getWeather().getMain());
                channelValues.put(CityContract.SPEED, String.valueOf(response.getWind().getSpeed()));
            /*if (!TextUtils.isEmpty(response.getName()))
                channelValues.put(CityContract.NAME, response.getName());
            else return;
            if (!TextUtils.isEmpty(String.valueOf(response.getMain().getTemp())))
                channelValues.put(CityContract.TEMP, String.valueOf(response.getMain().getTemp()));
            else return;
            if (!TextUtils.isEmpty(String.valueOf(response.getMain().getPressure())))
                channelValues.put(CityContract.PRESSURE, String.valueOf(response.getMain().getPressure()));
            else return;
            if (!TextUtils.isEmpty(String.valueOf(response.getMain().getHumidity())))
                channelValues.put(CityContract.HUMIDITY, String.valueOf(response.getMain().getHumidity()));
            else return;
            if (!TextUtils.isEmpty(response.getWeather().getMain()))
                channelValues.put(CityContract.MAIN, response.getWeather().getMain());
            else return;
            if (!TextUtils.isEmpty(String.valueOf(response.getWind().getSpeed())))
                channelValues.put(CityContract.SPEED, String.valueOf(response.getWind().getSpeed()));
            else return;*/

            /*syncResult.stats.numUpdates += provider
                    .update(CityContract.CONTENT_URI, channelValues,
                            CityContract.CITY_ID + "=?",
                            new String[] { cityId });*/
                getContext().getContentResolver().update(CityContract.CONTENT_URI, channelValues,
                        CityContract.CITY_ID + "=?",
                        new String[]{cityId});
            }

        }
        catch (IOException /*| RemoteException*/ e) {
            Log.e(SyncAdapter.class.getName(), e.getMessage(), e);
            //++syncResult.stats.numIoExceptions;
        }
    }

    /*private void syncData(ContentProviderClient provider,
                          SyncResult syncResult,
                          String where, String[] whereArgs) {

        try {
            final Cursor cities = provider.query(CityContract.CONTENT_URI, new String[] {
                                                                                CityContract.CITY_ID,
                                                                                CityContract.NAME
                                                                                },
                                                                            where,
                                                                            whereArgs,
                                                                    null);
            try {
                if (cities.moveToFirst()) {
                    do {
                        requestData(cities.getString(0),
                                cities.getString(1),
                                provider, syncResult);
                    } while (cities.moveToNext());
                }
            } finally {
                cities.close();
            }
        }
        catch (RemoteException e) {
            Log.e(SyncAdapter.class.getName(), e.getMessage(), e);
            ++syncResult.stats.numIoExceptions;
        }
    }*/
}