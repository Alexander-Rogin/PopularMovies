package com.example.arogin.popularmovies;

import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by arogin on 3/23/17.
 */

public class MovieDbHelper {
    public static final String POPULAR_MOVIES_BASE_ADDRESS = "http://api.themoviedb.org/3/movie/";
    public static final String POPULAR_MOVIES_DEFAULT_SORT = "popular";
    public static final String POPULAR_MOVIES_API_KEY_PARAM = "api_key";
    public static final String POPULAR_MOVIES_API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;
    public static final String POPULAR_MOVIES_VIDEOS = "videos";
    public static final String POPULAR_MOVIES_REVIEWS = "reviews";

    private static Uri getMoviesUri(String sortType) {
        if (sortType == null || sortType == "") {
            sortType = POPULAR_MOVIES_DEFAULT_SORT;
        }
        return Uri.parse(POPULAR_MOVIES_BASE_ADDRESS).buildUpon()
                .appendQueryParameter(POPULAR_MOVIES_API_KEY_PARAM, POPULAR_MOVIES_API_KEY)
                .appendPath(sortType).build();
    }

    private static String getNetworkData(Uri uri) {
        URL url;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            String results = NetworkDataFetcher.getResponseFromUrl(url);
            return results;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMoviesJsonData(String sortType) {
        Uri uri = getMoviesUri(sortType);
        return getNetworkData(uri);
    }

    public static String getTrailersJsonData(Integer movieId) {
        Uri uri = getTrailersUri(movieId);
        return getNetworkData(uri);
    }

    private static Uri getTrailersUri(Integer movieId) {
        return Uri.parse(POPULAR_MOVIES_BASE_ADDRESS).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath(POPULAR_MOVIES_VIDEOS)
                .appendQueryParameter(POPULAR_MOVIES_API_KEY_PARAM, POPULAR_MOVIES_API_KEY)
                .build();
    }

    public static String getReviewsJsonData(Integer movieId) {
        Uri uri = getReviewsUri(movieId);
        return getNetworkData(uri);
    }

    private static Uri getReviewsUri(Integer movieId) {
        return Uri.parse(POPULAR_MOVIES_BASE_ADDRESS).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath(POPULAR_MOVIES_REVIEWS)
                .appendQueryParameter(POPULAR_MOVIES_API_KEY_PARAM, POPULAR_MOVIES_API_KEY)
                .build();
    }
}
