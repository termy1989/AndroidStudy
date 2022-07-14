package ru.oepak22.simpleweatherpatterna;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import ru.oepak22.simpleweatherpatterna.activity.AddCityDialog;
import ru.oepak22.simpleweatherpatterna.activity.SettingsActivity;
import ru.oepak22.simpleweatherpatterna.activity.WeatherActivity;
import ru.oepak22.simpleweatherpatterna.adapter.CitiesAdapter;
import ru.oepak22.simpleweatherpatterna.model.City;
import ru.oepak22.simpleweatherpatterna.model.Main;
import ru.oepak22.simpleweatherpatterna.model.Weather;
import ru.oepak22.simpleweatherpatterna.model.Wind;
import ru.oepak22.simpleweatherpatterna.sqlite.CityContentProvider;
import ru.oepak22.simpleweatherpatterna.sqlite.CityContract;
import ru.oepak22.simpleweatherpatterna.view.LoadingDialog;
import ru.oepak22.simpleweatherpatterna.view.LoadingView;
import ru.oepak22.simpleweatherpatterna.view.SimpleDividerItemDecoration;

public class MainActivity extends AppCompatActivity implements CitiesAdapter.OnItemClick,
        CitiesAdapter.OnLongItemClick {

    //private static final String CITY_LIST = "cities";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CitiesAdapter mAdapter;
    private LoadingView mLoadingView;

    private final List<City> loadedCities = new ArrayList<>();
    //private List<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty);

        setSupportActionBar(mToolbar);

        // инициализация обновлялки
        mSwipeRefreshLayout = findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

            ContentResolver.requestSync(WeatherApp.newAccount,
                    CityContentProvider.AUTHORITY, bundle);

            //loadWeather(true);

            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 4000);
        });

        mSwipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        // инициализация индикатора загрузки
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        /*if (savedInstanceState == null) {
            loadWeather(false);
        } else {
            loadWeather(true);
        }*/

        loadWeather(false);
    }

    // загрузка
    private void loadWeather(boolean restart) {

        // открытие индикатора загрузки
        mLoadingView.showLoadingIndicator();

        // инициализация callback для загрузки информации о погоде
        LoaderManager.LoaderCallbacks<Cursor> callbacks = new WeatherCallbacks();

        // рестарт загрузки
        // замена экземпляра LoaderCallbacks на новый и передача в его onLoadFInished загруженных данных
        if (restart) getSupportLoaderManager().restartLoader(0, Bundle.EMPTY, callbacks);

        // инициализация загрузки
        else getSupportLoaderManager().initLoader(0, Bundle.EMPTY, callbacks);
    }

    // вывод списка городов
    @SuppressLint("Range")
    private void showCityList(Cursor cursor) {

        if (loadedCities.size() != 0)
            loadedCities.clear();

        //if(cursor == null)
        cursor = getContentResolver().query(CityContract.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            City city = new City();
            Main main = new Main();
            Weather weather = new Weather();
            List<Weather> weatherList = new ArrayList<>();
            Wind wind = new Wind();
            String tmp;

            city.setName(cursor.getString(cursor.getColumnIndex(CityContract.NAME)));

            tmp = cursor.getString(cursor.getColumnIndex(CityContract.TEMP));
            /*if (!TextUtils.isEmpty(tmp))*/ main.setTemp(Integer.parseInt(tmp));

            tmp = cursor.getString(cursor.getColumnIndex(CityContract.PRESSURE));
            /*if (!TextUtils.isEmpty(tmp))*/ main.setPressure(Integer.parseInt(tmp));

            tmp = cursor.getString(cursor.getColumnIndex(CityContract.HUMIDITY));
            /*if (!TextUtils.isEmpty(tmp)) {*/
                main.setHumidity(Integer.parseInt(tmp));
                city.setMain(main);
            //}

            tmp = cursor.getString(cursor.getColumnIndex(CityContract.MAIN));
            //if (!TextUtils.isEmpty(tmp)) {
                weather.setMain(tmp);
                weatherList.add(weather);
                city.setWeathers(weatherList);
            //}

            tmp = cursor.getString(cursor.getColumnIndex(CityContract.SPEED));
            //if (!TextUtils.isEmpty(tmp)) {
                wind.setSpeed(Integer.parseInt(tmp));
                city.setWind(wind);
            //}

            loadedCities.add(city);
        }

        // вывод списка
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, false));
        mAdapter = new CitiesAdapter(loadedCities, this, this);
        mRecyclerView.setAdapter(mAdapter);

        mLoadingView.hideLoadingIndicator();
    }

    // нажатие на элемент списка
    @Override
    public void onItemClick(@NonNull City city) {
        startActivity(WeatherActivity.makeIntent(this, city));
    }

    // долгое нажатие на элемент списка
    @Override
    public void onLongItemClick(@NonNull City city) {
        Bundle bundle = new Bundle();
        bundle.putString("name", city.getName());
        showDialog(0, bundle);
    }

    // инициализация меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cities, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // обработчик меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            new AddCityDialog().show(getFragmentManager(), AddCityDialog.class.getName());
            return true;
        } else if (item.getItemId() == R.id.action_prefs) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // диалог удаления записи
    @Override
    protected Dialog onCreateDialog(int id, Bundle bundle) {

        String str = bundle.getString("name");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Remove selected city?")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            (dialogInterface, i) -> getContentResolver().delete(CityContract.CONTENT_URI,
                                        "name=?", new String[] { str }))
                    .setNegativeButton("Cancel",
                            (dialogInterface, i) -> dialogInterface.cancel());

        return alertDialog.create();
    }

    private class WeatherCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new CursorLoader(MainActivity.this,
                    CityContract.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            showCityList(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    }
}