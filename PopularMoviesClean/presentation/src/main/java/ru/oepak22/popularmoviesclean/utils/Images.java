package ru.oepak22.popularmoviesclean.utils;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import ru.oepak22.domain.model.Movie;
import ru.oepak22.popularmoviesclean.MoviesApp;

// класс загрузки изображения
public final class Images {

    // варианты размеров изображений
    public static final String WIDTH_185 = "w185";
    public static final String WIDTH_780 = "w780";

    // конструктор
    private Images() {}

    // загрузка изображения
    public static void loadMovie(@NonNull ImageView imageView, @NonNull Movie movie,
                                 @NonNull String size) {
        loadMovie(imageView, movie.getPosterPath(), size);
    }

    // инициализация Picasso
    public static void loadMovie(@NonNull ImageView imageView, @NonNull String posterPath,
                                 @NonNull String size) {
        String url = "http://image.tmdb.org/t/p/" + size + posterPath;
        Picasso.with(imageView.getContext())
                .load(url)
                .noFade()
                .into(imageView);
    }

    public static void fetch(@NonNull String posterPath, @NonNull String size) {
        String url = "http://image.tmdb.org/t/p/" + size + posterPath;
        Picasso.with(MoviesApp.getAppContext())
                .load(url)
                .fetch();
    }
}
