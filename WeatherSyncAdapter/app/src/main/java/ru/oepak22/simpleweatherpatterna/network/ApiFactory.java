package ru.oepak22.simpleweatherpatterna.network;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// класс для формирования сетевого запроса
public class ApiFactory {

    private static OkHttpClient sClient;

    private static volatile WeatherService sService;

    // конструктор
    private ApiFactory() {}

    // получение сервиса для сетевого запроса
    @NonNull
    public static WeatherService getWeatherService() {

        WeatherService service = sService;

        if (service == null) {

            // доступ к ApiFactory синхронизирован
            synchronized (ApiFactory.class) {
                service = sService;
                if (service == null) {
                    service = sService = buildRetrofit().create(WeatherService.class);
                }
            }
        }
        return service;
    }

    // формирование запроса
    @NonNull
    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // получение http-клиента для запроса
    @NonNull
    private static OkHttpClient getClient() {

        OkHttpClient client = sClient;

        if (client == null) {

            // доступ к ApiFactory синхронизирован
            synchronized (ApiFactory.class) {
                client = sClient;
                if (client == null) {
                    client = sClient = buildClient();
                }
            }
        }
        return client;
    }

    // формирование http-клиента для запроса
    @NonNull
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .addInterceptor(new ApiKeyInterceptor())
                .build();
    }
}
