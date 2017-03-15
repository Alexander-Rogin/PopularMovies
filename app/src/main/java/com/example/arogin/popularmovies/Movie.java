package com.example.arogin.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arogin on 2/9/17.
 */

public class Movie implements Parcelable {
    private String mTitle;
    private String mPosterRelativePath;
    private String mOverview;
    private double mRating;
    private String mReleaseDate;

    public Movie(String title, String posterPath, String overview, double rating, String releaseDate) {
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
    }
}
