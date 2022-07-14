package ru.oepak22.popularmoviesagera.model.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.oepak22.popularmoviesagera.model.content.Review;

public class ReviewsResponse {

    @SerializedName("results")
    private List<Review> mReviews;

    @NonNull
    public List<Review> getReviews() {
        if (mReviews == null) {
            return new ArrayList<>();
        }
        return mReviews;
    }
}
