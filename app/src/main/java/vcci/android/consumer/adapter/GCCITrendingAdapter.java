package vcci.android.consumer.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import vcci.android.consumer.R;
import vcci.android.consumer.interfaces.NewsSelectionListener;
import vcci.android.consumer.model.dashboard_news.TrendingNewsItem;

public class GCCITrendingAdapter extends RecyclerView.Adapter<GCCITrendingAdapter.MyViewHolder> {

    private final List<TrendingNewsItem> trendingNewsItems;
    private final Context context;
    private final NewsSelectionListener newsSelectionListener;

    public GCCITrendingAdapter(Context context, List<TrendingNewsItem> trendingNewsItems,  NewsSelectionListener newsSelectionListener) {
        this.context = context;
        this.trendingNewsItems = trendingNewsItems;
        this.newsSelectionListener = newsSelectionListener;
    }

    @NonNull
    @Override
    public GCCITrendingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_gcci_trending_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GCCITrendingAdapter.MyViewHolder holder, int position) {
        TrendingNewsItem item = trendingNewsItems.get(position);

        Picasso.get().load(item.getThumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(holder.iv_bulletin_thumb);

        holder.tv_bulletins_title.setText(item.getTitle());
        Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(context).getAssets(), "fonts/OpenSans-Regular.ttf");
        holder.tv_bulletins_title.setTypeface(custom_font);

        holder.rl_gcci_trending.setOnClickListener(v -> newsSelectionListener.onNewsSelected(item.getNewsId()));

    }

    @Override
    public int getItemCount() {
        if (trendingNewsItems != null && trendingNewsItems.size() > 0) {
            return trendingNewsItems.size();
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
