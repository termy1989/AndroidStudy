package ru.oepak22.githubmvp.screen.commits;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Commit;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.repository.RepositoryProvider;
import ru.oepak22.githubmvp.utils.PreferenceUtils;

public class CommitsPresenter extends MvpBasePresenter<CommitsView> {

    private final LifecycleHandler mLifecycleHandler;

    public CommitsPresenter(@NonNull LifecycleHandler lifecycleHandler) {
        mLifecycleHandler = lifecycleHandler;
    }

    public void init(String repository) {

        if (!isViewAttached() || getView() == null) {
            return;
        }

        RepositoryProvider.provideGithubRepository()
                .commits(PreferenceUtils.getUserName(), repository)
                .doOnSubscribe(getView()::showLoading)
                .doOnTerminate(getView()::hideLoading)
                .compose(mLifecycleHandler.load(R.id.repositories_request))
                .subscribe(getView()::showCommits, throwable -> getView().showError());
    }

    public void onItemClick(@NonNull CommitResponse commit) {
        if (isViewAttached() && getView() != null) {
            getView().showSingleCommit(commit);
        }
    }
}
