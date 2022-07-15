package ru.oepak22.githubmvp.screen.commits;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Commit;
import ru.oepak22.githubmvp.content.CommitResponse;

public class CommitHolder extends RecyclerView.ViewHolder {

    TextView mSha;
    TextView mUrl;

    public CommitHolder(View itemView) {
        super(itemView);

        mSha = itemView.findViewById(R.id.commitSha);
        mUrl = itemView.findViewById(R.id.commitUrl);
    }

    public void bind(@NonNull CommitResponse commit) {
        mSha.setText(commit.getSha());
        mUrl.setText(commit.getCommit().getUrl());
    }
}