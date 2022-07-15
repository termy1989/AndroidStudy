package ru.oepak22.githubmvp.test;

import androidx.annotation.NonNull;

import java.util.List;

import ru.oepak22.githubmvp.content.Authorization;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.repository.GithubRepository;
import rx.Observable;

// класс тестового репозитория
public class TestGithubRepository implements GithubRepository {

    @NonNull
    @Override
    public Observable<Authorization> authorize(@NonNull String login, @NonNull String password) {
        return Observable.empty();
    }

    @NonNull
    @Override
    public Observable<List<Repository>> repositories() {
        return Observable.empty();
    }

    @NonNull
    @Override
    public Observable<List<CommitResponse>> commits(@NonNull String user, @NonNull String repo) {
        return Observable.empty();
    }
}
