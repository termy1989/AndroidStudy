package ru.oepak22.domain.model;

import java.io.Serializable;

// класс фильма
public class Movie implements Serializable {

    private int mId;                // идентификатор
    private String mPosterPath;     // путь к изображению (постеру)
    private String mOverview;       // описание
    private String mTitle;          // заголовок (название)
    private String mReleasedDate;   // дата анонса
    private double mVoteAverage;    // рейтинг


    public Movie() {}

    // конструктор
    public Movie(int id, String posterPath, String overview, String title, String releasedDate, double voteAverage) {
        mId = id;
        mPosterPath = posterPath;
        mOverview = overview;
        mTitle = title;
        mReleasedDate = releasedDate;
        mVoteAverage = voteAverage;
    }

    // GET и SET идентификатора
    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    // GET и SET пути к изображению
    public String getPosterPath() {
        return mPosterPath;
    }
    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    // GET и SET описания
    public String getOverview() {
        return mOverview;
    }
    public void setOverview(String overview) {
        mOverview = overview;
    }

    // GET и SET названия
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    // GET и SET даты
    public String getReleasedDate() {
        return mReleasedDate;
    }
    public void setReleasedDate(String releasedDate) {
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
