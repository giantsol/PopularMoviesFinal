package com.lee.hansol.finalpopularmovies.asynctaskloaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.JSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.net.URL;

abstract class AbstractMovieListAsyncTaskLoader extends AbstractAsyncTaskLoaderWithJson<Movie[]> {

    AbstractMovieListAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected Movie[] processJson(String responseJson) throws JSONException {
        return JSONUtils.getMoviesFromJson(responseJson);
    }

}
