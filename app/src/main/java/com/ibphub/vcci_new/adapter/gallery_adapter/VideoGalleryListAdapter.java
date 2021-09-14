package com.ibphub.vcci_new.adapter.gallery_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.interfaces.ItemSelectionListener;
import com.ibphub.vcci_new.model.gallery.videos.video_list.VideoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoGalleryListAdapter extends RecyclerView.Adapter<VideoGalleryListAdapter.MyViewHolder> {

    private List<VideoItem> galleryItemList;
    private Context context;
    private ItemSelectionListener itemSelectionListener;

    public VideoGalleryListAdapter(Context context, List<VideoItem> galleryItemsList, ItemSelectionListener itemSelectionListener) {
        this.context = context;
        this.galleryItemList = galleryItemsList;
        this.itemSelectionListener = itemSelectionListener;
    }

    @NonNull
    @Override
    public VideoGalleryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_video_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoGalleryListAdapter.MyViewHolder holder, int position) {
        VideoItem videoItem = galleryItemList.get(position);

        Picasso.get()
                .load("http://img.youtube.com/vi/" + videoItem.getYoutubeId() + "/hqdefault.jpg").placeholder(R.drawable.ic_placeholder)
                .into(holder.iv_event);

        holder.tv_event_name.setText(videoItem.getTitle());

        holder.tv_event_date.setText("");

        holder.card_view.setOnClickListener(v -> itemSelectionListener.onItemSelected(videoItem.getYoutubeId(), "videos"));
    }

    @Override
    public int getItemCount() {
        if (galleryItemList != null && galleryItemList.size() > 0) {
            return galleryItemList.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
       /* @BindView(R.id.iv_video)
        ImageView iv_video;

        @BindView(R.id.tv_video_title)
        TextView tv_video_title;

        @BindView(R.id.card_view)
        CardView card_view;*/

        @BindView(R.id.iv_event)
        ImageView iv_event;

        @BindView(R.id.tv_event_name)
        TextView tv_event_name;

        @BindView(R.id.tv_event_date)
        TextView tv_event_date;

        @BindView(R.id.card_view)
        CardView card_view;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
