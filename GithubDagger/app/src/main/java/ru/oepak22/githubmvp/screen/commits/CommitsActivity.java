package ru.oepak22.githubmvp.screen.commits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.oepak22.githubmvp.AppDelegate;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Commit;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.repository.GithubRepository;
import ru.oepak22.githubmvp.repository.KeyValueStorage;
import ru.oepak22.githubmvp.screen.general.LoadingDialog;
import ru.oepak22.githubmvp.screen.general.LoadingView;
import ru.oepak22.githubmvp.screen.repositories.RepositoriesAdapter;
import ru.oepak22.githubmvp.screen.repositories.RepositoriesPresenter;
import ru.oepak22.githubmvp.screen.repositories.RepositoriesView;
import ru.oepak22.githubmvp.widget.BaseAdapter;
import ru.oepak22.githubmvp.widget.DividerItemDecoration;
import ru.oepak22.githubmvp.widget.EmptyRecyclerView;

// класс окна со списком коммитов
public class CommitsActivity extends AppCompatActivity implements CommitsView,
        BaseAdapter.OnItemClickListener<CommitResponse>{

    private static final String REPO_NAME_KEY = "repo_name_key";

    Toolbar mToolbar;
    EmptyRecyclerView mRecyclerView;
    View mEmptyView;

    private LoadingView mLoadingView;

    private CommitsAdapter mAdapter;

    private CommitsPresenter mPresenter;

    @Inject
    GithubRepository mRepository;

    @Inject
    KeyValueStorage mKeyValueStorage;

    public static void start(@NonNull Activity activity, @NonNull Repository repository) {
        Intent intent = new Intent(activity, CommitsActivity.class);
        intent.putExtra(REPO_NAME_KEY, repository.getName());
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commits);

        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mEmptyView = findViewById(R.id.empty);

        setSupportActionBar(mToolbar);

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.setEmptyView(mEmptyView);

        mAdapter = new CommitsAdapter(new ArrayList<>());
        mAdapter.attachToRecyclerView(mRecyclerView);
        mAdapter.setOnItemClickListener(this);

        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this, getSupportLoaderManager());

        AppDelegate.getAppComponent().injectCommitsActivity(this);
        mPresenter = new CommitsPresenter(mKeyValueStorage, mRepository, lifecycleHandler, this);
        mPresenter.init(getIntent().getStringExtra(REPO_NAME_KEY));
    }

    @Override
    public void showLoading() { mLoadingView.showLoading(); }

    @Override
    public void hideLoading() { mLoadingView.hideLoading(); }

    @Override
    public void onItemClick(@NonNull CommitResponse item) { mPresenter.onItemClick(item); }

    @Override
    public void showCommits(@NonNull List<CommitResponse> commits) {
        mAdapter.changeDataSet(commits);
    }

    @Override
    public void showSingleCommit(@NonNull CommitResponse commit) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String str = "Repository: " + commit.getRepoName() + "\n"
                    + "Sha: " + commit.getSha() + "\n"
                    + "Url: " + commit.getCommit().getUrl() + "\n"
                    + "Author: " + commit.getCommit().getAuthor().getAuthorName() + "\n"
                    + "Message: " + commit.getCommit().getMessage() + "\n";

        // set title
        alertDialogBuilder.setTitle("Commit");

        // set dialog message
        alertDialogBuilder
                .setMessage(str)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> dialog.cancel());

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void showError() { mAdapter.clear(); }
}