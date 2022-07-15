package ru.oepak22.githubmvp.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

// класс провайдера репозиториев
public final class RepositoryProvider {

    // экземпляр
    private static GithubRepository sGithubRepository;

    // конструктор
    private RepositoryProvider() {
    }

    // инициализатор
    @MainThread
    public static void init() {
        sGithubRepository = new DefaultGithubRepository();
    }

    // проверка на то, что репозиторий проинициализирован
    @NonNull
    public static GithubRepository provideGithubRepository() {
        if (sGithubRepository == null) {
            sGithubRepository = new DefaultGithubRepository();
        }
        return sGithubRepository;
    }

    public static void setGithubRepository(@NonNull GithubRepository githubRepository) {
        sGithubRepository = githubRepository;
    }
}
