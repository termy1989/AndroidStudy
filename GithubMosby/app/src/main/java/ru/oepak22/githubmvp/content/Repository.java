package ru.oepak22.githubmvp.content;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Repository extends RealmObject {

    @PrimaryKey
    @SerializedName("name")
    private String mName;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("language")
    private String mLanguage;

    @SerializedName("stargazers_count")
    private int mStarsCount;

    @SerializedName("forks_count")
    private int mForksCount;

    @SerializedName("watchers_count")
    private int mWatchersCount;

    @SerializedName("private")
    private boolean mPrivate;

    public Repository() {}

    public Repository(@NonNull String name, boolean priv, @NonNull String description, @NonNull String language,
                      int starsCount, int forksCount, int watchersCount) {
        mName = name;
        mPrivate = priv;
        mDescription = description;
        mLanguage = language;
        mStarsCount = starsCount;
        mForksCount = forksCount;
        mWatchersCount = watchersCount;
    }

    @NonNull
    public String getName() { return mName; }
    public void setName(String name) { mName = name == null ? "" : name; }

    @NonNull
    public String getDescription() { return mDescription; }
    public void setDescription(String description) { mDescription = description == null ? "" : description; }

    @NonNull
    public String getLanguage() { return mLanguage; }
    public void setLanguage(String language) { mLanguage = language == null ? "" : language; }

    public int getStarsCount() { return mStarsCount; }
    public void setStarsCount(int starsCount) { mStarsCount = starsCount < 0 ? 0 : starsCount; }

    public int getForksCount() { return mForksCount; }
    public void setForksCount(int forksCount) { mForksCount = forksCount < 0 ? 0 : forksCount; }

    public int getWatchersCount() { return mWatchersCount; }
    public void setWatchersCount(int watchersCount) { mWatchersCount = watchersCount < 0 ? 0 : watchersCount; }

    public String getPrivate() {
        if (mPrivate) return "private";
        else return "public";
    }
    public void setmPrivate(boolean mPrivate) { this.mPrivate = mPrivate; }
}
