package ru.oepak22.githubmvp.screen.commits;

import androidx.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Commit;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.repository.RepositoryProvider;
import ru.oepak22.githubmvp.utils.PreferenceUtils;

public class CommitsPresenter {

    private final LifecycleHandler mLifecycleHandler;
    private final CommitsView mView;

    public CommitsPresenter(@NonNull LifecycleHandler lifecycleHandler,
                                 @NonNull CommitsView view) {
        mLifecycleHandler = lifecycleHandler;
        mView = view;
    }

    public void init(String repository) {
        RepositoryProvider.provideGithubRepository()
                .commits(PreferenceUtils.getUserName(), repository)
                .doOnSubscribe(mView::showLoading)
                .doOnTerminate(mView::hideLoading)
                .compose(mLifecycleHandler.load(R.id.repositories_request))
                .subscribe(mView::showCommits, throwable -> mView.showError());
    }

    public void onItemClick(@NonNull CommitResponse commit) {
        mView.showSingleCommit(commit);
    }
}
