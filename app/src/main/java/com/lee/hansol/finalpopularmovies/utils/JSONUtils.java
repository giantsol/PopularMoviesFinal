package com.lee.hansol.finalpopularmovies.utils;


import android.net.Uri;
import android.util.Log;

import com.lee.hansol.finalpopularmovies.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    public static Movie[] getMoviesFromJson(String json) throws JSONException{
        JSONObject root = new JSONObject(json);
        JSONArray movieJsons = root.getJSONArray("results");
        Movie[] movies = new Movie[movieJsons.length()];

        for (int i = 0; i < movies.length; i++) {
            JSONObject movieJson = movieJsons.getJSONObject(i);
            int movieId = movieJson.getInt("id");
            String originalTitle = movieJson.getString("original_title");
            String thumbnailPath = movieJson.getString("poster_path");
            thumbnailPath = Uri.parse("http://image.tmdb.org/t/p/w185/" + thumbnailPath).toString();
            String date = movieJson.getString("release_date");
            String rating = movieJson.getString("vote_average");
            String overview = movieJson.getString("overview");

            Movie movie = new Movie(movieId, originalTitle, thumbnailPath, date, rating, overview);
            movies[i] = movie;
        }

        if (movies[0] == null) return null;
        else return movies;
    }

    public static String[] getTrailerKeysFromJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray videoJsons = root.getJSONArray("results");
        ArrayList<String> trailerKeys = new ArrayList<>();
        for (int i = 0; i < videoJsons.length(); i++) {
            JSONObject videoJson = videoJsons.getJSONObject(i);
            String site = videoJson.getString("site");
            String type = videoJson.getString("type");
            if ((!site.equalsIgnoreCase("YouTube")) || (!type.equals("Trailer"))) continue;
            String trailerKey = videoJson.getString("key");
            trailerKeys.add(trailerKey);
        }
        if (!trailerKeys.isEmpty()) return (String[]) trailerKeys.toArray(new String[0]);
        else return null;
    }
}
