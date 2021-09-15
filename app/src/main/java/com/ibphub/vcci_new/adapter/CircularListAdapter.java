package com.ibphub.vcci_new.adapter;

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
import com.ibphub.vcci_new.model.circulars.circular_list.DataItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CircularListAdapter extends RecyclerView.Adapter<CircularListAdapter.MyViewHolder> {

    private List<DataItem> eventItemsList;
    private Context context;
    private ItemSelectionListener itemSelectionListener;

    public CircularListAdapter(Context context, List<DataItem> eventItemsList, ItemSelectionListener itemSelectionListener) {
        this.context = context;
        this.eventItemsList = eventItemsList;
        this.itemSelectionListener = itemSelectionListener;
    }

    @NonNull
    @Override
    public CircularListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_circular_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CircularListAdapter.MyViewHolder holder, int position) {
        DataItem eventItem = eventItemsList.get(position);

        Picasso.get().load(eventItem.getThumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(holder.iv_event);

        holder.tv_event_name.setText(eventItem.getTitle());
        holder.tv_event_date.setText(String.format("%s %s", "Date : ", eventItem.getStartDate()));

        holder.card_view.setOnClickListener(v -> itemSelectionListener.onItemSelected(eventItem.getEventId(), eventItem.getTitle()));
    }

    @Override
    public int getItemCount() {
        if (eventItemsList != null && eventItemsList.size() > 0) {
            return eventItemsList.size();
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
