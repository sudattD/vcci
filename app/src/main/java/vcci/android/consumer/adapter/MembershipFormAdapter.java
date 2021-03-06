package vcci.android.consumer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import vcci.android.consumer.R;
import vcci.android.consumer.interfaces.ItemSelectionListener;
import vcci.android.consumer.model.membership_form.MembershipForm;

public class MembershipFormAdapter extends RecyclerView.Adapter<MembershipFormAdapter.MyViewHolder> {

    private final List<MembershipForm> membershipFormsList;
    private final Context context;
    private final ItemSelectionListener itemSelectionListener;

    public MembershipFormAdapter(Context context, List<MembershipForm> membershipFormsList, ItemSelectionListener itemSelectionListener) {
        this.context = context;
        this.membershipFormsList = membershipFormsList;
        this.itemSelectionListener = itemSelectionListener;
    }

    @NonNull
    @Override
    public MembershipFormAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membership_form_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MembershipFormAdapter.MyViewHolder holder, int position) {
        MembershipForm dataItem = membershipFormsList.get(position);
        holder.tv_form.setText(dataItem.getForms());

        holder.iv_view.setOnClickListener(v -> itemSelectionListener.onItemSelected("", dataItem.getView()));
    }

    @Override
    public int getItemCount() {
        if (membershipFormsList != null && membershipFormsList.size() > 0) {
            return membershipFormsList.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_form)
        TextView tv_form;

        @BindView(R.id.iv_view)
        ImageView iv_view;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


  //  Message data payload: {object_id=65, object_body=, object_type=news, object_title=News: Opportunity to export in Oman}
