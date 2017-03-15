package com.example.arogin.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by arogin on 3/15/17.
 */

public class PopularMoviesFetcherTask extends AsyncTask<String, Void, Movie[]> {
    private static final String POPULAR_MOVIES_BASE_ADDRESS = "http://api.themoviedb.org/3/movie/";
    private static final String POPULAR_MOVIES_DEFAULT_SORT = "popular";
    private static final String POPULAR_MOVIES_API_KEY_PARAM = "api_key";
    private static final String POPULAR_MOVIES_API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;

    private MovieAdapter mMovieAdapter;

    public PopularMoviesFetcherTask(MovieAdapter movieAdapter) {
        super();
        mMovieAdapter = movieAdapter;
    }

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
