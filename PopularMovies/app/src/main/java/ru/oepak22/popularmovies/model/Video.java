package ru.oepak22.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

// класс видео
public class Video extends RealmObject implements Parcelable {

    // ключ
    @SerializedName("key")
    private String mKey;

    // название
    @SerializedName("name")
    private String mName;

    public Video() {}

    public Video(Parcel in) {
        mKey = in.readString();
        mName = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    // GET и SET ключа
    @NonNull
    public String getKey() {
        return mKey;
    }
    public void setKey(@NonNull String key) {
        mKey = key;
    }

    // GET и SET названия
    @NonNull
    public String getName() {
        return mName;
    }
    public void setName(@NonNull String name) { mName = name; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mKey);
        parcel.writeString(mName);
    }
}
