package com.ibphub.vcci_new.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.interfaces.CategorySelectionListener;
import com.ibphub.vcci_new.model.category.CategoriesItem;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    private List<CategoriesItem> myCategory;
    private Context context;
    private CategorySelectionListener categorySelectionListener;

    public CategoriesAdapter(Context context, List<CategoriesItem> myCategory, CategorySelectionListener categorySelectionListener) {
        this.context = context;
        this.myCategory = myCategory;
        this.categorySelectionListener = categorySelectionListener;
    }

    @NonNull
    @Override
    public CategoriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_name, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.MyViewHolder holder, int position) {
        CategoriesItem categoriesItem = myCategory.get(position);
        if (categoriesItem.getId() == -1) {
            holder.tv_catgeory.setText(context.getResources().getString(R.string.select_category));
            holder.tv_catgeory.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tv_catgeory.setTextColor(Color.BLACK);
            holder.tv_catgeory.setGravity(Gravity.CENTER);
            holder.rl_category.setBackgroundColor(Color.WHITE);
            holder.v_border.setVisibility(View.GONE);
        } else {
            holder.tv_catgeory.setText(categoriesItem.getTitle());
            holder.tv_catgeory.setTextColor(Color.WHITE);
            holder.tv_catgeory.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            //holder.rl_category.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            holder.v_border.setVisibility(View.VISIBLE);
        }

        holder.rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoriesItem.getId() != -1) {
                    categorySelectionListener.onCategorySelected(categoriesItem.getId(), categoriesItem.getTitle());
                }

            }
        });

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
        private View v_border;

        MyViewHolder(View view) {
            super(view);
            tv_catgeory = view.findViewById(R.id.tv_catgeory);
            rl_category = view.findViewById(R.id.rl_category);
            v_border = view.findViewById(R.id.v_border);
        }
    }
}
