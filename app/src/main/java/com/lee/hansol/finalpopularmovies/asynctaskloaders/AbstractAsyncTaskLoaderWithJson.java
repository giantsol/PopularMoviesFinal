package com.lee.hansol.finalpopularmovies.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;

import org.json.JSONException;

import java.net.URL;

abstract class AbstractAsyncTaskLoaderWithJson<T> extends AsyncTaskLoader<T> {

    AbstractAsyncTaskLoaderWithJson(Context context) { super(context); }

    @Override
    protected final void onStartLoading() {
        forceLoad();
    }

    @Override
    public final T loadInBackground() {
        URL url = getUrl();
        if (url == null) return null;
        return loadFrom(url);
    }

    protected abstract URL getUrl();

    private T loadFrom(URL url) {
        T t = null;
        try {
            String responseInJSON = NetworkUtils.getResponseFromHttpUrl(url);
            t = processJson(responseInJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    protected abstract T processJson(String responseJson) throws JSONException;
}
