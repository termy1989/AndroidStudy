package ru.oepak22.popularmoviesagera.model.content;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("key")
    private String mKey;

    @SerializedName("name")
    private String mName;

    @NonNull
    public String getKey() {
        return mKey;
    }
    public void setKey(@NonNull String key) {
        mKey = key;
    }

    @NonNull
    public String getName() {
        return mName;
    }
    public void setName(@NonNull String name) {
        mName = name;
    }
}
