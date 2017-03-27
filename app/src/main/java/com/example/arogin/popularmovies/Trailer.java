package com.example.arogin.popularmovies;

import android.net.Uri;

/**
 * Created by arogin on 3/16/17.
 */

class Trailer {
    private String mName;
    private String mKey;
    private static String YOUTUBE_PREFIX = "http://www.youtube.com/watch";
    private static String YOUTUBE_ID_PARAM = "v";

    public Trailer(String name, String key) {
        mName = name;
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public Uri getTrailerUri() {

        return Uri.parse(YOUTUBE_PREFIX).buildUpon()
                .appendQueryParameter(YOUTUBE_ID_PARAM, mKey)
                .build();
    }
}
