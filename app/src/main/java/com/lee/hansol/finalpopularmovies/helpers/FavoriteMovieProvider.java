package com.lee.hansol.finalpopularmovies.helpers;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.sql.RowSetInternal;

public class FavoriteMovieProvider extends ContentProvider {
    public final int CODE_FAVORITE_MOVIE = 200;
    public final int CODE_FAVORITE_MOVIE_WITH_MOVIE_ID = 201;

    private UriMatcher uriMatcher;
    private MovieDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        uriMatcher = buildUriMatcher();
        dbHelper = new MovieDbHelper(getContext());
        return false;
    }

    private UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIE, CODE_FAVORITE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIE + "/#", CODE_FAVORITE_MOVIE_WITH_MOVIE_ID);
        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch(uriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIE:
                cursor = dbHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            case CODE_FAVORITE_MOVIE_WITH_MOVIE_ID:
                String movieIdString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieIdString};
                cursor = dbHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null, null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Unimplemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int rowsInserted = 0;

        switch(uriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIE:
                long id = dbHelper.getWritableDatabase().insert(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        null,
                        values
                );
                if (id != -1) rowsInserted++;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numOfRowsDeleted;
        switch (uriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIE_WITH_MOVIE_ID:
                String movieIdString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieIdString};
                numOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numOfRowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);
        return numOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Unimplemented");
    }
}
