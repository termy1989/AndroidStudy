package ru.oepak22.popularmovies.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// класс списка видео
public class VideosResponse {

    @SerializedName("results")
    private List<Video> mVideos;

    @NonNull
    public List<Video> getVideos() {
        if (mVideos == null) {
            return new ArrayList<>();
        }
        return mVideos;
    }
}
