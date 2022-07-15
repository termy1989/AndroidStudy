package ru.oepak22.githubmvp.repository;

import androidx.annotation.NonNull;

public interface KeyValueStorage {

    String TOKEN_KEY = "github_token";
    String USER_NAME_KEY = "user_name";
    String WALKTHROUGH_PASSED_KEY = "walkthrough_passed";

    // имя ползователя
    @NonNull
    String getUserName();
    void saveUserName(@NonNull String userName);

    // токен (пароль)
    @NonNull
    String getToken();
    void saveToken(@NonNull String token);

    // проверка на первый запуск приложения
    void saveWalkthroughPassed();
    boolean isWalkthroughPassed();
}
