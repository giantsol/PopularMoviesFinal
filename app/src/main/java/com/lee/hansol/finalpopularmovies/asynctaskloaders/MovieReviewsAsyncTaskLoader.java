package com.lee.hansol.finalpopularmovies.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.lee.hansol.finalpopularmovies.utils.JSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;
import com.lee.hansol.finalpopularmovies.utils.UriUtils;

import java.net.URL;

public class MovieReviewsAsyncTaskLoader extends AsyncTaskLoader<String[]> {
    private final int movieId;

    public MovieReviewsAsyncTaskLoader (Context context, int movieId) {
        super(context);
        this.movieId = movieId;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String[] loadInBackground() {
        URL url = UriUtils.getMovieReviewsUrl(getContext(), movieId);
        if (url == null) return null;
        return loadReviewsFrom(url);
    }

    private String[] loadReviewsFrom(URL url) {
        Log.d("hi", url.toString());
        String[] reviews = null;
        try {
            String responseInJSON = NetworkUtils.getResponseFromHttpUrl(url);
            reviews = JSONUtils.getMovieReviewsFromJson(responseInJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
