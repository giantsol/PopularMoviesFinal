package com.lee.hansol.finalpopularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lee.hansol.finalpopularmovies.R;

import java.util.Locale;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewItemHolder> {
    private Context context;
    private String[] reviews;

    @Override
    public ReviewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View holderView = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);
        return new ReviewItemHolder(holderView);
    }

    @Override
    public void onBindViewHolder(ReviewItemHolder holder, int position) {
        holder.content.setText(reviews[position]);
    }

    @Override
    public int getItemCount() {
        if (reviews == null) return 0;
        else return reviews.length;
    }

    public void setReviewsAndRefresh(String[] reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewItemHolder extends RecyclerView.ViewHolder {
        TextView content;

        ReviewItemHolder(View view) {
            super(view);
            content = (TextView) view.findViewById(R.id.review_list_item_content);
        }
    }
}

