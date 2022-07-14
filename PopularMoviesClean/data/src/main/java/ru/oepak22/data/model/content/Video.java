package ru.oepak22.data.model.content;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

// realm-класс трейлера
public class Video extends RealmObject {

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
}
