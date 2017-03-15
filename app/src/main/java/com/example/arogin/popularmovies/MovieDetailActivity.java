package com.example.arogin.popularmovies;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        findViews();

        Intent parentIntent = getIntent();
        if (parentIntent.hasExtra(Intent.EXTRA_TEXT)) {
            Movie movie = parentIntent.getParcelableExtra(Intent.EXTRA_TEXT);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w185" + movie.getPosterRelativePath()).into(mPoster);
            mTitle.setText(movie.getTitle());
            mPlot.setText(movie.getOverview());
            mRating.setText(String.valueOf(movie.getRating()));
            mRelease.setText(movie.getReleaseDate());
        }
    }

    private void findViews() {
        mPoster = (ImageView) findViewById(R.id.iv_thumbnail);
        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mRelease = (TextView) findViewById(R.id.tv_release);
    }
}
