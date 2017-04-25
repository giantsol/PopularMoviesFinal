package com.lee.hansol.finalpopularmovies.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.MovieJSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;
import com.lee.hansol.finalpopularmovies.utils.UriUtils;

import java.net.URL;

public class RatingMoviesAsyncTaskLoader extends AsyncTaskLoader<Movie[]> {
    private Movie[] movies = null;

    public RatingMoviesAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (movies != null) deliverResult(movies);
        else forceLoad();
    }

    @Override
    public Movie[] loadInBackground() {
        URL url = UriUtils.getRatingMoviesUrl(getContext());
        if (url == null) return null;
        return loadMoviesFrom(url);
    }

    private Movie[] loadMoviesFrom(URL url) {
        Movie[] movies = null;
        try {
            String responseInJSON = NetworkUtils.getResponseFromHttpUrl(url);
            movies = MovieJSONUtils.getMoviesFromJson(responseInJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    public void deliverResult(Movie[] data) {
        movies = data;
        super.deliverResult(movies);
    }
}
