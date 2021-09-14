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
import com.ibphub.vcci_new.model.news_by_category.NewsItem;
import com.ibphub.vcci_new.util.Stash;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryWiseNewsAdapter extends RecyclerView.Adapter<CategoryWiseNewsAdapter.MyViewHolder> {

    private List<NewsItem> news;
    private Context context;
    private NewsSelectionListener newsSelectionListener;

    public CategoryWiseNewsAdapter(Context context, List<NewsItem> news, NewsSelectionListener newsSelectionListener) {
        this.context = context;
        this.news = news;
        this.newsSelectionListener = newsSelectionListener;
    }

    @NonNull
    @Override
    public CategoryWiseNewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_news_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryWiseNewsAdapter.MyViewHolder holder, int position) {
        NewsItem item = news.get(position);
        Picasso.get().load(item.getThumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(holder.iv_news_thumb);
        holder.tv_headline.setText(item.getTitle());

        holder.rl_news_item.setOnClickListener(v -> {
            Stash.put("news_type", "news_detail");
            newsSelectionListener.onNewsSelected(item.getNewsId());
        });
    }

    @Override
    public int getItemCount() {
        if (news != null && news.size() > 0) {
            return news.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_news_thumb)
        ImageView iv_news_thumb;

        @BindView(R.id.tv_headline)
        TextView tv_headline;

        @BindView(R.id.v_divider)
        View v_divider;
        @BindView(R.id.rl_news_item)
        RelativeLayout rl_news_item;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
