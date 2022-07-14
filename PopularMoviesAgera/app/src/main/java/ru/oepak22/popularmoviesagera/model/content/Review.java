package ru.oepak22.popularmoviesagera.model.content;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    @NonNull
    public String getAuthor() {
        return mAuthor;
    }
    public void setAuthor(@NonNull String author) {
        mAuthor = author;
    }

    @NonNull
    public String getContent() {
        return mContent;
    }
    public void setContent(@NonNull String content) {
        mContent = content;
    }
}
