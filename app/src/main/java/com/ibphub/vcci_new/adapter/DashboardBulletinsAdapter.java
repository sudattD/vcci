package com.ibphub.vcci_new.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.interfaces.BulletinSelectedListener;
import com.ibphub.vcci_new.model.dashboard_news.BulletinsItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardBulletinsAdapter extends RecyclerView.Adapter<DashboardBulletinsAdapter.MyViewHolder> {

    private List<BulletinsItem> bulletinsItemList;
    private Context context;
    private BulletinSelectedListener bulletinSelectedListener;

    public DashboardBulletinsAdapter(Context context, List<BulletinsItem> bulletinsItemList,
                                     BulletinSelectedListener bulletinSelectedListener) {
        this.context = context;
        this.bulletinsItemList = bulletinsItemList;
        this.bulletinSelectedListener = bulletinSelectedListener;
    }

    @NonNull
    @Override
    public DashboardBulletinsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_bulletins_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardBulletinsAdapter.MyViewHolder holder, int position) {
        BulletinsItem item = bulletinsItemList.get(position);

        Picasso.get().load(item.getImage()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(holder.iv_bulletin_thumb);

        holder.tv_bulletins_title.setText(item.getTitle());
        Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(context).getAssets(), "fonts/OpenSans-Regular.ttf");
        holder.tv_bulletins_title.setTypeface(custom_font);

        holder.rl_bulletin.setOnClickListener(v -> bulletinSelectedListener.onBulletinSelected(item.getBulletinId(), item.getVideoUrl(), ""));

    }

    @Override
    public int getItemCount() {
        if (bulletinsItemList != null && bulletinsItemList.size() > 0) {
            return bulletinsItemList.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_bulletin_thumb)
        ImageView iv_bulletin_thumb;

        @BindView(R.id.tv_bulletins_title)
        TextView tv_bulletins_title;

        @BindView(R.id.rl_bulletin)
        RelativeLayout rl_bulletin;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
