package ru.oepak22.popularmovies.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// класс списка фильмов
public class MoviesResponse {

    @SerializedName("results")
    private List<Movie> mMovies;

    @NonNull
    public List<Movie> getMovies() {
        if (mMovies == null) {
            return new ArrayList<>();
        }
        return mMovies;
    }

}
