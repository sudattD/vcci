package com.ibphub.vcci_new.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.image_slider.ViewPagerActivity;
import com.ibphub.vcci_new.model.DetailPhotos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {

    private List<DetailPhotos> detailPhotos;
    private Context context;

    public PhotosAdapter(Context context, List<DetailPhotos> detailPhotos) {
        this.context = context;
        this.detailPhotos = detailPhotos;
    }

    @NonNull
    @Override
    public PhotosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_photo_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.MyViewHolder holder, int position) {
        DetailPhotos item = detailPhotos.get(position);

        Picasso.get().load(item.getGallery_thumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ibp_logo_splash).into(holder.iv_photo);

        holder.iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class clazz = ViewPagerActivity.class;
                Intent toFullScreenImageView = new Intent(context, clazz);
                ArrayList<String> myList = new ArrayList<>();
                for (DetailPhotos item : detailPhotos) {
                    myList.add(item.getGallery_image());
                }
                toFullScreenImageView.putExtra("mylist", myList);
                toFullScreenImageView.putExtra("current_pos", position);
                toFullScreenImageView.putExtra("title", "");
                context.startActivity(toFullScreenImageView);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (detailPhotos != null && detailPhotos.size() > 0) {
            return detailPhotos.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView iv_photo;


        @BindView(R.id.rl_bulletin)
        RelativeLayout rl_bulletin;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
