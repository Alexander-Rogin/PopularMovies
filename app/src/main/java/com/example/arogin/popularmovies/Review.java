package com.example.arogin.popularmovies;

/**
 * Created by arogin on 3/24/17.
 */

class Review {
    private String mReviewIntro;
    private String mReviewUrl;

    public Review(String reviewIntro, String url) {
        mReviewIntro = reviewIntro;
        mReviewUrl = url;
    }

    public String getReviewIntro() {
        return mReviewIntro;
    }

    public String getReviewUrl() {
        return mReviewUrl;
    }
}
