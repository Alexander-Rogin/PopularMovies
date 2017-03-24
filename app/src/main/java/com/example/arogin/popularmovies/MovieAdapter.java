package com.example.arogin.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arogin on 2/9/17.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private final MovieAdapterOnClickHandler mClickHandler;
    private Movie[] mMovies;
    private Context mContext;
    private Cursor mCursor;

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shoulAttachToParentImmediately = false;
        
        View view = inflater.inflate(layoutIdForListItem, parent, shoulAttachToParentImmediately);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = mMovies[position];
        ImageView posterImageView = holder.mMovieImageView;
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + movie.getPosterRelativePath()).into(posterImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.length;
    }

    public void setMoviesData(Movie[] movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void setMoviesDataFromCursor(Cursor cursor) {
        if (cursor == null) {
            return;
        }

        ArrayList<Movie> movies = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int movieIdIndex = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
            int titleIndex = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE);
            int posterIndex = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER);
            int overviewIndex = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW);
            int ratingIndex = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RATING);
            int releaseIndex = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE);

            movies.add(new Movie(
                    cursor.getInt(movieIdIndex),
                    cursor.getString(titleIndex),
                    cursor.getString(posterIndex),
                    cursor.getString(overviewIndex),
                    cursor.getDouble(ratingIndex),
                    cursor.getString(releaseIndex)
            ));
        }
        cursor.close();
        setMoviesData(movies.toArray(new Movie[movies.size()]));
//        if (mCursor == c) {
//            return;
//        }
//        Cursor temp = mCursor;
//        this.mCursor = c;
//
//        if (c != null) {
//            this.notifyDataSetChanged();
//        }
//        return temp;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie chosenMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }
}
