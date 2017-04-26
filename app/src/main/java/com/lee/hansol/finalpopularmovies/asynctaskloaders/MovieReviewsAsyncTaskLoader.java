package com.lee.hansol.finalpopularmovies.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.lee.hansol.finalpopularmovies.utils.JSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;
import com.lee.hansol.finalpopularmovies.utils.UriUtils;

import org.json.JSONException;

import java.net.URL;

public class MovieReviewsAsyncTaskLoader extends AbstractAsyncTaskLoaderWithJson<String[]> {
    private final int movieId;

    public MovieReviewsAsyncTaskLoader (Context context, int movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected URL getUrl() {
        return UriUtils.getMovieReviewsUrl(getContext(), movieId);
    }

    @Override
    protected String[] processJson(String responseJson) throws JSONException {
        return JSONUtils.getMovieReviewsFromJson(responseJson);
    }
}
