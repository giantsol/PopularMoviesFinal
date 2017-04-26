package com.lee.hansol.finalpopularmovies.helpers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lee.hansol.finalpopularmovies.helpers.MovieContract.FavoriteMovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMovieEntry.COLUMN_TITLE + " TEXT, " +
                FavoriteMovieEntry.COLUMN_THUMBNAIL_PATH + " TEXT, " +
                FavoriteMovieEntry.COLUMN_DATE + " TEXT, " +
                FavoriteMovieEntry.COLUMN_RATING + " TEXT, " +
                FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                " UNIQUE (" + FavoriteMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
