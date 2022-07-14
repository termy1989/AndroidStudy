package ru.oepak22.data.model.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.oepak22.data.model.content.Review;

// класс списка ревью
public class ReviewsResponse {

    @SerializedName("results")
    private List<Review> mReviews;

    @NonNull
    public List<Review> getReviews() {
        if (mReviews == null) {
            //mReviews = new ArrayList<>();
            return new ArrayList<>();
        }
        return mReviews;
    }
}
