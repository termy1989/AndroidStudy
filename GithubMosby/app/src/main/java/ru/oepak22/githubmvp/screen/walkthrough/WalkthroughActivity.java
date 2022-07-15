package ru.oepak22.githubmvp.screen.walkthrough;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Benefit;
import ru.oepak22.githubmvp.screen.auth.AuthActivity;
import ru.oepak22.githubmvp.utils.PreferenceUtils;
import ru.oepak22.githubmvp.widget.PageChangeViewPager;

public class WalkthroughActivity extends AppCompatActivity implements
        PageChangeViewPager.PagerStateListener {

    private static final int PAGES_COUNT = 3;

    PageChangeViewPager mPager;
    Button mActionButton;

    private int mCurrentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        mPager = findViewById(R.id.pager);
        mActionButton = findViewById(R.id.btn_walkthrough);

        mPager.setAdapter(new WalkthroughAdapter(getFragmentManager(), getBenefits()));
        mPager.setOnPageChangedListener(this);

        mActionButton.setText(R.string.next_uppercase);

        if (PreferenceUtils.isWalkthroughPassed()) {
            startAuthActivity();
        }

        /**
         * TODO : task
         *
         * Refactor this screen using MVP pattern
         *
         * Hint: there are no requests on this screen, so it's good place to start
         *
         * You can simply go through each line of code and decide if it should be in View or in Presenter
         */
    }

    @Override
    public void onPageChanged(int selectedPage, boolean fromUser) {
        if (fromUser) {
            mCurrentItem = selectedPage;
            showBenefit(mCurrentItem, isLastBenefit());
        }
    }

    private boolean isLastBenefit() {
        return mCurrentItem == PAGES_COUNT - 1;
    }

    private void showBenefit(int index, boolean isLastBenefit) {
        mActionButton.setText(isLastBenefit ? R.string.finish_uppercase : R.string.next_uppercase);
        if (index == mPager.getCurrentItem()) {
            return;
        }
        mPager.smoothScrollNext(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void startAuthActivity() {
        AuthActivity.start(this);
        finish();
    }

    @NonNull
    private List<Benefit> getBenefits() {
        return new ArrayList<Benefit>() {
            {
                add(Benefit.WORK_TOGETHER);
                add(Benefit.CODE_HISTORY);
                add(Benefit.PUBLISH_SOURCE);
            }
        };
    }

    public void onActionButtonClick(View view) {
        if (isLastBenefit()) {
            PreferenceUtils.saveWalkthroughPassed();
            startAuthActivity();
        } else {
            mCurrentItem++;
            showBenefit(mCurrentItem, isLastBenefit());
        }
    }
}