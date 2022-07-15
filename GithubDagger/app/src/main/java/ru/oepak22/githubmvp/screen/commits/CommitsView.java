package ru.oepak22.githubmvp.screen.commits;

import androidx.annotation.NonNull;

import java.util.List;

import ru.oepak22.githubmvp.content.Commit;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.screen.general.LoadingView;

public interface CommitsView extends LoadingView {
    void showCommits(@NonNull List<CommitResponse> commits);
    void showSingleCommit(@NonNull CommitResponse commit);
    void showError();
}
