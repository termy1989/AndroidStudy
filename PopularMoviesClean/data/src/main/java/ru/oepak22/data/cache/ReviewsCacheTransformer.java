package ru.oepak22.data.cache;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.oepak22.data.model.content.Review;
import rx.Observable;
import rx.functions.Func1;

// класс кэширования данных
public class ReviewsCacheTransformer implements Observable.Transformer<List<Review>, List<Review>> {

    // сохранение полученных данных
    private final Func1<List<Review>, Observable<List<Review>>> mSaveFunc = movies -> {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            realm.delete(Review.class);
            realm.insert(movies);
        });
        return Observable.just(movies);
    };

    // извлечение данных из кэша при ошибке
    private final Func1<Throwable, Observable<List<Review>>> mCacheErrorHandler = throwable -> {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Review> results = realm.where(Review.class).findAll();
        return Observable.just(realm.copyFromRealm(results));
    };

    // выполнение кэширования
    @Override
    public Observable<List<Review>> call(Observable<List<Review>> upstream) {
        return upstream
                .flatMap(mSaveFunc)
                .onErrorResumeNext(mCacheErrorHandler);
    }
}