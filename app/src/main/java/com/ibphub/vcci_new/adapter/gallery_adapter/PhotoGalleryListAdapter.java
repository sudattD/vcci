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
import com.ibphub.vcci_new.model.gallery.photos.gallery_list.GalleryItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoGalleryListAdapter extends RecyclerView.Adapter<PhotoGalleryListAdapter.MyViewHolder> {

    private List<GalleryItem> galleryItemsList;
    private Context context;
    private ItemSelectionListener itemSelectionListener;

    public PhotoGalleryListAdapter(Context context, List<GalleryItem> galleryItemsList, ItemSelectionListener itemSelectionListener) {
        this.context = context;
        this.galleryItemsList = galleryItemsList;
        this.itemSelectionListener = itemSelectionListener;
    }

    @NonNull
    @Override
    public PhotoGalleryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoGalleryListAdapter.MyViewHolder holder, int position) {
        GalleryItem eventItem = galleryItemsList.get(position);

        Picasso.get().load(eventItem.getThumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(holder.iv_event);

        holder.tv_event_name.setText(eventItem.getTitle());
        holder.tv_event_date.setText(String.format("Event Date : %s", eventItem.getStartDate()));

        holder.card_view.setOnClickListener(v -> itemSelectionListener.onItemSelected(eventItem.getGalleryId(), "photos"));
    }

    @Override
    public int getItemCount() {
        if (galleryItemsList != null && galleryItemsList.size() > 0) {
            return galleryItemsList.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
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
