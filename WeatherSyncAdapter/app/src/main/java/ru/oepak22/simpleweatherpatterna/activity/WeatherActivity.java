package ru.oepak22.simpleweatherpatterna.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import ru.oepak22.simpleweatherpatterna.R;
import ru.oepak22.simpleweatherpatterna.model.City;


// класс активности с информацией о погоде
public class WeatherActivity extends AppCompatActivity {

    private static final String cityName = "cityName";
    private static final String cityWeather = "cityWeather";
    private static final String cityTemp = "cityTemp";
    private static final String cityPressure = "cityPressure";
    private static final String cityHumidity = "cityHumidity";
    private static final String citySpeed = "citySpeed";

    Toolbar mToolbar;
    TextView mToolbarTitle;
    View mWeatherLayout;
    TextView mWeatherMain;
    TextView mTemperature;
    TextView mPressure;
    TextView mHumidity;
    TextView mWindSpeed;
    TextView mErrorLayout;

    //@NonNull
    public static Intent makeIntent(@NonNull Activity activity, @NonNull City city) {

        Intent intent = new Intent(activity, WeatherActivity.class);

        intent.putExtra(cityName, city.getName());
        intent.putExtra(cityWeather, city.getWeather().getMain());
        intent.putExtra(cityTemp, city.getMain().getTemp());
        intent.putExtra(cityPressure, city.getMain().getPressure());
        intent.putExtra(cityHumidity, city.getMain().getHumidity());
        intent.putExtra(citySpeed, city.getWind().getSpeed());

        return intent;
    }

    // создание активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // инициализация компонентов активности
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mWeatherLayout = findViewById(R.id.weather_layout);
        mWeatherMain = findViewById(R.id.weather_main);
        mTemperature = findViewById(R.id.temperature);
        mPressure = findViewById(R.id.pressure);
        mHumidity = findViewById(R.id.humidity);
        mWindSpeed = findViewById(R.id.wind_speed);
        mErrorLayout = findViewById(R.id.error_layout);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        String mCityName = getIntent().getStringExtra(cityName);
        mToolbarTitle.setText(mCityName);

        showWeather();
    }

    private void showWeather() {

        mWeatherLayout.setVisibility(View.VISIBLE);

        mToolbarTitle.setText(getIntent().getStringExtra(cityName));
        mWeatherMain.setText(getIntent().getStringExtra(cityWeather));
        mTemperature.setText(getString(R.string.f_temperature, getIntent().getIntExtra(cityTemp, 0)));
        mPressure.setText(getString(R.string.f_pressure, getIntent().getIntExtra(cityPressure, 0)));
        mHumidity.setText(getString(R.string.f_humidity, getIntent().getIntExtra(cityHumidity, 0)));
        mWindSpeed.setText(getString(R.string.f_wind_speed, getIntent().getIntExtra(citySpeed, 0)));
    }
}