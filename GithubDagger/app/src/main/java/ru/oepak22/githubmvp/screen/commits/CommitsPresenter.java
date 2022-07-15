package ru.oepak22.githubmvp.screen.commits;

import androidx.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.repository.GithubRepository;
import ru.oepak22.githubmvp.repository.KeyValueStorage;

public class CommitsPresenter {

    private final LifecycleHandler mLifecycleHandler;
    private final CommitsView mView;
    private final GithubRepository mRepository;
    private final KeyValueStorage mStorage;

    public CommitsPresenter(@NonNull KeyValueStorage storage,
                            @NonNull GithubRepository githubRepository,
                            @NonNull LifecycleHandler lifecycleHandler,
                            @NonNull CommitsView view) {
        mStorage = storage;
        mRepository = githubRepository;
        mLifecycleHandler = lifecycleHandler;
        mView = view;
    }

    public void init(String repository) {
        mRepository.commits(mStorage.getUserName(), repository)
                .doOnSubscribe(mView::showLoading)
                .doOnTerminate(mView::hideLoading)
                .compose(mLifecycleHandler.load(R.id.repositories_request))
                .subscribe(mView::showCommits, throwable -> mView.showError());
    }

    public void onItemClick(@NonNull CommitResponse commit) {
        mView.showSingleCommit(commit);
    }
}
