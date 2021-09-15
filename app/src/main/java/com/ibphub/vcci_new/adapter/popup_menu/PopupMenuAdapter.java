package com.ibphub.vcci_new.adapter.popup_menu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.interfaces.OtherMenuSelectionListener;

import java.util.List;
import java.util.Objects;


public class PopupMenuAdapter extends RecyclerView.Adapter<PopupMenuAdapter.MyViewHolder> {

    private List<String> myCategory;
    private Context context;
    private OtherMenuSelectionListener otherMenuSelectionListener;

    public PopupMenuAdapter(Context context, List<String> myCategory, OtherMenuSelectionListener otherMenuSelectionListener) {
        this.context = context;
        this.myCategory = myCategory;
        this.otherMenuSelectionListener = otherMenuSelectionListener;
    }

    @NonNull
    @Override
    public PopupMenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_name, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PopupMenuAdapter.MyViewHolder holder, int position) {
        holder.tv_catgeory.setText(myCategory.get(position));
        Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(context).getAssets(), "fonts/OpenSans-Bold.ttf");
        holder.tv_catgeory.setTypeface(custom_font);

        holder.rl_category.setOnClickListener(v -> otherMenuSelectionListener.onMenuSelected(position + 1, myCategory.get(position)));
    }

    @Override
    public int getItemCount() {
        if (myCategory.size() > 0) {
            return myCategory.size();
        } else {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_catgeory;
        private RelativeLayout rl_category;

        MyViewHolder(View view) {
            super(view);
            tv_catgeory = view.findViewById(R.id.tv_catgeory);
            rl_category = view.findViewById(R.id.rl_category);
        }
    }
}
