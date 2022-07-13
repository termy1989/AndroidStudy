package com.example.loaderweather.view;

// интерфейс для обозначения загрузки информации
public interface LoadingView {

    void showLoadingIndicator();    // показ индикатора
    void hideLoadingIndicator();    // закрытие индикатора
}
