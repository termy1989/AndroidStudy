package ru.oepak22.githubmvp.screen.repositories;

import androidx.annotation.NonNull;

import java.util.List;

import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.screen.general.LoadingView;

public interface RepositoriesView extends LoadingView {

    void showRepositories(@NonNull List<Repository> repositories);
    void showCommits(@NonNull Repository repository);
    void showError();
}
