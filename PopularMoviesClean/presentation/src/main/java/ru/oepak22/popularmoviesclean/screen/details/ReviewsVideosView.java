package ru.oepak22.popularmoviesclean.screen.details;

import androidx.annotation.NonNull;

import ru.oepak22.domain.model.ReviewsAndVideos;
import ru.oepak22.popularmoviesclean.screen.general.LoadingView;

// интерфейс вывода данных (списки ревью и трейлеров)
public interface ReviewsVideosView extends LoadingView {
    void showReviewsAndVideos(@NonNull ReviewsAndVideos reviewsAndVideos);
    void showError();
}
