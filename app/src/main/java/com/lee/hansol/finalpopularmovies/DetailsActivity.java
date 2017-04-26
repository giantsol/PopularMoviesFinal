package com.lee.hansol.finalpopularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lee.hansol.finalpopularmovies.adapters.TrailerListAdapter;
import com.lee.hansol.finalpopularmovies.asynctaskloaders.MovieReviewsAsyncTaskLoader;
import com.lee.hansol.finalpopularmovies.asynctaskloaders.MovieTrailersAsyncTaskLoader;
import com.lee.hansol.finalpopularmovies.databinding.ActivityDetailsBinding;
import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.UriUtils;
import com.squareup.picasso.Picasso;

import static com.lee.hansol.finalpopularmovies.utils.ToastUtils.toast;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>,
            TrailerListAdapter.OnTrailerItemClickListener {
    private ActivityDetailsBinding layout;
    private TrailerListAdapter trailerListAdapter;

    private final int LOADER_LOAD_TRAILERS_ID = 156;
    private final int LOADER_LOAD_REVIEWS_ID = 157;

    private final String BUNDLE_MOVIE_ID_KEY = "movie-id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        initialize();
    }

    private void initialize() {
        layout.activityDetailsTrailersRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layout.activityDetailsTrailersRecyclerView.setLayoutManager(linearLayoutManager);
        trailerListAdapter = new TrailerListAdapter(this);
        layout.activityDetailsTrailersRecyclerView.setAdapter(trailerListAdapter);

        setupActionBar();
        setupViewContents();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewContents() {
        Intent callingIntent = getIntent();
        if (receivedMovieObjectSuccessfullyFrom(callingIntent)) fillContentsWith(getMovieObjectFromIntent(callingIntent));
        else showErrorView();
    }

    private boolean receivedMovieObjectSuccessfullyFrom(Intent callingIntent) {
        return callingIntent != null && callingIntent.hasExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT);
    }

    private Movie getMovieObjectFromIntent(Intent intent) {
        return intent.getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT);
    }

    private void fillContentsWith(Movie movie) {
        fillBasicContents(movie);
        loadMovieTrailers(movie.movieId);
        loadMovieReviews(movie.movieId);
    }

    private void fillBasicContents(Movie movie) {
        layout.activityDetailsContainer.setVisibility(View.VISIBLE);
        layout.activityDetailsErrorView.setVisibility(View.INVISIBLE);
        layout.activityDetailsTitle.setText(movie.title);
        Picasso.with(this).load(movie.thumbnailPath).into(layout.activityDetailsThumbnail);
        layout.activityDetailsDate.setText(movie.date);
        layout.activityDetailsRating.setText(movie.rating);
        layout.activityDetailsOverview.setText(movie.overview);
    }

    private void loadMovieTrailers(int movieId) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_MOVIE_ID_KEY, movieId);
        getSupportLoaderManager().initLoader(LOADER_LOAD_TRAILERS_ID, args, this);
    }

    private void loadMovieReviews(int movieId) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_MOVIE_ID_KEY, movieId);
        getSupportLoaderManager().initLoader(LOADER_LOAD_REVIEWS_ID, args, this);
    }

    private void showErrorView() {
        layout.activityDetailsContainer.setVisibility(View.INVISIBLE);
        layout.activityDetailsErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        int movieId = args.getInt(BUNDLE_MOVIE_ID_KEY);

        if (id == LOADER_LOAD_TRAILERS_ID) {
            return new MovieTrailersAsyncTaskLoader(this, movieId);
        } else if (id == LOADER_LOAD_REVIEWS_ID) {
            return new MovieReviewsAsyncTaskLoader(this, movieId);
        } else {
            throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        int loaderId = loader.getId();
        if (loaderId == LOADER_LOAD_TRAILERS_ID) {
            trailerListAdapter.setTrailerKeysAndRefresh(data);
        } else if (loaderId == LOADER_LOAD_REVIEWS_ID){
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {
        loader.cancelLoad();
    }

    @Override
    public void onTrailerItemClick(String trailerKey) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
        youtubeIntent.setData(UriUtils.getYoutubeVideoUri(this, trailerKey));
        startActivity(youtubeIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else return super.onOptionsItemSelected(item);
    }
}
