package com.example.arogin.popularmovies;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arogin on 3/15/17.
 */

class PopularMoviesFetcherTask extends AsyncTask<String, Void, Movie[]> {
    private MovieAdapter mMovieAdapter;

    public PopularMoviesFetcherTask(MovieAdapter movieAdapter) {
        super();
        mMovieAdapter = movieAdapter;
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        String results = MovieDbHelper.getMoviesJsonData(params[0]);
        if (results != null) {
            return parseMoviesJsonData(results);
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
        final String JSON_ID_CODE = "id";

        try {
            JSONObject moviesJson = new JSONObject(json);
            if (!moviesJson.has(JSON_RESULTS_CODE)) {
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
                int id = movie.getInt(JSON_ID_CODE);

                movies[i] = new Movie(id, title, posterPath, overview, rating, releaseDate);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
