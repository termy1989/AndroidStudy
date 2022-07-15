package ru.oepak22.githubmvp.screen.walkthrough;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.List;

import ru.oepak22.githubmvp.content.Benefit;

public interface WalkthroughView {

    void showBenefits(@NonNull List<Benefit> benefits);
    void showActionButtonText(@StringRes int buttonTextId);
    void scrollToNextBenefit();
    void startAuth();

}
