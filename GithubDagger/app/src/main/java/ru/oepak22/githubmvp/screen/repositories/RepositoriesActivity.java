package ru.oepak22.githubmvp.screen.repositories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.oepak22.githubmvp.AppDelegate;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.repository.GithubRepository;
import ru.oepak22.githubmvp.screen.commits.CommitsActivity;
import ru.oepak22.githubmvp.screen.general.LoadingDialog;
import ru.oepak22.githubmvp.screen.general.LoadingView;
import ru.oepak22.githubmvp.widget.BaseAdapter;
import ru.oepak22.githubmvp.widget.DividerItemDecoration;
import ru.oepak22.githubmvp.widget.EmptyRecyclerView;

// класс основного окна
public class RepositoriesActivity extends AppCompatActivity implements RepositoriesView,
        BaseAdapter.OnItemClickListener<Repository> {

    Toolbar mToolbar;
    EmptyRecyclerView mRecyclerView;
    View mEmptyView;

    private LoadingView mLoadingView;

    private RepositoriesAdapter mAdapter;

    private RepositoriesPresenter mPresenter;

    @Inject
    GithubRepository mRepository;

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, RepositoriesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty);

        setSupportActionBar(mToolbar);

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.setEmptyView(mEmptyView);

        mAdapter = new RepositoriesAdapter(new ArrayList<>());
        mAdapter.attachToRecyclerView(mRecyclerView);
        mAdapter.setOnItemClickListener(this);

        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this, getSupportLoaderManager());
        AppDelegate.getAppComponent().injectRepositoriesActivity(this);
        mPresenter = new RepositoriesPresenter(mRepository, lifecycleHandler, this);
        mPresenter.init();
    }

    @Override
    public void showLoading() {
        mLoadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        mLoadingView.hideLoading();
    }

    @Override
    public void onItemClick(@NonNull Repository item) {
        mPresenter.onItemClick(item);
    }

    @Override
    public void showRepositories(@NonNull List<Repository> repositories) {
        mAdapter.changeDataSet(repositories);
    }

    @Override
    public void showCommits(@NonNull Repository repository) {
        CommitsActivity.start(this, repository);
    }

    @Override
    public void showError() {
        mAdapter.clear();
    }
}