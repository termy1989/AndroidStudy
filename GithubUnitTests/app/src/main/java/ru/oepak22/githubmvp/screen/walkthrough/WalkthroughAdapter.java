package ru.oepak22.githubmvp.screen.walkthrough;

import android.app.Fragment;
import android.app.FragmentManager;

import androidx.annotation.NonNull;
import androidx.legacy.app.FragmentStatePagerAdapter;

import java.util.List;

import ru.oepak22.githubmvp.content.Benefit;

public class WalkthroughAdapter extends FragmentStatePagerAdapter {

    private final List<Benefit> mBenefits;

    public WalkthroughAdapter(FragmentManager fm, @NonNull List<Benefit> benefits) {
        super(fm);
        mBenefits = benefits;
    }

    @Override
    public Fragment getItem(int position) {
        return WalkthroughBenefitFragment.create(mBenefits.get(position));
    }

    @Override
    public int getCount() {
        return mBenefits.size();
    }
}

