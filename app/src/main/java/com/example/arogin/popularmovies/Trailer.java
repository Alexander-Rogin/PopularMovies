package com.example.arogin.popularmovies;

/**
 * Created by arogin on 3/16/17.
 */

class Trailer {
    private String mId;
    private String mName;
    private String mKey;
    private static String YOUTUBE_PREFIX = "www.youtube.com/watch";
    private static String YOUTUBE_ID_PARAM = "v";

    public Trailer(String id, String name, String key) {
        mId = id;
        mName = name;
        mKey = key;
    }
}
