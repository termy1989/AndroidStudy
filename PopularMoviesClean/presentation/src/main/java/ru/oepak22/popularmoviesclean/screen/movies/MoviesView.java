package ru.oepak22.popularmoviesclean.screen.movies;

import androidx.annotation.NonNull;

import java.util.List;

import ru.oepak22.domain.model.Movie;
import ru.oepak22.popularmoviesclean.screen.general.LoadingView;

// интерфейс вывода данных (списка фильмов)
public interface MoviesView extends LoadingView {
    void showMovies(@NonNull List<Movie> movies);
    void showError();
}
