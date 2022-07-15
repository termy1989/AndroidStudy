package ru.oepak22.githubmvp.screen.auth;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.Objects;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.repository.RepositoryProvider;
import ru.oepak22.githubmvp.utils.PreferenceUtils;

// класс презентера авторизации
public class AuthPresenter extends MvpBasePresenter<AuthView> {

    private final LifecycleHandler mLifecycleHandler;       // экземпляр управления жизненным циклом

    // конструктор
    public AuthPresenter(@NonNull LifecycleHandler lifecycleHandler) {
        mLifecycleHandler = lifecycleHandler;
    }

    // инициализатор
    public void init() {

        // открыте списка репозиториев, если авторизация была пройдена, и токен имеется
        String token = PreferenceUtils.getToken();
        if (!TextUtils.isEmpty(token) && isViewAttached() && getView() != null) {
            getView().openRepositoriesScreen();
        }
    }

    // функция авторизации
    public void tryLogIn(@NonNull String login, @NonNull String password) {

        if (!isViewAttached() || getView() == null) {
            return;
        }

        // проверка на пустоту полей ввода
        if (TextUtils.isEmpty(login)) {
            getView().showLoginError();
        } else if (TextUtils.isEmpty(password)) {
            getView().showPasswordError();
        }

        // выполнение авторизации
        else {
            RepositoryProvider.provideGithubRepository()                                // обращение к провайдеру репозиториев
                    .authorize(login, password)                                         // запрос авторизации
                    .doOnSubscribe(getView()::showLoading)                              // в течение процесса - показ индикатора загрузки
                    .doOnTerminate(getView()::hideLoading)                              // по окончании процесса - скрытие индикатора загрузки
                    .compose(mLifecycleHandler.reload(R.id.auth_request))
                    .subscribe(authorization -> getView().openRepositoriesScreen(),     // успешная авторизация - открытие основного окна
                                throwable -> getView().showLoginError());               // ошибка
        }
    }
}
