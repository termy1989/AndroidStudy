package ru.oepak22.githubmvp.test;

import androidx.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import rx.Observable;

public class MockLifecycleHandler implements LifecycleHandler {

    @NonNull
    @Override
    public <T> Observable.Transformer<T, T> load(int id) {
        return observable -> observable;
    }

    @NonNull
    @Override
    public <T> Observable.Transformer<T, T> reload(int id) {
        return observable -> observable;
    }

    @Override
    public void clear(int id) {
        // Do nothing
    }
}
