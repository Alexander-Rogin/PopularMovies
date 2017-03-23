package com.example.arogin.popularmovies;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private RecyclerView mRecyclerViewMovies;
    private GridLayoutManager mGridLayoutManager;
    private MovieAdapter mMovieAdapter;

    private static final int LAYOUT_COLUMN_COUNT = 5;

    private static final String POPULAR_SORT = "popular";
    private static final String RATING_SORT = "top_rated";

    public static SQLiteDatabase DB;

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

        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(this);
        DB = dbHelper.getWritableDatabase();
    }

    @Override
    public void onClick(Movie chosenMovie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, chosenMovie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sortType = "";
        switch (item.getItemId()) {
            case R.id.menuSortPopular:
                sortType = POPULAR_SORT;
                break;
            case R.id.menuSortTopRated:
                sortType = RATING_SORT;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        new PopularMoviesFetcherTask(mMovieAdapter).execute(sortType);
        item.setChecked(true);
        return true;
    }
}
