package ru.oepak22.githubmvp.screen.walkthrough;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Benefit;
import ru.oepak22.githubmvp.repository.KeyValueStorage;

public class WalkthroughPresenter {

    private static final int PAGES_COUNT = 3;

    private final KeyValueStorage mKeyValueStorage;
    private final WalkthroughView mWalkthroughView;

    private int mCurrentItem = 0;

    public WalkthroughPresenter(@NonNull KeyValueStorage keyValueStorage, @NonNull WalkthroughView walkthroughView) {
        mKeyValueStorage = keyValueStorage;
        mWalkthroughView = walkthroughView;
    }

    public void init() {
        if (mKeyValueStorage.isWalkthroughPassed()) {
            mWalkthroughView.startAuth();
        } else {
            mWalkthroughView.showBenefits(getBenefits());
            mWalkthroughView.showActionButtonText(R.string.next_uppercase);
        }
    }

    public void onActionButtonClick() {
        if (isLastBenefit()) {
            mKeyValueStorage.saveWalkthroughPassed();
            mWalkthroughView.startAuth();
        } else {
            mCurrentItem++;
            showBenefitText();
            mWalkthroughView.scrollToNextBenefit();
        }
    }

    public void onPageChanged(int selectedPage, boolean fromUser) {
        if (fromUser) {
            mCurrentItem = selectedPage;
            showBenefitText();
        }
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

    private boolean isLastBenefit() {
        return mCurrentItem == PAGES_COUNT - 1;
    }

    private void showBenefitText() {
        @StringRes int buttonTextId = isLastBenefit() ? R.string.finish_uppercase : R.string.next_uppercase;
        mWalkthroughView.showActionButtonText(buttonTextId);
    }
}
