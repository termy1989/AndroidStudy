package ru.oepak22.popularmoviesagera;

import android.app.Application;

import androidx.annotation.NonNull;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class MoviesApp extends Application {

    private static MoviesApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(this))
                .build();
        Picasso.setSingletonInstance(picasso);
    }

    @NonNull
    public static MoviesApp getAppContext() {
        return sInstance;
    }
}
