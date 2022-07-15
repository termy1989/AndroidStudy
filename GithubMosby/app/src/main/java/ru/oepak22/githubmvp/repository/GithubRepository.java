package ru.oepak22.githubmvp.repository;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.oepak22.githubmvp.content.Authorization;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.content.Repository;
import rx.Observable;

// интерфейс для запросов данных
public interface GithubRepository {

    // авторизация
    @NonNull
    Observable<Authorization> authorize(@NonNull String login, @NonNull String password);

    // список репозиториев
    @NonNull
    Observable<List<Repository>> repositories();

    // список коммитов выбранного репозитория
    @NonNull
    Observable<List<CommitResponse>> commits(@NonNull String user, @NonNull String repo);
}
