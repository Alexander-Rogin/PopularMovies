package com.example.arogin.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arogin on 3/16/17.
 */

class Trailer {
    private String mId;
    private String mName;
    private String mKey;
    private static String YOUTUBE_PREFIX = "http://www.youtube.com/watch";
    private static String YOUTUBE_ID_PARAM = "v";

    public Trailer(String id, String name, String key) {
        mId = id;
        mName = name;
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public Uri getTrailerUri() {
        Uri builtUri = Uri.parse(YOUTUBE_PREFIX).buildUpon()
                .appendQueryParameter(YOUTUBE_ID_PARAM, mKey)
                .build();

        return builtUri;
    }
}
