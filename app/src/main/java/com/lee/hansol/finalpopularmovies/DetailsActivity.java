package com.lee.hansol.finalpopularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lee.hansol.finalpopularmovies.databinding.ActivityDetailsBinding;
import com.lee.hansol.finalpopularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding layout;

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
            Movie movie =  callingIntent.getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT);
            fillViewContentsWith(movie);
        } else {
            showErrorView();
        }
    }

    private void fillViewContentsWith(Movie movie) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else return super.onOptionsItemSelected(item);
    }
}
