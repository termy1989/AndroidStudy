package ru.oepak22.data.model.content;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

// realm-класс фильма
public class Movie extends RealmObject {

    // идентификатор
    @PrimaryKey
    @SerializedName("id")
    private int mId;

    // путь к изображению (постеру)
    @SerializedName("poster_path")
    private String mPosterPath;

    // описание
    @SerializedName("overview")
    private String mOverview;

    // заголовок (название)
    @SerializedName("original_title")
    private String mTitle;

    // дата анонса
    @SerializedName("release_date")
    private String mReleasedDate;

    // рейтинг
    @SerializedName("vote_average")
    private double mVoteAverage;

    public Movie() {
    }

    // стандартный конструктор
    public Movie(int id, @NonNull String posterPath, @NonNull String overview,
                 @NonNull String title, @NonNull String releasedDate, double voteAverage) {
        mId = id;
        mPosterPath = posterPath;
        mOverview = overview;
        mTitle = title;
        mReleasedDate = releasedDate;
        mVoteAverage = voteAverage;
    }

    // конструктор Parceable
    public Movie(Parcel in) {
        mId = in.readInt();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mTitle = in.readString();
        mReleasedDate = in.readString();
        mVoteAverage = in.readDouble();
    }

    // GET и SET идентификатора
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    // GET и SET пути к изображению
    @NonNull
    public String getPosterPath() {
        return mPosterPath;
    }
    public void setPosterPath(@NonNull String posterPath) {
        mPosterPath = posterPath;
    }

    // GET и SET описания
    @NonNull
    public String getOverview() {
        return mOverview;
    }
    public void setOverview(@NonNull String overview) {
        mOverview = overview;
    }

    // GET и SET названия
    @NonNull
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    // GET и SET даты
    @NonNull
    public String getReleasedDate() {
        return mReleasedDate;
    }
    public void setReleasedDate(@NonNull String releasedDate) {
        mReleasedDate = releasedDate;
    }

    // GET и SET рейтинга
    public double getVoteAverage() {
        return mVoteAverage;
    }
    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }
}
