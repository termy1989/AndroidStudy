package ru.oepak22.githubmvp.content;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Commit extends RealmObject {

    @SerializedName("author")
    private Author mAuthor;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("url")
    private String mUrl;

    @NonNull
    public Author getAuthor() { return mAuthor; }
    public void setAuthor(@NonNull Author author) { mAuthor = author; }

    @NonNull
    public String getMessage() { return mMessage; }
    public void setMessage(@NonNull String message) { mMessage = message; }

    @NonNull
    public String getUrl() { return mUrl; }
    public void setUrl(@NonNull String url) { mUrl = url; }
}