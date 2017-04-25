package com.lee.hansol.finalpopularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lee.hansol.finalpopularmovies.databinding.ActivityDetailsBinding;
import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.JSONUtils;
import com.lee.hansol.finalpopularmovies.utils.NetworkUtils;
import com.lee.hansol.finalpopularmovies.utils.UriUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>{
    private Movie movie;
    private ActivityDetailsBinding layout;
    private URL trailersUrl;

    private final int LOADER_LOAD_TRAILERS_ID = 156;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        initialize();
    }

    private void initialize() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Intent callingIntent = getIntent();
        if (callingIntent != null && callingIntent.hasExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT)) {
            movie =  callingIntent.getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT);
            fillContents();
        } else {
            showErrorView();
        }

        trailersUrl = UriUtils.getMovieTrailersUrl(this, movie.movieId);
        getSupportLoaderManager().initLoader(LOADER_LOAD_TRAILERS_ID, null, this);
    }

    private void fillContents() {
        layout.activityDetailsContainer.setVisibility(View.VISIBLE);
        layout.activityDetailsErrorView.setVisibility(View.INVISIBLE);
        layout.activityDetailsTitle.setText(movie.title);
        Picasso.with(this).load(movie.thumbnailPath).into(layout.activityDetailsThumbnail);
        layout.activityDetailsDate.setText(movie.date);
        layout.activityDetailsRating.setText(movie.rating);
        layout.activityDetailsOverview.setText(movie.overview);
    }

    private void showErrorView() {
        layout.activityDetailsContainer.setVisibility(View.INVISIBLE);
        layout.activityDetailsErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_LOAD_TRAILERS_ID) {
            return new AsyncTaskLoader<String[]>(this) {
                @Override
                protected void onStartLoading() {
                    forceLoad();
                }

                @Override
                public String[] loadInBackground() {
                    if (trailersUrl == null) return null;
                    return loadTrailersFrom(trailersUrl);
                }

                private String[] loadTrailersFrom(URL url) {
                    Log.d("hi", url.toString());
                    String[] trailerKeys = null;
                    try {
                        String responseInJSON = NetworkUtils.getResponseFromHttpUrl(url);
                        trailerKeys = JSONUtils.getTrailerKeysFromJson(responseInJSON);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return trailerKeys;
                }
            };
        } else {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        if (data ==null) return; //TODO: erase this line later
        if (loader.getId() == LOADER_LOAD_TRAILERS_ID) {
            //TODO: data is an array of trailer keys. Use them to fill out Trailers UI
            //trailersAdapter.setDataAndRefresh(data); --> null check 안해도됨. Null이면 length가 0이되도록 설정.
            for (String trailerKey : data) {
                Toast.makeText(this, trailerKey, Toast.LENGTH_SHORT).show();
            }
        } else {
            throw new RuntimeException("unimplemented");
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else return super.onOptionsItemSelected(item);
    }
}
