package com.lee.hansol.finalpopularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lee.hansol.finalpopularmovies.R;
import com.lee.hansol.finalpopularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemHolder> {
    private Context context;
    private Movie[] movies;
    private final OnMovieItemClickListener itemClickListener;

    public interface OnMovieItemClickListener {
        void onItemClick(Movie movie);
    }

    public MovieListAdapter(OnMovieItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MovieItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View holderView = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
        return new MovieItemHolder(holderView);
    }

    @Override
    public void onBindViewHolder(MovieItemHolder holder, int position) {
        Movie movie = movies[position];
        Picasso.with(context).load(movie.thumbnailPath).into(holder.thumbnailImageView);
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        else return movies.length;
    }

    public void setMoviesAndRefresh(Movie[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    class MovieItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnailImageView;

        MovieItemHolder(View view) {
            super(view);
            thumbnailImageView = (ImageView)view.findViewById(R.id.movie_list_item_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(movies[getAdapterPosition()]);
        }
    }
}
