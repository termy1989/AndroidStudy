package ru.oepak22.githubmvp;

import android.app.Application;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.oepak22.githubmvp.di.AppComponent;
import ru.oepak22.githubmvp.di.DaggerAppComponent;
import ru.oepak22.githubmvp.di.DataModule;


public class AppDelegate extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

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

        sAppComponent = DaggerAppComponent.builder()
                .dataModule(new DataModule())
                .build();
    }

    @NonNull
    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
}