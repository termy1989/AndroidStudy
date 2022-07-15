package ru.oepak22.githubmvp.screen.auth;

import ru.oepak22.githubmvp.screen.general.LoadingView;

// интерфейс управления окном авторизации
public interface AuthView extends LoadingView {

    void openRepositoriesScreen();      // открытие основного окна
    void showLoginError();              // ошибка в логине
    void showPasswordError();           // ошибка в пароле

}
