package ru.oepak22.githubmvp.content;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

// класс автора коммита
public class Author extends RealmObject {

    @SerializedName("name")
    private String mAuthorName;

    // SET и GET
    @NonNull
    public String getAuthorName() {
        return mAuthorName;
    }
    public void setAuthorName(@NonNull String authorName) {
        mAuthorName = authorName;
    }
}