package ru.oepak22.githubmvp.utils;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

// класс создания заголовка авторизации
public final class AuthorizationUtils {

    private static final String BASIC_AUTHORIZATION = "Basic ";

    // конструктор
    private AuthorizationUtils() {
    }

    // формирование строки для простой авторизации
    @NonNull
    public static String createAuthorizationString(@NonNull String login, @NonNull String password) {
        String combinedStr = String.format("%1$s:%2$s", login, password);
        String authorizationString = BASIC_AUTHORIZATION + Base64.encodeToString(combinedStr.getBytes(), Base64.DEFAULT);

        return authorizationString.trim();
    }
}
