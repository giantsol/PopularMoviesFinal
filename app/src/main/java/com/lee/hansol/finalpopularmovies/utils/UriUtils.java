package com.lee.hansol.finalpopularmovies.utils;

import android.content.Context;
import android.net.Uri;

import com.lee.hansol.finalpopularmovies.R;
import com.lee.hansol.finalpopularmovies.helpers.MovieContract;

import java.net.MalformedURLException;
import java.net.URL;

public class UriUtils {

    public static URL getPopularMoviesUrl(Context context) {
        return getApiKeyAppendedUrl(context, context.getString(R.string.url_base_popularity));
    }

    public static URL getRatingMoviesUrl(Context context) {
        return getApiKeyAppendedUrl(context, context.getString(R.string.url_base_rating));
    }

    private static URL getApiKeyAppendedUrl(Context context, String baseUrl) {
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

    public static URL getMovieTrailersUrl(Context context, int movieId) {
        String baseUrl = String.format(context.getString(R.string.url_base_videos_with_id_holder), movieId);
        return getApiKeyAppendedUrl(context, baseUrl);
    }

    public static URL getMovieReviewsUrl(Context context, int movieid) {
        String baseUrl = String.format(context.getString(R.string.url_base_reviews_with_id_holder), movieid);
        return getApiKeyAppendedUrl(context, baseUrl);
    }

    public static Uri getYoutubeVideoUri(Context context, String videoKey) {
        String baseUri = context.getString(R.string.url_base_youtube_video);
        Uri uri = Uri.parse(baseUri).buildUpon()
                .appendQueryParameter(context.getString(R.string.url_param_video_key), videoKey)
                .build();
        return uri;
    }

    public static Uri getMovieContentUriWithId(int movieId) {
        return MovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
    }
}
