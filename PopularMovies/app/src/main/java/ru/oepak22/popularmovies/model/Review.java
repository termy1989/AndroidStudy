package ru.oepak22.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

// класс ревью к фильму
public class Review extends RealmObject implements Parcelable {

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

    public Review(Parcel in) {
        mId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
        mUpdate = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
        parcel.writeString(mUpdate);
    }
}
