package ru.oepak22.githubmvp.repository;

import androidx.annotation.NonNull;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.arturvasilov.rxloader.RxUtils;
import ru.oepak22.githubmvp.api.GithubService;
import ru.oepak22.githubmvp.content.Authorization;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.utils.AuthorizationUtils;
import rx.Observable;

// класс для запросов данных
public class DefaultGithubRepository implements GithubRepository {

    private final KeyValueStorage mKeyValueStorage;
    private final GithubService mGithubService;

    // конструктор
    public DefaultGithubRepository(@NonNull GithubService githubService, @NonNull KeyValueStorage keyValueStorage) {
        mKeyValueStorage = keyValueStorage;
        mGithubService = githubService;
    }

    // авторизация
    @NonNull
    @Override
    public Observable<Authorization> authorize(@NonNull String login, @NonNull String password) {
        String authorizationString = AuthorizationUtils.createAuthorizationString(login, password);
        return mGithubService
                .authorize(authorizationString)
                .flatMap(authorization -> {
                    //KeyValueStorage storage = mKeyValueStorage;//RepositoryProvider.provideKeyValueStorage();
                    mKeyValueStorage.saveToken(password);
                    mKeyValueStorage.saveUserName(authorization.getLogin());
                    //ApiFactory.recreate();
                    return Observable.just(authorization);
                })
                .compose(RxUtils.async());
    }

    // список репозиториев
    @NonNull
    @Override
    public Observable<List<Repository>> repositories() {
        return mGithubService
                .repositories()
                .flatMap(repositories -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Repository.class);
                        realm.insert(repositories);
                    });
                    return Observable.just(repositories);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Repository> repositories = realm.where(Repository.class).findAll();
                    return Observable.just(realm.copyFromRealm(repositories));
                })
                .compose(RxUtils.async());
    }

    // список коммитов у репозитория
    @NonNull
    @Override
    public Observable<List<CommitResponse>> commits(@NonNull String user, @NonNull String repo) {
        return mGithubService
                .commits(user, repo)
                .flatMap(commits -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        for (CommitResponse commit : commits) {
                            commit.setRepoName(repo);
                        }
                        realm.delete(CommitResponse.class);
                        realm.insert(commits);
                    });
                    return Observable.just(commits);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<CommitResponse> commitResponse = realm.where(CommitResponse.class).equalTo("mRepoName", repo).findAll();
                    return Observable.just(realm.copyFromRealm(commitResponse));
                })
                .compose(RxUtils.async());
    }
}
