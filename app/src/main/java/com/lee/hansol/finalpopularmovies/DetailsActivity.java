package com.lee.hansol.finalpopularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lee.hansol.finalpopularmovies.adapters.ReviewListAdapter;
import com.lee.hansol.finalpopularmovies.adapters.TrailerListAdapter;
import com.lee.hansol.finalpopularmovies.asynctaskloaders.MovieReviewsAsyncTaskLoader;
import com.lee.hansol.finalpopularmovies.asynctaskloaders.MovieTrailersAsyncTaskLoader;
import com.lee.hansol.finalpopularmovies.databinding.ActivityDetailsBinding;
import com.lee.hansol.finalpopularmovies.helpers.MovieContract;
import com.lee.hansol.finalpopularmovies.models.Movie;
import com.lee.hansol.finalpopularmovies.utils.UriUtils;
import com.squareup.picasso.Picasso;

import static android.R.attr.id;
import static com.lee.hansol.finalpopularmovies.utils.ToastUtils.toast;

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String[]>,
        TrailerListAdapter.OnTrailerItemClickListener {
    private ActivityDetailsBinding layout;
    private TrailerListAdapter trailerListAdapter;
    private ReviewListAdapter reviewListAdapter;
    private MenuItem favoriteButton;
    private Movie movie;

    private final int LOADER_LOAD_TRAILERS_ID = 156;
    private final int LOADER_LOAD_REVIEWS_ID = 157;

    private final String BUNDLE_MOVIE_ID_KEY = "movie-id";

    private boolean isFavoriteMovie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        initialize();
    }

    private void initialize() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layout.activityDetailsTrailersRecyclerView.setHasFixedSize(true);
        layout.activityDetailsTrailersRecyclerView.setLayoutManager(linearLayoutManager);
        trailerListAdapter = new TrailerListAdapter(this);
        layout.activityDetailsTrailersRecyclerView.setAdapter(trailerListAdapter);

        RecyclerView.LayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        layout.activityDetailsReviewsRecyclerView.setHasFixedSize(true);
        layout.activityDetailsReviewsRecyclerView.setLayoutManager(linearLayoutManager2);
        reviewListAdapter = new ReviewListAdapter();
        layout.activityDetailsReviewsRecyclerView.setAdapter(reviewListAdapter);

        setupActionBar();
        setupViewContents();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewContents() {
        Intent callingIntent = getIntent();
        movie = getMovieObjectFromIntent(callingIntent);
        if (movie != null) fillContentsWith(movie);
        else showErrorView();
    }

    private Movie getMovieObjectFromIntent(Intent intent) {
        if (hasMovieObject(intent)) return intent.getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT);
        else return null;
    }

    private boolean hasMovieObject(Intent intent) {
        return intent != null && intent.hasExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT);
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
            reviewListAdapter.setReviewsAndRefresh(data);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_details, menu);
        favoriteButton = menu.findItem(R.id.activity_details_menu_favorite);

        if (isFavoriteMovie) favoriteButton.setIcon(R.drawable.ic_star_filled);
        else favoriteButton.setIcon(R.drawable.ic_star_empty);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Cursor cursor = getContentResolver().query(MovieContract.FavoriteMovieEntry.CONTENT_URI, new String[]{MovieContract.FavoriteMovieEntry.COLUMN_TITLE},
                    null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Toast.makeText(this, cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_TITLE)),Toast.LENGTH_SHORT).show();
                    } while (cursor.moveToNext());
                }

            }
            NavUtils.navigateUpFromSameTask(this);
        } else if (id == R.id.activity_details_menu_favorite) {
            if (movie == null) return false;

            if (isFavoriteMovie) defavoriteThisMovie();
            else favoriteThisMovie();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void defavoriteThisMovie() {
        favoriteButton.setEnabled(false);
        //do thing about contentresolver
        getContentResolver().delete(MovieContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.movieId)).build(), null, null);

        //and then change
        toast(this, getString(R.string.message_defavorited));
        isFavoriteMovie = false;
        favoriteButton.setIcon(R.drawable.ic_star_empty);
        favoriteButton.setEnabled(true);
    }

    private void favoriteThisMovie() {
        favoriteButton.setEnabled(false);
        //do thing about contentresolver
        ContentValues values = new ContentValues();
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.movieId);
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_TITLE, movie.title);
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_THUMBNAIL_PATH, movie.thumbnailPath);
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_DATE, movie.date);
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_RATING, movie.rating);
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, movie.overview);
        getContentResolver().insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, values);

        //and then change
        toast(this, getString(R.string.message_favorited));
        isFavoriteMovie = true;
        favoriteButton.setIcon(R.drawable.ic_star_filled);
        favoriteButton.setEnabled(true);
    }
}
