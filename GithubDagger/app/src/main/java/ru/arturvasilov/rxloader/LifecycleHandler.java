package ru.arturvasilov.rxloader;

/*import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;*/

import androidx.annotation.NonNull;

import rx.Observable;

/**
 * Interface for handling configuration changes and activity stopping
 * <p>
 * Contract of this interface:
 * <p>
 * 1) Each new subscription with load method should return the same result instantly
 * 2) #1 must be carried out event after configuration changes
 * 3) When Activity stopped request should be also stopped and restarted after activity is again visible
 * <p>
 * The only known implementation is {@link LoaderLifecycleHandler} which is base on loaders
 *
 * @author Artur Vasilov
 */
public interface LifecycleHandler {

    @NonNull
    <T> Observable.Transformer<T, T> load(int id);

    @NonNull
    <T> Observable.Transformer<T, T> reload(int id);

    void clear(int id);
}
