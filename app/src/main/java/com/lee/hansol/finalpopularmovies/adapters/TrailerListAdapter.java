package com.lee.hansol.finalpopularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lee.hansol.finalpopularmovies.R;

import java.util.Locale;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerItemHolder> {
    private Context context;
    private String[] trailerKeys;
    private final OnTrailerItemClickListener itemClickListener;
    private final String titleTextFormat = "Trailer %d";

    public interface OnTrailerItemClickListener {
        void onTrailerItemClick(String trailerKey);
    }

    public TrailerListAdapter(OnTrailerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public TrailerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View holderView = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerItemHolder(holderView);
    }

    @Override
    public void onBindViewHolder(TrailerItemHolder holder, int position) {
        holder.trailerTitleText.setText(String.format(Locale.getDefault() ,titleTextFormat, position+1));
    }

    @Override
    public int getItemCount() {
        if (trailerKeys == null) return 0;
        else return trailerKeys.length;
    }

    public void setTrailerKeysAndRefresh(String[] trailerKeys) {
        this.trailerKeys = trailerKeys;
        notifyDataSetChanged();
    }

    class TrailerItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailerTitleText;

        TrailerItemHolder(View view) {
            super(view);
            trailerTitleText = (TextView) view.findViewById(R.id.trailer_list_item_title_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onTrailerItemClick(trailerKeys[getAdapterPosition()]);
        }
    }
}

