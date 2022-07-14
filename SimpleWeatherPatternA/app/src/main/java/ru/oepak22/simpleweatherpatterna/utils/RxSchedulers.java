package ru.oepak22.simpleweatherpatterna.utils;

import androidx.annotation.NonNull;

import io.reactivex.ObservableTransformer;
//import rx.Observable;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
//import io.reactivex.schedulers.AndroidSchedulers;

//import rx.Observable;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;

public final class RxSchedulers {

    private RxSchedulers() {
    }

    @NonNull
    public static <T> ObservableTransformer<T, T> async() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
