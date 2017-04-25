package com.lee.hansol.finalpopularmovies.utils;


import android.net.Uri;
import com.lee.hansol.finalpopularmovies.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieJSONUtils {

    public static Movie[] getMoviesFromJson(String json) throws JSONException{
        Movie[] movies = null;

        JSONObject root = new JSONObject(json);
        JSONArray movieJsons = root.getJSONArray("results");
        movies = new Movie[movieJsons.length()];

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
        return movies;
    }
}
