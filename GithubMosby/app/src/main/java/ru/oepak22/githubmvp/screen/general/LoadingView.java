package ru.oepak22.githubmvp.screen.general;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoadingView extends MvpView {

    void showLoading();

    void hideLoading();

}
