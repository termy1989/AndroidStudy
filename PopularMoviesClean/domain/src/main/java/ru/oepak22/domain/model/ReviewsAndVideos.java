package ru.oepak22.domain.model;

import java.util.List;

public class ReviewsAndVideos {

    List<Review> reviews;
    List<Video> videos;

    public ReviewsAndVideos(List<Review> reviews, List<Video> videos) {
        this.reviews = reviews;
        this.videos = videos;
    }

    public List<Review> getReviews() { return this.reviews; }
    public List<Video> getVideos() { return this.videos; }
}
