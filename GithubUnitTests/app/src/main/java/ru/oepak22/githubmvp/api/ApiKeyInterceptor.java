package ru.oepak22.githubmvp.api;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.oepak22.githubmvp.repository.RepositoryProvider;

// класс перехватчика для авторизации запросов
public final class ApiKeyInterceptor implements Interceptor {

    private final String mToken;

    // конструктор
    private ApiKeyInterceptor() {
        mToken = RepositoryProvider.provideKeyValueStorage().getToken();
    }

    // инициализатор
    @NonNull
    public static Interceptor create() {
        return new ApiKeyInterceptor();
    }

    // авторизация через токен
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (TextUtils.isEmpty(mToken)) {
            return chain.proceed(chain.request());
        }
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", String.format("%s %s", "token", mToken))
                .build();
        return chain.proceed(request);
    }
}
