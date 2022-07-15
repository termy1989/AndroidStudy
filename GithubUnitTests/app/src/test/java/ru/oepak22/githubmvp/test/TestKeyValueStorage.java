package ru.oepak22.githubmvp.test;

import androidx.annotation.NonNull;

import ru.oepak22.githubmvp.repository.KeyValueStorage;

// класс тестового хранилища параметров авторизации
public class TestKeyValueStorage implements KeyValueStorage {

    @NonNull
    @Override
    public String getUserName() {
        return "";
    }

    @Override
    public void saveUserName(@NonNull String userName) {

    }

    @NonNull
    @Override
    public String getToken() {
        return "";
    }

    @Override
    public void saveToken(@NonNull String token) {

    }

    @Override
    public void saveWalkthroughPassed() {

    }

    @Override
    public boolean isWalkthroughPassed() {
        return false;
    }
}
