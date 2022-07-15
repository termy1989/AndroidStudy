package ru.oepak22.githubmvp.screen.repositories;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.repository.RepositoryProvider;

public class RepositoriesPresenter extends MvpBasePresenter<RepositoriesView> {

    private final LifecycleHandler mLifecycleHandler;

    public RepositoriesPresenter(@NonNull LifecycleHandler lifecycleHandler) {
        mLifecycleHandler = lifecycleHandler;
    }

    public void init() {

        if (!isViewAttached() || getView() == null) {
            return;
        }

        RepositoryProvider.provideGithubRepository()
                .repositories()
                .doOnSubscribe(getView()::showLoading)
                .doOnTerminate(getView()::hideLoading)
                .compose(mLifecycleHandler.load(R.id.repositories_request))
                .subscribe(getView()::showRepositories, throwable -> getView().showError());
    }

    public void onItemClick(@NonNull Repository repository) {
        if (isViewAttached() && getView() != null) {
            getView().showCommits(repository);
        }
    }
}
