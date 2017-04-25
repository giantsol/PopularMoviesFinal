package com.lee.hansol.finalpopularmovies;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lee.hansol.finalpopularmovies.adapters.MovieListAdapter;
import com.lee.hansol.finalpopularmovies.asynctasks.PopularMoviesAsyncTask;
import com.lee.hansol.finalpopularmovies.databinding.ActivityMainBinding;
import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.MovieJSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;
import com.lee.hansol.finalpopularmovies.utils.UriUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnMovieItemClickListener, PopularMoviesAsyncTask.MovieLoaderCallbacks{
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
        URL popularMoviesUrl = UriUtils.getPopularMoviesUrl(this);
        if (popularMoviesUrl == null) {
            showErrorMessage();
        } else {
            startLoadingMoviesOnline(popularMoviesUrl);
        }
    }

    private void startLoadingMoviesOnline(URL url) {
        new PopularMoviesAsyncTask(this).execute(url);
    }

    private void showErrorMessage() {
        mainLayout.activityMainProgressBar.setVisibility(View.INVISIBLE);
        mainLayout.activityMainRecyclerView.setVisibility(View.INVISIBLE);
        mainLayout.activityMainErrorView.setVisibility(View.VISIBLE);
    }
    private void showOnlyProgressBar() {
        mainLayout.activityMainRecyclerView.setVisibility(View.INVISIBLE);
        mainLayout.activityMainErrorView.setVisibility(View.INVISIBLE);
        mainLayout.activityMainProgressBar.setVisibility(View.VISIBLE);
    }
    private void showMovieListView() {
        mainLayout.activityMainRecyclerView.setVisibility(View.VISIBLE);
        mainLayout.activityMainErrorView.setVisibility(View.INVISIBLE);
        mainLayout.activityMainProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(Movie movie) {
        //TODO: Go to DetailsActivity with the given movie
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, movie.title, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onPreMovieLoad() {
        showOnlyProgressBar();
    }

    @Override
    public void onPostMovieLoad(Movie[] movies) {
        if (movies == null) {
            showErrorMessage();
        } else {
            recyclerViewAdapter.setMoviesAndRefresh(movies);
            showMovieListView();
        }
    }
}
