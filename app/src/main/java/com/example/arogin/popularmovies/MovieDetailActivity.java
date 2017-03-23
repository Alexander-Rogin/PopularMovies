package com.example.arogin.popularmovies;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    private static String LOG_TAG = "MovieDetailActivity";
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mRelease;
    private RecyclerView mRecyclerViewTrailers;
    private LinearLayoutManager mLinearLayoutManager;
    private TrailerAdapter mTrailerAdapter;
    private Button mReviewButton;

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

            new TrailerFetcherTask(mTrailerAdapter).execute(mMovie.getId());
        }
    }

    private boolean isFavorite() {
        Cursor cursor = MainActivity.DB.rawQuery( "select * from " +
                FavoritesContract.FavoritesEntry.TABLE_NAME +
                " where " + FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID +
                "=" + mMovie.getId() + "", null);
        return cursor.getCount() > 0;
    }

    private void findViews() {
        mPoster = (ImageView) findViewById(R.id.iv_thumbnail);
        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mRelease = (TextView) findViewById(R.id.tv_release);
        mReviewButton = (Button) findViewById(R.id.open_reviews);
        mReviewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mMovie != null) {
                    Intent intent = new Intent(this, ReviewActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, mMovie);
                    startActivity(intent);
                }
            }
        });
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
        boolean checked = addButton.isChecked();
        if (checked) {
            removeFavorite();
            addButton.setIcon(android.R.drawable.star_off);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            addFavorite();
            addButton.setIcon(android.R.drawable.star_on);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
        addButton.setChecked(!checked);
    }

    private void removeFavorite() {
        MainActivity.DB.delete(FavoritesContract.FavoritesEntry.TABLE_NAME,
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=" + mMovie.getId(), null);
    }

    private void addFavorite() {
        ContentValues cv = new ContentValues();

        cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovie.getId());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, mMovie.getTitle());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER, mMovie.getPosterRelativePath());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW, mMovie.getOverview());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_RATING, mMovie.getRating());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        if (MainActivity.DB.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, cv) == -1) {
            Log.d(LOG_TAG, "Inserting returned an error. Probably, the entry exists");
        }
    }
}
