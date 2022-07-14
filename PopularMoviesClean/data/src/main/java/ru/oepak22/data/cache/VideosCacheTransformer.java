package ru.oepak22.data.cache;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.oepak22.data.model.content.Video;
import rx.Observable;
import rx.functions.Func1;

// класс кэширования данных
public class VideosCacheTransformer implements Observable.Transformer<List<Video>, List<Video>> {

    // сохранение полученных данных
    private final Func1<List<Video>, Observable<List<Video>>> mSaveFunc = movies -> {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            realm.delete(Video.class);
            realm.insert(movies);
        });
        return Observable.just(movies);
    };

    // извлечение данных из кэша при ошибке
    private final Func1<Throwable, Observable<List<Video>>> mCacheErrorHandler = throwable -> {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Video> results = realm.where(Video.class).findAll();
        return Observable.just(realm.copyFromRealm(results));
    };

    // выполнение кэширования
    @Override
    public Observable<List<Video>> call(Observable<List<Video>> upstream) {
        return upstream
                .flatMap(mSaveFunc)
                .onErrorResumeNext(mCacheErrorHandler);
    }
}