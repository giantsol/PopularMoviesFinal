package com.lee.hansol.finalpopularmovies.asynctasks;

import android.os.AsyncTask;

import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.MovieJSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;

import java.net.URL;

public class PopularMoviesAsyncTask extends AsyncTask<URL, Void, Movie[]> {
    private final MovieLoaderCallbacks callbacks;

    public PopularMoviesAsyncTask(MovieLoaderCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface MovieLoaderCallbacks {
        void onPreMovieLoad();
        void onPostMovieLoad(Movie[] movies);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callbacks.onPreMovieLoad();
    }

    @Override
    protected Movie[] doInBackground(URL... params) {
        if (params.length == 0) return null;
        URL url = params[0];
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

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        callbacks.onPostMovieLoad(movies);
    }
}
