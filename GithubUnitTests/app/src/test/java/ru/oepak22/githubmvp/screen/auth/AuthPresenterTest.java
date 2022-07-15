package ru.oepak22.githubmvp.screen.auth;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;

import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.content.Authorization;
import ru.oepak22.githubmvp.repository.KeyValueStorage;
import ru.oepak22.githubmvp.repository.RepositoryProvider;
import ru.oepak22.githubmvp.test.MockLifecycleHandler;
import ru.oepak22.githubmvp.test.TestGithubRepository;
import ru.oepak22.githubmvp.test.TestKeyValueStorage;
import rx.Observable;

@RunWith(JUnit4.class)
public class AuthPresenterTest {

    private AuthPresenter mPresenter;
    private AuthView mAuthView;

    // инициализация объектов
    @Before
    public void setUp() throws Exception {

        LifecycleHandler lifecycleHandler = new MockLifecycleHandler();

        mAuthView = Mockito.mock(AuthView.class);
        mPresenter = new AuthPresenter(lifecycleHandler, mAuthView);
    }

    // проверка на то, что презентер корректно создается и инициализируется
    @Test
    public void testCreated() throws Exception {
        assertNotNull(mPresenter);
    }

    // проверка на открытие основного окна, авторизация прошла, и есть токен
    @Test
    public void testMainScreenOpened() throws Exception {

        KeyValueStorage storage = new TokenKeyValueStorage("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");
        RepositoryProvider.setKeyValueStorage(storage);

        mPresenter.init();

        Mockito.verify(mAuthView).openRepositoriesScreen();

        // проверка на то, что индикатор загрузки ни разу не высветился
        Mockito.verify(mAuthView, times(0)).showLoading();
    }

    // проверка на пустой токен (ничего не должно происходить)
    @Test
    public void testEmptyToken() throws Exception {
        KeyValueStorage storage = new TokenKeyValueStorage("");
        RepositoryProvider.setKeyValueStorage(storage);

        mPresenter.init();

        Mockito.verifyNoMoreInteractions(mAuthView);
    }

    // проверка на отсутствие каких-либо действий
    @Test
    public void testNoActionsWithView() throws Exception {
        Mockito.verifyNoMoreInteractions(mAuthView);
    }

    // проверка на срабатывание ошибки при пустом логине
    @Test
    public void testEmptyLogin() throws Exception {
        mPresenter.tryLogIn("", "password");
        Mockito.verify(mAuthView).showLoginError();
    }

    // проверка на срабатывание ошибки при пустом пароле
    @Test
    public void testEmptyPassword() throws Exception {
        mPresenter.tryLogIn("login", "");
        Mockito.verify(mAuthView).showPasswordError();
    }

    // проверка на срабатывание ошибки при пустых логине и пароле
    @Test
    public void testLoginAndPasswordEmpty() throws Exception {
        mPresenter.tryLogIn("", "");
        Mockito.verify(mAuthView).showLoginError();
    }

    // проверка на успешную авторизацию
    @Test
    public void testSuccessAuth() throws Exception {

        // инициализация тестового репозитория
        RepositoryProvider.setGithubRepository(new AuthTestRepository());

        // атворизация
        mPresenter.tryLogIn("termy1989", "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");

        // открытие основного окна
        Mockito.verify(mAuthView).openRepositoriesScreen();
    }

    // проверка на ошибку при авторизации
    @Test
    public void testErrorAuth() throws Exception {

        // инициализация тестового репозитория
        RepositoryProvider.setGithubRepository(new AuthTestRepository());

        // авторизация
        mPresenter.tryLogIn("bob", "123456");

        // ошибка
        Mockito.verify(mAuthView).showLoginError();
    }

    // проверка на неправильную, а затем правильную (со второго раза) авторизацию
    @Test
    public void testScreenScenario() throws Exception {

        // обнуление токена
        KeyValueStorage storage = new TokenKeyValueStorage("");
        RepositoryProvider.setKeyValueStorage(storage);

        // инициализация презентера
        mPresenter.init();

        // не вызван ни один метод
        Mockito.verifyNoMoreInteractions(mAuthView);

        // инициализация тестового репозитория
        RepositoryProvider.setGithubRepository(new AuthTestRepository());

        // неправильная авторизация
        mPresenter.tryLogIn("abc", "xzy");
        Mockito.verify(mAuthView).showLoading();                                                            // показ индикатора загрузки
        Mockito.verify(mAuthView).hideLoading();                                                            // закрытие индикатора загрузки
        Mockito.verify(mAuthView).showLoginError();                                                         // ошибка при авторизации

        // правильная авторизация
        mPresenter.tryLogIn("termy1989", "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");
        Mockito.verify(mAuthView, times(2)).showLoading();                            // показ индикатора загрузки 2 раза
        Mockito.verify(mAuthView, times(2)).hideLoading();                            // закрытие индикатора загрузки 2 раза
        Mockito.verify(mAuthView).openRepositoriesScreen();                                                 // открытие основного окна
    }

    // конец работы
    @SuppressWarnings("ConstantConditions")
    @After
    public void tearDown() throws Exception {
        RepositoryProvider.setKeyValueStorage(null);
        RepositoryProvider.setGithubRepository(null);
    }

    // класс тестового хранилища параметров авторизации, переопределение
    private class TokenKeyValueStorage extends TestKeyValueStorage {

        private final String mToken;

        public TokenKeyValueStorage(@NonNull String token) {
            mToken = token;
        }

        @NonNull
        @Override
        public String getToken() {
            return mToken;
        }
    }

    // класс тестового репозитория с переопределенной авторизацией
    private class AuthTestRepository extends TestGithubRepository {

        @NonNull
        @Override
        public Observable<Authorization> authorize(@NonNull String login, @NonNull String password) {
            if ("termy1989".equals(login) && "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".equals(password)) {
                return Observable.just(new Authorization());
            }
            return Observable.error(new IOException());
        }
    }
}
