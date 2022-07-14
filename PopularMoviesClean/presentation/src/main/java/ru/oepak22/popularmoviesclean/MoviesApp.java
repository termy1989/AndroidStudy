package ru.oepak22.popularmoviesclean;

import android.app.Application;

import androidx.annotation.NonNull;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MoviesApp extends Application {

    private static MoviesApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // инициализация Picasso
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(this))
                .build();
        Picasso.setSingletonInstance(picasso);

        // инициализация Realm
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                //.name("myrealm.realm")
                //.rxFactory(new RealmObservableFactory(false))
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    @NonNull
    public static MoviesApp getAppContext() {
        return sInstance;
    }
}
