package ru.oepak22.data.model.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.oepak22.data.model.content.Video;

// класс списка трейлеров
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
