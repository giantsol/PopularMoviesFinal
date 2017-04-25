package com.lee.hansol.finalpopularmovies;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

import com.lee.hansol.finalpopularmovies.adapters.MovieListAdapter;
import com.lee.hansol.finalpopularmovies.asynctaskloaders.PopularMoviesAsyncTaskLoader;
import com.lee.hansol.finalpopularmovies.asynctaskloaders.RatingMoviesAsyncTaskLoader;
import com.lee.hansol.finalpopularmovies.databinding.ActivityMainBinding;
import com.lee.hansol.finalpopularmovies.models.Movie;

import static com.lee.hansol.finalpopularmovies.utils.ToastUtils.toast;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnMovieItemClickListener, LoaderManager.LoaderCallbacks<Movie[]> {
    private ActivityMainBinding mainLayout;
    private MovieListAdapter recyclerViewAdapter;

    private final int GRID_COL_NUM = 2;

    private final int BY_POPULARITY = 151;
    private final int BY_RATING = 152;
    private final int BY_FAVORITE = 153;

    private final String INTENT_EXTRA_MOVIE_OBJECT = "movie-object";

    //TODO: get last saved ordering and apply it to this variable
    private int ordering = BY_POPULARITY;

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
        loadMovies();
    }

    private void loadMovies() {
        showOnlyProgressBar();
        startLoader();
    }

    private void startLoader() {
        Loader<Movie[]> loader = getSupportLoaderManager().getLoader(ordering);
        if (loader == null) getSupportLoaderManager().initLoader(ordering, null, this);
        else getSupportLoaderManager().restartLoader(ordering, null, this);
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
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(INTENT_EXTRA_MOVIE_OBJECT, movie);
        startActivity(intent);
    }

    @Override
    public Loader<Movie[]> onCreateLoader(int id, Bundle args) {
        if (id == BY_POPULARITY) {
            return new PopularMoviesAsyncTaskLoader(this);
        } else if (id == BY_RATING){
            return new RatingMoviesAsyncTaskLoader(this);
        } else if (id == BY_FAVORITE){
            //TODO implement
            throw new RuntimeException("unimplemented");
        } else {
            throw new UnsupportedOperationException("Unknown loader id: " + id);
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
        if (ordering == BY_POPULARITY) actionBar.setTitle(getString(R.string.actionbar_title_popularity));
        else if (ordering == BY_RATING) actionBar.setTitle(getString(R.string.actionbar_title_rating));
        else if (ordering == BY_FAVORITE) actionBar.setTitle(getString(R.string.actionbar_title_favorite));
    }

    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {
        loader.cancelLoad();
        recyclerViewAdapter.setMoviesAndRefresh(null);
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
        ordering = newOrdering;
        loadMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: Got a problem. When coming back from the DetailsActivity, somehow the images are
        // random
//        loadMovies();
    }
}
