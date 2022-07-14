package ru.oepak22.data.model.content;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

// realm-класс описания фильма
public class Review extends RealmObject {

    // идентификатор
    @PrimaryKey
    @SerializedName("id")
    private String mId;

    // автор
    @SerializedName("author")
    private String mAuthor;

    // содержимое
    @SerializedName("content")
    private String mContent;

    // дата обновления
    @SerializedName("updated_at")
    private String mUpdate;

    public Review() {}

    // GET и SET идентификатора
    @NonNull
    public String getId() {
        return mId;
    }
    public void setId(String id) {
        mId = id;
    }

    // GET и SET автора
    @NonNull
    public String getAuthor() {
        return mAuthor;
    }
    public void setAuthor(@NonNull String author) {
        mAuthor = author;
    }

    // GET и SET содержимого
    @NonNull
    public String getContent() {
        return mContent;
    }
    public void setContent(@NonNull String content) {
        mContent = content;
    }

    // GET и SET даты
    @NonNull
    public String getUpdateAt() {
        return mUpdate;
    }
    public void setUpdateAt(@NonNull String update) {
        mUpdate = update;
    }
}
