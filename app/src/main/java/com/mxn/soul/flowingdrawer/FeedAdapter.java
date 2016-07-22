package com.mxn.soul.flowingdrawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.heroliu.beans.News;
import com.heroliu.www.shark.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int itemsCount = 0;
    private List<News> lists;
    private OnItemClickListener onItemClickListener;

    public FeedAdapter(Context context,List<News> newsList) {
        this.context = context;
        lists = newsList;
    }

    public interface OnItemClickListener{
        public abstract void  onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new CellFeedViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
        bindDefaultFeedItem(position, holder);
    }

    private void bindDefaultFeedItem(final int position, final CellFeedViewHolder holder) {
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });
        }
        int avatarSize = context.getResources().getDimensionPixelSize(R.dimen.test_size);
        String url = lists.get(position).getImage();
        if(url.equals("http://www.heroliu.com/images/")){
            url = "http://www.heroliu.com/images/banner1.jpg";
        }
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .resize(avatarSize,avatarSize)
                .placeholder(R.drawable.img_circle_placeholder)
                .into(holder.ivFeedCenter);
        holder.tvFeedBottom.setText(lists.get(position).getTitle());
        holder.tvUserName.setText(lists.get(position).getAuthor());
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnLike.setImageResource(R.drawable.ic_heart_small_blue);
                Toast.makeText(context, "已加入收藏", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void updateItems() {
        itemsCount = lists.size();
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        return 1;
    }
    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder{
        ImageView ivFeedCenter;
        TextView tvFeedBottom;
        ImageButton btnComments;
        ImageButton btnLike;
        ImageButton btnMore;
        TextSwitcher tsLikesCounter;
        TextView tvUserName;
        FrameLayout vImageRoot;

        public CellFeedViewHolder(View view) {
            super(view);
            ivFeedCenter = (ImageView) view.findViewById(R.id.ivFeedCenter);
            tvFeedBottom = (TextView) view.findViewById(R.id.tvFeedBottom);
            btnComments = (ImageButton) view.findViewById(R.id.btnComments);
            btnLike = (ImageButton) view.findViewById(R.id.btnLike);
            btnMore = (ImageButton) view.findViewById(R.id.btnMore);
            tsLikesCounter = (TextSwitcher) view.findViewById(R.id.tsLikesCounter);
            tvUserName = (TextView) view.findViewById(R.id.tvUserName);
            vImageRoot = (FrameLayout) view.findViewById(R.id.vImageRoot);
        }
    }
}
