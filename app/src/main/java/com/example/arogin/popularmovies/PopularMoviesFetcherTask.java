package com.example.arogin.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
    private static final String POPULAR_MOVIES_VIDEOS = "videos";

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

        String results = getNetworkData(builtUri);
        if (results != null) {
            Movie[] movies = parseMoviesJsonData(results);
            findAndSetTrailers(movies);
            return movies;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        if (movies != null) {
            mMovieAdapter.setMoviesData(movies);
        }
    }

    private String getNetworkData(Uri uri) {
        URL url;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            String results = NetworkDataFetcher.getResponseFromUrl(url);
            return results;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
                int id = movie.getInt(JSON_ID_CODE);

                movies[i] = new Movie(id, title, posterPath, overview, rating, releaseDate);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Trailer[] getTrailers(int id) {
        Uri builtUri = Uri.parse(POPULAR_MOVIES_BASE_ADDRESS).buildUpon()
                .appendPath(Integer.toString(id))
                .appendPath(POPULAR_MOVIES_VIDEOS)
                .appendQueryParameter(POPULAR_MOVIES_API_KEY_PARAM, POPULAR_MOVIES_API_KEY)
                .build();
        String results = getNetworkData(builtUri);
        if (results != null) {
            Trailer[] trailers = parseTrailersJsonData(results);
            return trailers;
        }
        return null;
    }

    private Trailer[] parseTrailersJsonData(String json) {
        final String JSON_RESULTS_CODE = "results";
        final String JSON_NAME_CODE = "name";
        final String JSON_KEY_CODE = "key";
        final String JSON_ID_CODE = "id";

        try {
            JSONObject trailersJson = new JSONObject(json);
            if (trailersJson.has(JSON_RESULTS_CODE) == false) {
                return null;
            }
            JSONArray trailersArray = trailersJson.getJSONArray(JSON_RESULTS_CODE);
            Trailer[] trailers = new Trailer[trailersArray.length()];
            for (int i = 0; i < trailersArray.length(); i++) {
                JSONObject trailer = trailersArray.getJSONObject(i);

                String name = trailer.getString(JSON_NAME_CODE);
                String key = trailer.getString(JSON_KEY_CODE);
                String id = trailer.getString(JSON_ID_CODE);

                trailers[i] = new Trailer(id, name, key);
            }
            return trailers;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void findAndSetTrailers(Movie[] movies) {
        for (Movie movie : movies) {
            Trailer[] trailers = getTrailers(movie.getId());
            movie.setTrailers(trailers);
        }
    }
}
