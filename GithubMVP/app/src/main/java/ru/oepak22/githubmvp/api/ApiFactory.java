package ru.oepak22.githubmvp.api;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

// класс для формирования сетевого запроса
public final class ApiFactory {

    // клиент http
    private static OkHttpClient sClient;

    // экземпляр интерфейса запросов
    private static volatile GithubService sService;

    // конструктор
    private ApiFactory() {
    }

    // инициализация запроса
    @NonNull
    public static GithubService getGithubService() {
        GithubService service = sService;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = sService;
                if (service == null)
                    service = sService = buildRetrofit().create(GithubService.class);
            }
        }
        return service;
    }

    // инициализация api для запросов
    public static void recreate() {
        sClient = null;
        sClient = getClient();
        sService = buildRetrofit().create(GithubService.class);
    }

    // построение запроса через Retrofit
    @NonNull
    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    // инициализация http-клиента
    @NonNull
    private static OkHttpClient getClient() {
        OkHttpClient client = sClient;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = sClient;
                if (client == null) {
                    client = sClient = buildClient();
                }
            }
        }
        return client;
    }

    // построение http-клиента
    @NonNull
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor())
                .addInterceptor(ApiKeyInterceptor.create())
                .build();
    }

    // логгер для http-клиента
    public static HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v("NETWORK", message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
