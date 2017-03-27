package com.example.arogin.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by arogin on 3/23/17.
 */

public class FavoritesContract {
    public static final String AUTHORITY = "com.example.arogin.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
