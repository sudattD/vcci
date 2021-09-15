package com.ibphub.vcci_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.model.committee.CommitteeDataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommitteeListAdapter extends RecyclerView.Adapter<CommitteeListAdapter.MyViewHolder> {

    private List<CommitteeDataItem> committeeDataItems;
    private Context context;

    public CommitteeListAdapter(Context context, List<CommitteeDataItem> committeeDataItems) {
        this.context = context;
        this.committeeDataItems = committeeDataItems;
    }

    @NonNull
    @Override
    public CommitteeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_committee_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommitteeListAdapter.MyViewHolder holder, int position) {
        CommitteeDataItem dataItem = committeeDataItems.get(position);
        holder.tv_sr_no.setText(dataItem.getSrNo());
        holder.tv_name.setText(dataItem.getName());
        holder.tv_company_name.setText(dataItem.getCompanyName());
        holder.tv_mobile_no.setText(dataItem.getMobile());
        holder.tv_phone.setText(dataItem.getPhone());
        holder.tv_phone_r.setText(dataItem.getPhone1());
        holder.tv_email.setText(dataItem.getEmail());

    }

    @Override
    public int getItemCount() {
        if (committeeDataItems != null && committeeDataItems.size() > 0) {
            return committeeDataItems.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_sr_no)
        TextView tv_sr_no;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_company_name)
        TextView tv_company_name;

        @BindView(R.id.tv_mobile_no)
        TextView tv_mobile_no;

        @BindView(R.id.tv_phone)
        TextView tv_phone;

        @BindView(R.id.tv_phone_r)
        TextView tv_phone_r;

        @BindView(R.id.tv_email)
        TextView tv_email;


        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
