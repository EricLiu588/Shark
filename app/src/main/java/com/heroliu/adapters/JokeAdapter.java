package com.heroliu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heroliu.www.shark.R;

import java.util.List;

/**
 * Created by Eric on 6/22/16.
 */
public class JokeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> lists;
    private int itemsCount = 0;

    public JokeAdapter(Context context,List<String> lists){
        this.context = context;
        this.lists = lists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.joke_feed, parent, false);
        return new CellFeedViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void updateItems() {
        itemsCount = lists.size();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
        bindDefaultFeedItem(position, holder);
    }

    private void bindDefaultFeedItem(final int position, final CellFeedViewHolder holder) {
        holder.tvJokeCenter.setText(lists.get(position));
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder{
        TextView tvJokeCenter;
        public CellFeedViewHolder(View view) {
            super(view);
            tvJokeCenter = (TextView) view.findViewById(R.id.tvJokeCenter);
        }
    }
}
