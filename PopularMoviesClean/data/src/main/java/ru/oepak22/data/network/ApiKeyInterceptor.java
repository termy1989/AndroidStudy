package ru.oepak22.data.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("api_key", "60604afe96d7fe2d9e5070d8bd541fd4")
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
