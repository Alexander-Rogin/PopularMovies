package com.example.arogin.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arogin on 3/23/17.
 */

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoritesContract.FavoritesEntry.TABLE_NAME + " (" +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                FavoritesContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
