package ru.oepak22.githubmvp.screen.repositories;

import androidx.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.repository.GithubRepository;

public class RepositoriesPresenter {

    private final LifecycleHandler mLifecycleHandler;
    private final RepositoriesView mView;
    private final GithubRepository mRepository;

    public RepositoriesPresenter(@NonNull GithubRepository repository,
                                 @NonNull LifecycleHandler lifecycleHandler,
                                 @NonNull RepositoriesView view) {
        mLifecycleHandler = lifecycleHandler;
        mView = view;
        mRepository = repository;
    }

    public void init() {
        mRepository.repositories()
                .doOnSubscribe(mView::showLoading)
                .doOnTerminate(mView::hideLoading)
                .compose(mLifecycleHandler.load(R.id.repositories_request))
                .subscribe(mView::showRepositories, throwable -> mView.showError());
    }

    public void onItemClick(@NonNull Repository repository) {
        mView.showCommits(repository);
    }
}
