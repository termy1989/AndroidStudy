package ru.oepak22.githubmvp.content;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CommitResponse extends RealmObject {

    private String mRepoName;

    @SerializedName("sha")
    private String mSha;

    @SerializedName("commit")
    private Commit mCommit;

    public CommitResponse() {}

    public CommitResponse(@NonNull String repoName, @NonNull String sha, Commit commit) {
        mRepoName = repoName;
        mSha = sha;
        mCommit = commit;
    }

    @NonNull
    public String getRepoName() { return mRepoName; }
    public void setRepoName(@NonNull String repoName) { mRepoName = repoName; }

    @NonNull
    public String getSha() { return mSha; }
    public void setmSha(@NonNull String sha) { mSha = sha; }

    @NonNull
    public Commit getCommit() {
        return mCommit;
    }
    public void setmCommit(@NonNull Commit commit) { mCommit = commit; }
}
