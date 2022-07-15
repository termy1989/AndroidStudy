package ru.oepak22.githubmvp;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.oepak22.githubmvp.api.ApiFactory;
import ru.oepak22.githubmvp.repository.RepositoryProvider;


public class AppDelegate extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        // инициализация hawk для хранения параметров авторизации
        Hawk.init(this)
                /*.setEncryption(HawkBuilder.Encryption.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)*/
                .build();

        // инициализация realm для кэширования
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                //.name("myrealm.realm")
                //.rxFactory(new RealmObservableFactory(false))
                .build();
        Realm.setDefaultConfiguration(configuration);

        // инициализация api для запросов
        ApiFactory.recreate();

        // инициализация провайдера репозиториев
        RepositoryProvider.init();
    }

    @NonNull
    public static Context getContext() {
        return sContext;
    }
}