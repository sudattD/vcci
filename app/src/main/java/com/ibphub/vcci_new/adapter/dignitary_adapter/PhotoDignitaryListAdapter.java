package com.ibphub.vcci_new.adapter.dignitary_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.image_slider.ViewPagerActivity;
import com.ibphub.vcci_new.interfaces.ItemSelectionListener;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_photos.DataItemDignitaryPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoDignitaryListAdapter extends RecyclerView.Adapter<PhotoDignitaryListAdapter.MyViewHolder> {

    private List<DataItemDignitaryPhoto> galleryItemsList;
    private Context context;
    private ItemSelectionListener itemSelectionListener;

    public PhotoDignitaryListAdapter(Context context, List<DataItemDignitaryPhoto> galleryItemsList, ItemSelectionListener itemSelectionListener) {
        this.context = context;
        this.galleryItemsList = galleryItemsList;
        this.itemSelectionListener = itemSelectionListener;
    }

    @NonNull
    @Override
    public PhotoDignitaryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoDignitaryListAdapter.MyViewHolder holder, int position) {
        DataItemDignitaryPhoto eventItem = galleryItemsList.get(position);

        Picasso.get().load(eventItem.getThumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(holder.iv_event);

        holder.tv_event_name.setText(eventItem.getTitle());
        holder.tv_event_date.setVisibility(View.GONE);

        holder.card_view.setOnClickListener(v -> {
            Class clazz = ViewPagerActivity.class;
            Intent toFullScreenImageView = new Intent(context, clazz);
            ArrayList<String> myList = new ArrayList<>();
            for (DataItemDignitaryPhoto item : galleryItemsList) {
                myList.add(item.getPopup());
            }
            toFullScreenImageView.putExtra("mylist", myList);
            toFullScreenImageView.putExtra("current_pos", position);
            toFullScreenImageView.putExtra("title", "");
            context.startActivity(toFullScreenImageView);
        });
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
