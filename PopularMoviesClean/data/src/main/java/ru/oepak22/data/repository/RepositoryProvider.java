package ru.oepak22.data.repository;

import ru.oepak22.domain.MoviesRepository;

// класс инициализации репозитория данных
public class RepositoryProvider {

    private static MoviesRepository sMoviesRepository;

    public static MoviesRepository getMoviesRepository() {
        if (sMoviesRepository == null) {
            sMoviesRepository = new MoviesDataRepository();
        }
        return sMoviesRepository;
    }

    public static void setMoviesRepository(MoviesRepository moviesRepository) {
        sMoviesRepository = moviesRepository;
    }
}
