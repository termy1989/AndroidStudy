package ru.oepak22.data.mapper;

import ru.oepak22.domain.model.Review;
import rx.functions.Func1;

// класс маппера полученных данных
public class ReviewsMapper implements Func1<ru.oepak22.data.model.content.Review, Review> {

    @Override
    public Review call(ru.oepak22.data.model.content.Review review) {
        return new Review(review.getId(),
                            review.getAuthor(),
                            review.getContent(),
                            review.getUpdateAt());
    }
}
