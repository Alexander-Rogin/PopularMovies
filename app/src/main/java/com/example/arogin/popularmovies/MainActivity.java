package com.example.arogin.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView mRecyclerViewMovies;
    private GridLayoutManager mGridLayoutManager;
    private MovieAdapter mMovieAdapter;

    private static final int LAYOUT_COLUMN_COUNT = 5;

    private static final String POPULAR_SORT = "popular";
    private static final String RATING_SORT = "top_rated";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAVORITES_LOADER_ID = 0;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerViewMovies = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mGridLayoutManager = new GridLayoutManager(this, LAYOUT_COLUMN_COUNT, GridLayoutManager.VERTICAL, false);
        mRecyclerViewMovies.setLayoutManager(mGridLayoutManager);

        mRecyclerViewMovies.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerViewMovies.setAdapter(mMovieAdapter);

        new PopularMoviesFetcherTask(mMovieAdapter).execute("");

        getSupportLoaderManager().initLoader(FAVORITES_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMenu == null) {
            return;
        }
        MenuItem item = mMenu.findItem(R.id.menuFavorites);
        if (item == null) {
            return;
        }
        if (item.isChecked()) {
            getSupportLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, this);
        }
    }

    @Override
    public void onClick(Movie chosenMovie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, chosenMovie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_options, menu);
        MenuItem item = mMenu.findItem(R.id.menuSortPopular);
        item.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSortPopular:
                new PopularMoviesFetcherTask(mMovieAdapter).execute(POPULAR_SORT);
                break;
            case R.id.menuSortTopRated:
                new PopularMoviesFetcherTask(mMovieAdapter).execute(RATING_SORT);
                break;
            case R.id.menuFavorites:
                getSupportLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        item.setChecked(true);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mFavoritesData = null;

            @Override
            protected void onStartLoading() {
                if (mFavoritesData != null) {
                    deliverResult(mFavoritesData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mFavoritesData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.setMoviesDataFromCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.setMoviesDataFromCursor(null);
    }
}
