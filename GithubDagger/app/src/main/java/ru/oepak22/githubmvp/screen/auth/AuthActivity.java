package ru.oepak22.githubmvp.screen.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.oepak22.githubmvp.AppDelegate;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.repository.GithubRepository;
import ru.oepak22.githubmvp.repository.KeyValueStorage;
import ru.oepak22.githubmvp.screen.general.LoadingDialog;
import ru.oepak22.githubmvp.screen.general.LoadingView;
import ru.oepak22.githubmvp.screen.repositories.RepositoriesActivity;

// класс окна авторизации
public class AuthActivity extends AppCompatActivity implements AuthView {

    // компоненты окна
    Toolbar mToolbar;
    EditText mLoginEdit;
    EditText mPasswordEdit;
    TextInputLayout mLoginInputLayout;
    TextInputLayout mPasswordInputLayout;

    // экземпляр интрефейса прогрузки
    private LoadingView mLoadingView;

    // презентер авторизации
    private AuthPresenter mPresenter;

    @Inject
    GithubRepository mRepository;

    @Inject
    KeyValueStorage mKeyValueStorage;

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // инициализация компонентов окна
        mToolbar = findViewById(R.id.toolbar);
        mLoginEdit = findViewById(R.id.loginEdit);
        mPasswordEdit = findViewById(R.id.passwordEdit);
        mLoginInputLayout = findViewById(R.id.loginInputLayout);
        mPasswordInputLayout = findViewById(R.id.passwordInputLayout);
        setSupportActionBar(mToolbar);

        // инициализация индикатора загрузчика
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this, getSupportLoaderManager());

        // инициализация презентера авторизации
        AppDelegate.getAppComponent().injectAuthActivity(this);
        mPresenter = new AuthPresenter(mRepository, mKeyValueStorage, lifecycleHandler, this);
        mPresenter.init();
    }

    // кнопка авторизации
    public void onLogInButtonClick(View view) {
        String login = mLoginEdit.getText().toString();
        String password = mPasswordEdit.getText().toString();

        // авторизация через презентер
        mPresenter.tryLogIn(login, password);
    }

    // открытие индикатора загрузки
    @Override
    public void showLoading() {
        mLoadingView.showLoading();
    }

    // закрытие индикатора загрузки
    @Override
    public void hideLoading() {
        mLoadingView.hideLoading();
    }

    // уведомление о неправильном логине
    @Override
    public void showLoginError() {
        mLoginInputLayout.setError(getString(R.string.error));
    }

    // уведомление о неправильном пароле
    @Override
    public void showPasswordError() {
        mPasswordInputLayout.setError(getString(R.string.error));
    }

    // успешная авторизация - открытие основного окна
    @Override
    public void openRepositoriesScreen() {
        RepositoriesActivity.start(this);
        finish();
    }
}