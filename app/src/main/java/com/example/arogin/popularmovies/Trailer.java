package com.example.arogin.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arogin on 3/16/17.
 */

class Trailer implements Parcelable{
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

    protected Trailer(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mKey = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mKey);
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
