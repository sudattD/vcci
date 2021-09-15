package com.ibphub.vcci_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.interfaces.NewsSelectionListener;
import com.ibphub.vcci_new.model.newsDetail.RelatedNewsItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelatedAdsFromNewsDetailAdapter extends RecyclerView.Adapter<RelatedAdsFromNewsDetailAdapter.MyViewHolder> {

    private List<RelatedNewsItem> relatedNewsItems;
    private Context context;
    private NewsSelectionListener newsSelectionListener;

    public RelatedAdsFromNewsDetailAdapter(Context context, List<RelatedNewsItem> relatedNewsItems,
                                           NewsSelectionListener newsSelectionListener) {
        this.context = context;
        this.relatedNewsItems = relatedNewsItems;
        this.newsSelectionListener = newsSelectionListener;
    }

    @NonNull
    @Override
    public RelatedAdsFromNewsDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_gcci_trending_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedAdsFromNewsDetailAdapter.MyViewHolder holder, int position) {
        RelatedNewsItem item = relatedNewsItems.get(position);

        Picasso.get().load(item.getThumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(holder.iv_bulletin_thumb);

        holder.tv_bulletins_title.setText(item.getTitle());
        holder.rl_gcci_trending.setOnClickListener(v -> newsSelectionListener.onNewsSelected(item.getNewsId()));


    }

    @Override
    public int getItemCount() {
        if (relatedNewsItems != null && relatedNewsItems.size() > 0) {
            return relatedNewsItems.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_bulletin_thumb)
        ImageView iv_bulletin_thumb;

        @BindView(R.id.tv_bulletins_title)
        TextView tv_bulletins_title;

        @BindView(R.id.rl_gcci_trending)
        RelativeLayout rl_gcci_trending;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
