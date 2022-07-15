package ru.oepak22.githubmvp.screen.auth;

import ru.oepak22.githubmvp.repository.GithubRepository;
import ru.oepak22.githubmvp.repository.KeyValueStorage;
import ru.oepak22.githubmvp.utils.TextUtils;

import androidx.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.R;

// класс презентера авторизации
public class AuthPresenter {

    private final LifecycleHandler mLifecycleHandler;       // экземпляр управления жизненным циклом
    private final AuthView mAuthView;                       // экземпляр интерфейса управления окном авторизации
    private final GithubRepository mRepository;
    private final KeyValueStorage mKeyValueStorage;

    // конструктор
    public AuthPresenter(@NonNull GithubRepository repository, @NonNull KeyValueStorage keyValueStorage,
                         @NonNull LifecycleHandler lifecycleHandler, @NonNull AuthView authView) {
        mRepository = repository;
        mKeyValueStorage = keyValueStorage;
        mLifecycleHandler = lifecycleHandler;
        mAuthView = authView;
    }

    // инициализатор
    public void init() {

        // открытие списка репозиториев, если авторизация была пройдена, и токен имеется
        String token = mKeyValueStorage.getToken();
        if (!TextUtils.isEmpty(token)) {
            mAuthView.openRepositoriesScreen();
        }
    }

    // функция авторизации
    public void tryLogIn(@NonNull String login, @NonNull String password) {

        // проверка на пустоту полей ввода
        if (TextUtils.isEmpty(login)) {
            mAuthView.showLoginError();
        } else if (TextUtils.isEmpty(password)) {
            mAuthView.showPasswordError();
        }

        // выполнение авторизации
        else {
            mRepository.authorize(login, password)                                      // запрос авторизации
                    .doOnSubscribe(mAuthView::showLoading)                              // в течение процесса - показ индикатора загрузки
                    .doOnTerminate(mAuthView::hideLoading)                              // по окончании процесса - скрытие индикатора загрузки
                    .compose(mLifecycleHandler.reload(R.id.auth_request))
                    .subscribe(authorization -> mAuthView.openRepositoriesScreen(),     // успешная авторизация - открытие основного окна
                                    throwable -> mAuthView.showLoginError());           // ошибка
        }
    }
}
