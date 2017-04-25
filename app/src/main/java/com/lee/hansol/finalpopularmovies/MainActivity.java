package com.lee.hansol.finalpopularmovies;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lee.hansol.finalpopularmovies.adapters.MovieListAdapter;
import com.lee.hansol.finalpopularmovies.databinding.ActivityMainBinding;
import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.FakeDataUtils;
import com.lee.hansol.finalpopularmovies.utils.MovieJSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnMovieItemClickListener {
    private ActivityMainBinding mainLayout;
    private MovieListAdapter recyclerViewAdapter;
    private Toast toast;

    private final int GRID_COL_NUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        mainLayout.activityMainRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_COL_NUM, LinearLayoutManager.VERTICAL, false);
        mainLayout.activityMainRecyclerView.setLayoutManager(gridLayoutManager);
        recyclerViewAdapter = new MovieListAdapter(this);
        mainLayout.activityMainRecyclerView.setAdapter(recyclerViewAdapter);

        fillWithPopularMovies();
    }

    private void fillWithPopularMovies() {
        String myApiKey = getString(R.string.api_key);
        Uri uri = Uri.parse(getString(R.string.url_base_popularity)).buildUpon()
                .appendQueryParameter(getString(R.string.url_param_api_key), myApiKey)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mainLayout.activityMainRecyclerView.setVisibility(View.INVISIBLE);
            mainLayout.activityMainErrorView.setVisibility(View.VISIBLE);
        }

        if (url != null) {
            new AsyncTask<URL, Void, Movie[]>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mainLayout.activityMainErrorView.setVisibility(View.INVISIBLE);
                    mainLayout.activityMainProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected Movie[] doInBackground(URL... params) {
                    if (params.length == 0) return null;
                    URL url = params[0];
                    try {
                        String responseInJSON = NetworkUtils.getResponseFromHttpUrl(url);
                        Movie[] movies = MovieJSONUtils.getMoviesFromJson(responseInJSON);
                        return movies;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Movie[] movies) {
                    super.onPostExecute(movies);
                    if (movies != null) {
                        mainLayout.activityMainRecyclerView.setVisibility(View.VISIBLE);
                        mainLayout.activityMainErrorView.setVisibility(View.INVISIBLE);
                        mainLayout.activityMainProgressBar.setVisibility(View.INVISIBLE);
                        recyclerViewAdapter.setMoviesAndRefresh(movies);
                    } else {
                        mainLayout.activityMainRecyclerView.setVisibility(View.INVISIBLE);
                        mainLayout.activityMainErrorView.setVisibility(View.VISIBLE);
                        mainLayout.activityMainProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }.execute(url);
        }
    }

    @Override
    public void onItemClick(Movie movie) {
        //TODO: Go to DetailsActivity with the given movie
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, movie.title, Toast.LENGTH_SHORT);
        toast.show();
    }
}
