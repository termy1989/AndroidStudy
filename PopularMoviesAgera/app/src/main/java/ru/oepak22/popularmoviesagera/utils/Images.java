package ru.oepak22.popularmoviesagera.utils;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import ru.oepak22.popularmoviesagera.MoviesApp;
import ru.oepak22.popularmoviesagera.model.content.Movie;

public final class Images {

    public static final String WIDTH_185 = "w185";
    public static final String WIDTH_780 = "w780";

    private Images() {
    }

    public static void loadMovie(@NonNull ImageView imageView, @NonNull Movie movie,
                                 @NonNull String size) {
        loadMovie(imageView, movie.getPosterPath(), size);
    }

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
