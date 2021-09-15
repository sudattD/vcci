package vcci.android.consumer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import vcci.android.consumer.R;
import vcci.android.consumer.interfaces.BulletinSelectedListener;
import vcci.android.consumer.model.bulletin.BulletinsItem;

public class BulletinsAdapter extends RecyclerView.Adapter<BulletinsAdapter.MyViewHolder> {

    private final List<BulletinsItem> bulletinsItemList;
    private final Context context;
    private final BulletinSelectedListener bulletinSelectedListener;

    public BulletinsAdapter(Context context, List<BulletinsItem> bulletinsItemList,
                            BulletinSelectedListener bulletinSelectedListener) {
        this.context = context;
        this.bulletinsItemList = bulletinsItemList;
        this.bulletinSelectedListener = bulletinSelectedListener;
    }

    @NonNull
    @Override
    public BulletinsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_bulletins_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BulletinsAdapter.MyViewHolder holder, int position) {
        BulletinsItem item = bulletinsItemList.get(position);

        Picasso.get().load(item.getImage()).placeholder(R.drawable.ibp_logo_splash)
                .error(R.drawable.ibp_logo_splash).into(holder.iv_bulletin_thumb);

        holder.tv_bulletins_title.setText(item.getTitle());

        holder.rl_bulletin.setOnClickListener(v -> bulletinSelectedListener.onBulletinSelected(item.getBulletinId(), item.getVideoUrl(), item.getYoutube_id()));
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
