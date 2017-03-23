package com.example.arogin.popularmovies;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arogin on 3/23/17.
 */

class TrailerFetcherTask extends AsyncTask<Integer, Void, Trailer[]> {
    TrailerAdapter mTrailerAdapter;

    public TrailerFetcherTask(TrailerAdapter trailerAdapter) {
        super();
        mTrailerAdapter = trailerAdapter;
    }

    @Override
    protected Trailer[] doInBackground(Integer... params) {
        String results = MovieDbHelper.getTrailersJsonData(params[0]);
        if (results != null) {
            Trailer[] trailers = parseTrailersJsonData(results);
            return trailers;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Trailer[] trailers) {
        if (trailers != null) {
            mTrailerAdapter.setTrailerData(trailers);
        }
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
}
