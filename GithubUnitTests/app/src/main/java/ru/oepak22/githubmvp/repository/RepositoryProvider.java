package ru.oepak22.githubmvp.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

// класс провайдера репозиториев
public final class RepositoryProvider {

    private static GithubRepository sGithubRepository;
    private static KeyValueStorage sKeyValueStorage;

    // конструктор
    private RepositoryProvider() {}

    // инициализатор
    @MainThread
    public static void init() {
        sKeyValueStorage = new HawkKeyValueStorage();
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

    @NonNull
    public static KeyValueStorage provideKeyValueStorage() {
        if (sKeyValueStorage == null) {
            sKeyValueStorage = new HawkKeyValueStorage();
        }
        return sKeyValueStorage;
    }

    public static void setKeyValueStorage(@NonNull KeyValueStorage keyValueStorage) {
        sKeyValueStorage = keyValueStorage;
    }

    public static void setGithubRepository(@NonNull GithubRepository githubRepository) {
        sGithubRepository = githubRepository;
    }
}
