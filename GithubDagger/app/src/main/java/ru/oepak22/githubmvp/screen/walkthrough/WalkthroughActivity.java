package ru.oepak22.githubmvp.screen.walkthrough;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.oepak22.githubmvp.AppDelegate;
import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Benefit;
import ru.oepak22.githubmvp.repository.KeyValueStorage;
import ru.oepak22.githubmvp.screen.auth.AuthActivity;
import ru.oepak22.githubmvp.widget.PageChangeViewPager;

public class WalkthroughActivity extends AppCompatActivity implements
        PageChangeViewPager.PagerStateListener, WalkthroughView {

    PageChangeViewPager mPager;
    Button mActionButton;

    private WalkthroughPresenter mPresenter;

    @Inject
    KeyValueStorage mKeyValueStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        mPager = findViewById(R.id.pager);
        mActionButton = findViewById(R.id.btn_walkthrough);

        mPager.setOnPageChangedListener(this);

        AppDelegate.getAppComponent().injectWalkthroughActivity(this);
        mPresenter = new WalkthroughPresenter(mKeyValueStorage,this);
        mPresenter.init();
    }

    @Override
    public void showBenefits(@NonNull List<Benefit> benefits) {
        mPager.setAdapter(new WalkthroughAdapter(getFragmentManager(), benefits));
    }

    @Override
    public void showActionButtonText(@StringRes int buttonTextId) {
        mActionButton.setText(buttonTextId);
    }

    @Override
    public void scrollToNextBenefit() {
        mPager.smoothScrollNext(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    @Override
    public void startAuth() {
        AuthActivity.start(this);
        finish();
    }

    public void onActionButtonClick(View view) {
        mPresenter.onActionButtonClick();
    }

    @Override
    public void onPageChanged(int selectedPage, boolean fromUser) {
        mPresenter.onPageChanged(selectedPage, fromUser);
    }
}