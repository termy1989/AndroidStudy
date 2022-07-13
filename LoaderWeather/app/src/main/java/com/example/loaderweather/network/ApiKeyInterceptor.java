package com.example.loaderweather.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

// класс перехватчика для изменения заголовка
public class ApiKeyInterceptor implements Interceptor {

    // добавление в заголовок ключа
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("appid", "bc0ffae33833bd4d0214451ff2c0d4be")
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
