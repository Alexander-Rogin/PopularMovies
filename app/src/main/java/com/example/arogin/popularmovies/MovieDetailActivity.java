package com.example.arogin.popularmovies;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mRelease;
    private RecyclerView mRecyclerViewTrailers;
    private LinearLayoutManager mLinearLayoutManager;
    private TrailerAdapter mTrailerAdapter;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mRecyclerViewTrailers = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewTrailers.setLayoutManager(mLinearLayoutManager);

        mRecyclerViewTrailers.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerViewTrailers.setAdapter(mTrailerAdapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        findViews();

        Intent parentIntent = getIntent();
        if (parentIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovie = parentIntent.getParcelableExtra(Intent.EXTRA_TEXT);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w185" + mMovie.getPosterRelativePath()).into(mPoster);
            mTitle.setText(mMovie.getTitle());
            mPlot.setText(mMovie.getOverview());
            mRating.setText(String.valueOf(mMovie.getRating()));
            mRelease.setText(mMovie.getReleaseDate());
            mTrailerAdapter.setTrailerData(mMovie.getTrailers());
        }
    }

    private void findViews() {
        mPoster = (ImageView) findViewById(R.id.iv_thumbnail);
        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mRelease = (TextView) findViewById(R.id.tv_release);
    }

    @Override
    public void onClick(Trailer chosenTrailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW, chosenTrailer.getTrailerUri());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_activity_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_favorite:
                handleFavorites(item);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleFavorites(MenuItem addButton) {
        boolean checked = addButton.isChecked();
        if (checked) {
            Toast.makeText(this, "Removing from favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Adding to favorites", Toast.LENGTH_SHORT).show();
        }
        addButton.setChecked(!checked);

    }
}
