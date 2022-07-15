package ru.oepak22.githubmvp.api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import ru.oepak22.githubmvp.content.Authorization;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.content.Repository;
import rx.Observable;

// интерфейс для сетевых запросов к Github API
public interface GithubService {

    // авторизация
    @GET("/user")
    Observable<Authorization> authorize(@Header("Authorization") String authorization);

    // список репозиториев
    @GET("/user/repos")
    Observable<List<Repository>> repositories();

    // список коммитов выбранного репозитория
    @GET("/repos/{user}/{repo}/commits")
    Observable<List<CommitResponse>> commits(@Path("user") String user, @Path("repo") String repo);
}
