package com.example.arogin.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity
        implements TrailerAdapter.TrailerAdapterOnClickHandler,
        ReviewAdapter.ReviewAdapterOnClickHandler {
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mRelease;
    private RecyclerView mRecyclerViewTrailers;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mRecyclerViewReviews;
    private ReviewAdapter mReviewAdapter;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        findViews();

        mRecyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewTrailers.setHasFixedSize(true);
        mRecyclerViewReviews.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this);
        mReviewAdapter = new ReviewAdapter(this);

        mRecyclerViewTrailers.setAdapter(mTrailerAdapter);
        mRecyclerViewReviews.setAdapter(mReviewAdapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        Intent parentIntent = getIntent();
        if (parentIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovie = parentIntent.getParcelableExtra(Intent.EXTRA_TEXT);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w185" + mMovie.getPosterRelativePath()).into(mPoster);
            mTitle.setText(mMovie.getTitle());
            mPlot.setText(mMovie.getOverview());
            mRating.setText(String.valueOf(mMovie.getRating()));
            mRelease.setText(mMovie.getReleaseDate());

            new TrailerFetcherTask(mTrailerAdapter).execute(mMovie.getId());
            new ReviewFetcherTask(mReviewAdapter).execute(mMovie.getId());
        }
    }

    private boolean isFavorite() {
        String movieId = mMovie.getId().toString();
        Uri uri = FavoritesContract.FavoritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId).build();
        Cursor cursor = getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        return cursor.getCount() > 0;
    }

    private void findViews() {
        mPoster = (ImageView) findViewById(R.id.iv_thumbnail);
        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mRelease = (TextView) findViewById(R.id.tv_release);
        mRecyclerViewTrailers = (RecyclerView) findViewById(R.id.recyclerview_trailers);
        mRecyclerViewReviews = (RecyclerView) findViewById(R.id.recyclerview_reviews);
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

        MenuItem addButton = menu.getItem(0);
        if (isFavorite()) {
            addButton.setIcon(android.R.drawable.star_on);
            addButton.setChecked(true);
        } else {
            addButton.setIcon(android.R.drawable.star_off);
            addButton.setChecked(false);
        }

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
        if (addButton.isChecked()) {
            removeFavorite(addButton);
        } else {
            addFavorite(addButton);
        }
    }

    private void removeFavorite(MenuItem addButton) {
        String movieId = mMovie.getId().toString();
        Uri uri = FavoritesContract.FavoritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId).build();

        int deleteCount = getContentResolver().delete(uri, null, null);
        if (deleteCount > 0) {
            addButton.setIcon(android.R.drawable.star_off);
            addButton.setChecked(false);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFavorite(MenuItem addButton) {
        ContentValues cv = new ContentValues();

        cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovie.getId());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, mMovie.getTitle());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER, mMovie.getPosterRelativePath());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW, mMovie.getOverview());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_RATING, mMovie.getRating());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, cv);
        if (uri != null) {
            addButton.setIcon(android.R.drawable.star_on);
            addButton.setChecked(true);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(Review chosenReview) {
        Uri uri = Uri.parse(chosenReview.getReviewUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
