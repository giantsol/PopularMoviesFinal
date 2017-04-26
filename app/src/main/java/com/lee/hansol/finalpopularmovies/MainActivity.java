package com.lee.hansol.finalpopularmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lee.hansol.finalpopularmovies.adapters.MovieListAdapter;
import com.lee.hansol.finalpopularmovies.asynctaskloaders.PopularMoviesAsyncTaskLoader;
import com.lee.hansol.finalpopularmovies.asynctaskloaders.RatingMoviesAsyncTaskLoader;
import com.lee.hansol.finalpopularmovies.databinding.ActivityMainBinding;
import com.lee.hansol.finalpopularmovies.models.Movie;

import static com.lee.hansol.finalpopularmovies.utils.ToastUtils.toast;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnMovieItemClickListener, LoaderManager.LoaderCallbacks<Movie[]> {
    private ActivityMainBinding layout;
    private MovieListAdapter recyclerViewAdapter;
    private SharedPreferences prefs;

    private final int GRID_COL_NUM = 2;

    private final int BY_POPULARITY = 0;
    private final int BY_RATING = 1;
    private final int BY_FAVORITE = 2;

    private final int LOADER_LOAD_MOVIES_ID = 1515;

    public static final String INTENT_EXTRA_MOVIE_OBJECT = "movie-object";

    private final String BUNDLE_ORDERING_KEY = "bundle-ordering";

    private final String PREF_KEY_LATEST_ORDERING = "latest-ordering";

    private int currentOrdering = BY_POPULARITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        layout.activityMainRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_COL_NUM, LinearLayoutManager.VERTICAL, false);
        layout.activityMainRecyclerView.setLayoutManager(gridLayoutManager);
        recyclerViewAdapter = new MovieListAdapter(this);
        layout.activityMainRecyclerView.setAdapter(recyclerViewAdapter);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        currentOrdering = prefs.getInt(PREF_KEY_LATEST_ORDERING, BY_POPULARITY);

        loadMovies();
    }

    private void loadMovies() {
        showOnlyProgressBar();
        startLoader();
    }

    private void startLoader() {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_ORDERING_KEY, currentOrdering);

        Loader<Movie[]> loader = getSupportLoaderManager().getLoader(LOADER_LOAD_MOVIES_ID);
        if (loader == null) getSupportLoaderManager().initLoader(LOADER_LOAD_MOVIES_ID, args, this);
        else getSupportLoaderManager().restartLoader(LOADER_LOAD_MOVIES_ID, args, this);
    }

    private void showErrorMessage() {
        layout.activityMainProgressBar.setVisibility(View.INVISIBLE);
        layout.activityMainRecyclerView.setVisibility(View.INVISIBLE);
        layout.activityMainErrorView.setVisibility(View.VISIBLE);
    }
    private void showOnlyProgressBar() {
        layout.activityMainRecyclerView.setVisibility(View.INVISIBLE);
        layout.activityMainErrorView.setVisibility(View.INVISIBLE);
        layout.activityMainProgressBar.setVisibility(View.VISIBLE);
    }
    private void showMovieListView() {
        layout.activityMainRecyclerView.setVisibility(View.VISIBLE);
        layout.activityMainErrorView.setVisibility(View.INVISIBLE);
        layout.activityMainProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMovieItemClick(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(INTENT_EXTRA_MOVIE_OBJECT, movie);
        startActivity(intent);
    }

    @Override
    public Loader<Movie[]> onCreateLoader(int id, Bundle args) {
        if (!args.containsKey(BUNDLE_ORDERING_KEY)) throw new RuntimeException("Ordering key not passed to onCreateLoader");
        int ordering = args.getInt(BUNDLE_ORDERING_KEY);

        if (ordering == BY_POPULARITY) {
            return new PopularMoviesAsyncTaskLoader(this);
        } else if (ordering == BY_RATING){
            return new RatingMoviesAsyncTaskLoader(this);
        } else if (ordering == BY_FAVORITE){
            //TODO implement
            throw new RuntimeException("unimplemented");
        } else {
            throw new UnsupportedOperationException("Unknown ordering: " + ordering);
        }
    }

    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] data) {
        if (data == null) showErrorMessage();
        else {
            recyclerViewAdapter.setMoviesAndRefresh(data);
            showMovieListView();
            setActionBarTitleToCurrentOrdering();
        }
    }

    private void setActionBarTitleToCurrentOrdering() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        if (currentOrdering == BY_POPULARITY) actionBar.setTitle(getString(R.string.actionbar_title_popularity));
        else if (currentOrdering == BY_RATING) actionBar.setTitle(getString(R.string.actionbar_title_rating));
        else if (currentOrdering == BY_FAVORITE) actionBar.setTitle(getString(R.string.actionbar_title_favorite));
    }

    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {
        loader.cancelLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem refreshButton = menu.findItem(R.id.activity_main_menu_refresh);
        refreshButton.getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.activity_main_menu_popularity:
                reloadMoviesBy(BY_POPULARITY); break;
            case R.id.activity_main_menu_rating:
                reloadMoviesBy(BY_RATING); break;
            case R.id.activity_main_menu_favorite:
                reloadMoviesBy(BY_FAVORITE); break;
            case R.id.activity_main_menu_refresh:
                loadMovies(); break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void reloadMoviesBy(int newOrdering) {
        currentOrdering = newOrdering;
        loadMovies();
    }

    @Override
    protected void onStop() {
        super.onStop();
        prefs.edit().putInt(PREF_KEY_LATEST_ORDERING, currentOrdering).apply();
    }
}
