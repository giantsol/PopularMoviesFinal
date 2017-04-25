package com.lee.hansol.finalpopularmovies.asynctaskloaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.MovieJSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;

import java.net.URL;

public abstract class AbstractMoviesAsyncTaskLoader extends AsyncTaskLoader<Movie[]> {

    public AbstractMoviesAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected final void onStartLoading() {
        forceLoad();
    }

    @Override
    public Movie[] loadInBackground() {
        URL url = getUrl();
        if (url == null) return null;
        return loadMoviesFrom(url);
    }

    protected abstract URL getUrl();

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
}