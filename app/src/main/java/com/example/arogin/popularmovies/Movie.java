package com.example.arogin.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by arogin on 2/9/17.
 */

public class Movie implements Parcelable {
    private String mTitle;
    private String mPosterRelativePath;
    private String mOverview;
    private double mRating;
    private String mReleaseDate;
    private int mId;
    private Trailer[] mTrailers;

    public Movie(int id, String title, String posterPath, String overview, double rating, String releaseDate) {
        mId = id;
        mTitle = title;
        mPosterRelativePath = posterPath;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mPosterRelativePath = in.readString();
        mOverview = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readString();
        Parcelable[] trailersParcel = in.readParcelableArray(Trailer.class.getClassLoader());
        if (trailersParcel != null) {
            mTrailers = Arrays.copyOf(trailersParcel, trailersParcel.length, Trailer[].class);
        }
        mId = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getRating() {
        return mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterRelativePath() {
        return mPosterRelativePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPosterRelativePath);
        dest.writeString(mOverview);
        dest.writeDouble(mRating);
        dest.writeString(mReleaseDate);
        dest.writeParcelableArray(mTrailers, 0);
        dest.writeInt(mId);
    }

    public int getId() {
        return mId;
    }

    public void setTrailers(Trailer[] trailers) {
        mTrailers = trailers;
    }

    public Trailer[] getTrailers() {
        return mTrailers;
    }
}
