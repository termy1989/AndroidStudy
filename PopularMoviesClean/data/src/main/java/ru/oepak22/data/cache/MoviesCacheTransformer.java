package ru.oepak22.data.cache;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.oepak22.data.model.content.Movie;
import rx.Observable;
import rx.functions.Func1;

// класс кэширования данных
public class MoviesCacheTransformer implements Observable.Transformer<List<Movie>, List<Movie>> {

    // сохранение полученных данных
    private final Func1<List<Movie>, Observable<List<Movie>>> mSaveFunc = movies -> {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            realm.delete(Movie.class);
            realm.insert(movies);
        });
        return Observable.just(movies);
    };

    // извлечение данных из кэша при ошибке
    private final Func1<Throwable, Observable<List<Movie>>> mCacheErrorHandler = throwable -> {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Movie> results = realm.where(Movie.class).findAll();
        return Observable.just(realm.copyFromRealm(results));
    };

    // выполнение кэширования
    @Override
    public Observable<List<Movie>> call(Observable<List<Movie>> upstream) {
        return upstream
                .flatMap(mSaveFunc)
                .onErrorResumeNext(mCacheErrorHandler);
    }
}