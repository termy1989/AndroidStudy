package ru.oepak22.simpleweatherpatterna.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.oepak22.simpleweatherpatterna.R;
import ru.oepak22.simpleweatherpatterna.WeatherApp;
import ru.oepak22.simpleweatherpatterna.model.City;
import ru.oepak22.simpleweatherpatterna.network.ApiFactory;
import ru.oepak22.simpleweatherpatterna.sqlite.CityContentProvider;
import ru.oepak22.simpleweatherpatterna.sqlite.CityContract;
import ru.oepak22.simpleweatherpatterna.sync.SyncAdapter;

public class AddCityDialog extends DialogFragment {

    private EditText mCityName;

    @SuppressLint("WrongViewCast")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add, null);
        mCityName = view.findViewById(R.id.name);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.add_city)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> onAddCity())
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private void onAddCity() {
        final String name = mCityName.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            /*final ContentValues values = new ContentValues();
            values.put(CityContract.NAME, name);
            getActivity().getContentResolver().insert(CityContract.CONTENT_URI, values);
            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            ContentResolver.requestSync(WeatherApp.newAccount,
                    CityContentProvider.AUTHORITY, bundle);*/

            ContentResolver cr = getActivity().getContentResolver();

            ApiFactory.getWeatherService()
                    .getWeather(name)
                    .enqueue(new Callback<City>() {

                        @Override
                        public void onResponse(@NonNull Call<City> call, @NonNull Response<City> response) {

                            final ContentValues channelValues = new ContentValues();

                            if (response.body() != null) {
                                channelValues.put(CityContract.NAME,
                                        response.body().getName());
                                channelValues.put(CityContract.TEMP,
                                        String.valueOf(response.body().getMain().getTemp()));
                                channelValues.put(CityContract.PRESSURE,
                                        String.valueOf(response.body().getMain().getPressure()));
                                channelValues.put(CityContract.HUMIDITY,
                                        String.valueOf(response.body().getMain().getHumidity()));
                                channelValues.put(CityContract.MAIN,
                                        response.body().getWeather().getMain());
                                channelValues.put(CityContract.SPEED,
                                        String.valueOf(response.body().getWind().getSpeed()));

                                cr.insert(CityContract.CONTENT_URI, channelValues);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<City> call, @NonNull Throwable t) {
                            Toast.makeText(getActivity(), "City not found!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}