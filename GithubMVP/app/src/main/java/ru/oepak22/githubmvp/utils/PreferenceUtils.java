package ru.oepak22.githubmvp.utils;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;

import rx.Observable;

// класс для сохранения параметров авторизации
public final class PreferenceUtils {

    private static final String TOKEN_KEY = "github_token";
    private static final String USER_NAME_KEY = "user_name";
    private static final String WALKTHROUGH_PASSED_KEY = "walkthrough_passed";

    // конструктор
    private PreferenceUtils() {
    }

    // имя ползователя
    @NonNull
    /*public static Observable<String> getUserName() { return Observable.just(Hawk.get(USER_NAME_KEY, "")); }*/
    public static String getUserName() { return Hawk.get(USER_NAME_KEY, ""); }
    public static void saveUserName(@NonNull String userName) { Hawk.put(USER_NAME_KEY, userName); }

    // токен (пароль)
    @NonNull
    public static String getToken() { return Hawk.get(TOKEN_KEY, ""); }
    public static void saveToken(@NonNull String token) { Hawk.put(TOKEN_KEY, token); }

    // проверка на первый запуск приложения
    public static void saveWalkthroughPassed() { Hawk.put(WALKTHROUGH_PASSED_KEY, true); }
    public static boolean isWalkthroughPassed() { return Hawk.get(WALKTHROUGH_PASSED_KEY, false); }
}
