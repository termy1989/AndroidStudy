package com.example.loaderweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.loaderweather.loader.RetrofitWeatherLoader;
import com.example.loaderweather.model.City;
import com.example.loaderweather.view.LoadingDialog;
import com.example.loaderweather.view.LoadingView;

// класс активности с информацией о погоде
public class WeatherActivity extends AppCompatActivity {

    private static final String CITY_NAME_KEY = "city_name";

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private View mWeatherLayout;
    private TextView mWeatherMain;
    private TextView mTemperature;
    private TextView mPressure;
    private TextView mHumidity;
    private TextView mWindSpeed;
    private TextView mErrorLayout;

    private LoadingView mLoadingView;

    private String mCityName;

    @NonNull
    public static Intent makeIntent(@NonNull Activity activity, @NonNull String cityName) {
        Intent intent = new Intent(activity, WeatherActivity.class);
        intent.putExtra(CITY_NAME_KEY, cityName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // инициализация компонентов интерфейса
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mWeatherLayout = findViewById(R.id.weather_layout);
        mWeatherMain = findViewById(R.id.weather_main);
        mTemperature = findViewById(R.id.temperature);
        mPressure = findViewById(R.id.pressure);
        mHumidity = findViewById(R.id.humidity);
        mWindSpeed = findViewById(R.id.wind_speed);
        mErrorLayout = findViewById(R.id.error_layout);

        // обнуление заголовка и замена его на название выбранного города
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        mCityName = getIntent().getStringExtra(CITY_NAME_KEY);
        mToolbarTitle.setText(mCityName);

        // инициализация диалогового окна загрузки
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        // загрузка информации о погоде
        loadWeather(false);
    }

    // загрузка информации о погоде
    private void loadWeather(boolean restart) {

        // закрытие интерфейса погоды
        mWeatherLayout.setVisibility(View.INVISIBLE);

        // закрытие сообщения об ошибке
        mErrorLayout.setVisibility(View.GONE);

        // открытие индикатора загрузки
        mLoadingView.showLoadingIndicator();

        // инициализация callback для загрузки информации о погоде
        LoaderManager.LoaderCallbacks<City> callbacks = new WeatherCallbacks();

        // рестарт загрузки
        // замена экземпляра LoaderCallbacks на новый и передача в его onLoadFInished загруженных данных
        if (restart) {
            getSupportLoaderManager().restartLoader(R.id.weather_loader_id, Bundle.EMPTY, callbacks);

        // инициализация загрузки
        } else {
            getSupportLoaderManager().initLoader(R.id.weather_loader_id, Bundle.EMPTY, callbacks);
        }
    }

    // вывод информации о погоде
    private void showWeather(@Nullable City city) {

        // проверка на ошибку в загруженной информации
        if (city == null
                || city.getMain() == null
                || city.getWeather() == null
                || city.getWind() == null) {
            showError();
            return;
        }

        // закрытие индикатора загрузки
        mLoadingView.hideLoadingIndicator();

        // открытие интерфейса погоды
        mWeatherLayout.setVisibility(View.VISIBLE);

        // закрытие сообщения об ошибке
        mErrorLayout.setVisibility(View.GONE);

        // вывод информации о погоде
        mToolbarTitle.setText(city.getName());
        mWeatherMain.setText(city.getWeather().getMain());
        mTemperature.setText(getString(R.string.f_temperature, city.getMain().getTemp()));
        mPressure.setText(getString(R.string.f_pressure, city.getMain().getPressure()));
        mHumidity.setText(getString(R.string.f_humidity, city.getMain().getHumidity()));
        mWindSpeed.setText(getString(R.string.f_wind_speed, city.getWind().getSpeed()));
    }

    // ошибка
    private void showError() {
        mLoadingView.hideLoadingIndicator();            // закртие индикации загрузки
        mWeatherLayout.setVisibility(View.INVISIBLE);   // закрытие интерфейса погоды
        mErrorLayout.setVisibility(View.VISIBLE);       // открытие сообщения об ошибке
    }

    // нажатие на сообщение об ошибке
    public void onErrorLayoutClick(View view) {
        loadWeather(true);
    }

    // класс LoaderCallbacks для погодного лоадера
    private class WeatherCallbacks implements LoaderManager.LoaderCallbacks<City> {

        // получение нужного лоадера в зависимости от id и параметров из bundle
        @NonNull
        @Override
        public Loader<City> onCreateLoader(int id, Bundle args) {
            return new RetrofitWeatherLoader(WeatherActivity.this, mCityName);
        }

        // завершение загрузки
        @Override
        public void onLoadFinished(@NonNull Loader<City> loader, City city) {
            showWeather(city);
        }

        // прерывание загрузки, очистка данных, связанных с лоадером
        @Override
        public void onLoaderReset(@NonNull Loader<City> loader) {
            // Do nothing
        }
    }
}