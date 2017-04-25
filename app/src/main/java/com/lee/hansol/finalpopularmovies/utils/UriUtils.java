package com.lee.hansol.finalpopularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.lee.hansol.finalpopularmovies.R;

import java.net.MalformedURLException;
import java.net.URL;

public class UriUtils {

    public static URL getPopularMoviesUrl(Context context) {
        return getOnlineMoviesUrl(context, context.getString(R.string.url_base_popularity));
    }

    public static URL getRatingMoviesUrl(Context context) {
        return getOnlineMoviesUrl(context, context.getString(R.string.url_base_rating));
    }

    private static URL getOnlineMoviesUrl(Context context, String baseUrl) {
        String myApiKey = context.getString(R.string.api_key);
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(context.getString(R.string.url_param_api_key), myApiKey)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
