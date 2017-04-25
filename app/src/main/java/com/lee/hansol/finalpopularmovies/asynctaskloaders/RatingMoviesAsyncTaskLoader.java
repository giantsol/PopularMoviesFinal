package com.lee.hansol.finalpopularmovies.asynctaskloaders;

import android.content.Context;

import com.lee.hansol.finalpopularmovies.utils.UriUtils;

import java.net.URL;

public class RatingMoviesAsyncTaskLoader extends AbstractMoviesAsyncTaskLoader {

    public RatingMoviesAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected URL getUrl() {
        return UriUtils.getRatingMoviesUrl(getContext());
    }
}
