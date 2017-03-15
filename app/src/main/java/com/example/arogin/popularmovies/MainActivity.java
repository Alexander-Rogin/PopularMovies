package com.example.arogin.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private RecyclerView mRecyclerViewMovies;
    private GridLayoutManager mGridLayoutManager;
    private MovieAdapter mMovieAdapter;

    private static final int LAYOUT_COLUMN_COUNT = 5;

    private static final String POPULAR_SORT = "popular";
    private static final String RATING_SORT = "top_rated";


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

        new PopularMoviesFetcherTask().execute("");
    }

    @Override
    public void onClick(Movie chosenMovie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, chosenMovie);
        startActivity(intent);
    }

    public class PopularMoviesFetcherTask extends AsyncTask<String, Void, Movie[]> {
        private static final String POPULAR_MOVIES_BASE_ADDRESS = "http://api.themoviedb.org/3/movie/";
        private static final String POPULAR_MOVIES_DEFAULT_SORT = POPULAR_SORT;
        private static final String POPULAR_MOVIES_API_KEY_PARAM = "api_key";
        private static final String POPULAR_MOVIES_API_KEY = "INSERT_YOUR_API_KEY";

        @Override
        protected Movie[] doInBackground(String... params) {
            String sortType = (params[0] != null && params[0] != "") ? params[0] : POPULAR_MOVIES_DEFAULT_SORT;
            Uri builtUri = Uri.parse(POPULAR_MOVIES_BASE_ADDRESS).buildUpon()
                    .appendQueryParameter(POPULAR_MOVIES_API_KEY_PARAM, POPULAR_MOVIES_API_KEY)
                    .appendPath(sortType).build();
            URL popularMoviesUrl;
            try {

                popularMoviesUrl = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            try {
                String results = NetworkDataFetcher.getResponseFromUrl(popularMoviesUrl);
                return parseMoviesJsonData(results);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies != null) {
                mMovieAdapter.setMoviesData(movies);
            }
        }

        private Movie[] parseMoviesJsonData(String json) {
            final String JSON_RESULTS_CODE = "results";
            final String JSON_TITLE_CODE = "title";
            final String JSON_POSTER_PATH_CODE = "poster_path";
            final String JSON_OVERVIEW_CODE = "overview";
            final String JSON_RATING_CODE = "popularity";
            final String JSON_RELEASE_DATE_CODE = "release_date";

            try {
                JSONObject moviesJson = new JSONObject(json);
                if (moviesJson.has(JSON_RESULTS_CODE) == false) {
                    return null;
                }
                JSONArray moviesArray = moviesJson.getJSONArray(JSON_RESULTS_CODE);
                Movie[] movies = new Movie[moviesArray.length()];
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);

                    String title = movie.getString(JSON_TITLE_CODE);
                    String posterPath = movie.getString(JSON_POSTER_PATH_CODE);
                    String overview = movie.getString(JSON_OVERVIEW_CODE);
                    double rating = movie.getDouble(JSON_RATING_CODE);
                    String releaseDate = movie.getString(JSON_RELEASE_DATE_CODE);
                    movies[i] = new Movie(title, posterPath, overview, rating, releaseDate);
                }
                return movies;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
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
        new PopularMoviesFetcherTask().execute(sortType);
        item.setChecked(true);
        return true;
    }
}
