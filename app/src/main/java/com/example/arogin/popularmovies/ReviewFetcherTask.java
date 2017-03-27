package com.example.arogin.popularmovies;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arogin on 3/24/17.
 */

class ReviewFetcherTask extends AsyncTask<Integer, Void, Review[]> {
    private ReviewAdapter mReviewAdapter;

    public ReviewFetcherTask(ReviewAdapter reviewAdapter) {
        super();
        mReviewAdapter = reviewAdapter;
    }

    @Override
    protected Review[] doInBackground(Integer... params) {
        String results = MovieDbHelper.getReviewsJsonData(params[0]);
        if (results != null) {
            return parseReviewsJsonData(results);
        }
        return null;
    }

    private Review[] parseReviewsJsonData(String json) {
        final String JSON_RESULTS_CODE = "results";
        final String JSON_REVIEW_CONTENT_CODE = "content";
        final String JSON_REVIEW_URL_CODE = "url";

        try {
            JSONObject moviesJson = new JSONObject(json);
            if (!moviesJson.has(JSON_RESULTS_CODE)) {
                return null;
            }
            JSONArray reviewsArray = moviesJson.getJSONArray(JSON_RESULTS_CODE);
            Review[] reviews = new Review[reviewsArray.length()];
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject review = reviewsArray.getJSONObject(i);
                String reviewText = review.getString(JSON_REVIEW_CONTENT_CODE);
                String fullReviewUrl = review.getString(JSON_REVIEW_URL_CODE);

                reviews[i] = new Review(reviewText, fullReviewUrl);
            }
            return reviews;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        if (reviews != null) {
            mReviewAdapter.setReviewsData(reviews);
        }
    }
}
