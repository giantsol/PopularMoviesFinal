package com.lee.hansol.finalpopularmovies.asynctaskloaders;

import android.content.Context;

import com.lee.hansol.finalpopularmovies.utils.JSONUtils;
import com.lee.hansol.finalpopularmovies.utils.UriUtils;

import org.json.JSONException;

import java.net.URL;

public class MovieTrailersAsyncTaskLoader extends AbstractAsyncTaskLoaderWithJson<String[]> {
    private final int movieId;

    public MovieTrailersAsyncTaskLoader(Context context, int movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected URL getUrl() {
        return UriUtils.getMovieTrailersUrl(getContext(), movieId);
    }

    @Override
    protected String[] processJson(String responseJson) throws JSONException {
        return JSONUtils.getTrailerKeysFromJson(responseJson);
    }
}
