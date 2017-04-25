package com.lee.hansol.finalpopularmovies;

import android.databinding.DataBindingUtil;
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

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnMovieItemClickListener {
    private ActivityMainBinding mainLayout;
    private Toast toast;

    private final int GRID_COL_NUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainLayout.activityMainRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_COL_NUM, LinearLayoutManager.VERTICAL, false);
        mainLayout.activityMainRecyclerView.setLayoutManager(gridLayoutManager);
        MovieListAdapter recyclerViewAdapter = new MovieListAdapter(this);
        mainLayout.activityMainRecyclerView.setAdapter(recyclerViewAdapter);
        mainLayout.activityMainErrorView.setVisibility(View.INVISIBLE);
        mainLayout.activityMainProgressBar.setVisibility(View.INVISIBLE);

        recyclerViewAdapter.setMoviesAndRefresh(FakeDataUtils.getFakeMoviesData());
    }

    @Override
    public void onItemClick(Movie movie) {
        //TODO: Go to DetailsActivity with the given movie
        if (toast != null) toast.cancel();
        toast = Toast.makeText(this, movie.title, Toast.LENGTH_SHORT);
        toast.show();
    }
}
