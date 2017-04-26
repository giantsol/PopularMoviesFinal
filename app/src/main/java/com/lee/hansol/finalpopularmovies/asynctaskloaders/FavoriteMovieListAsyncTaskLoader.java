package com.lee.hansol.finalpopularmovies.asynctaskloaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.lee.hansol.finalpopularmovies.helpers.MovieContract;
import com.lee.hansol.finalpopularmovies.models.Movie;

import java.util.ArrayList;

public class FavoriteMovieListAsyncTaskLoader extends AsyncTaskLoader<Movie[]> {

    public FavoriteMovieListAsyncTaskLoader(Context context) { super(context); }

    @Override
    protected final void onStartLoading() {
        forceLoad();
    }

    @Override
    public Movie[] loadInBackground() {
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(MovieContract.FavoriteMovieEntry.CONTENT_URI,
                    null, null, null, MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Movie movie = getMovieObjectFromCursor(cursor);
                    movies.add(movie);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return movies.toArray(new Movie[0]);
    }

    private Movie getMovieObjectFromCursor(Cursor cursor) {
        if (cursor == null) return null;

        int movieId = cursor.getInt(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID));
        String title = cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_TITLE));
        String thumbnailPath = cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL_PATH));
        String date = cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_DATE));
        String rating = cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_RATING));
        String overview = cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW));

        return new Movie(movieId, title, thumbnailPath, date, rating, overview);
    }
}
