package com.ibphub.vcci_new.adapter.dashboard_ads_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.image_slider.ViewPagerActivity;
import com.ibphub.vcci_new.model.dashboard_news.BannersItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DashboardBannersSliderAdapter extends PagerAdapter {

    private Context context;
    private List<BannersItem> bannersItemList;

    public DashboardBannersSliderAdapter(Context context, List<BannersItem> bannersItemList) {
        this.context = context;
        this.bannersItemList = bannersItemList;
    }

    @Override
    public int getCount() {
        return bannersItemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        RelativeLayout linearLayout = view.findViewById(R.id.linearLayout);
        ImageView iv_banner_img = view.findViewById(R.id.iv_banner_img);
        TextView tv_news_title = view.findViewById(R.id.tv_news_title);
        tv_news_title.setVisibility(View.GONE);

        BannersItem bannersItem = bannersItemList.get(position);

        Picasso.get().load(bannersItem.getThumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(iv_banner_img);

        tv_news_title.setText(bannersItem.getTitle());

        iv_banner_img.setOnClickListener(v -> {
            Class clazz = ViewPagerActivity.class;
            Context context = view.getContext();
            Intent toFullScreenImageView = new Intent(context, clazz);
            ArrayList<String> myList = new ArrayList<>();
            for (BannersItem item : bannersItemList) {
                myList.add(item.getThumb());
            }
            toFullScreenImageView.putExtra("mylist", myList);
            toFullScreenImageView.putExtra("current_pos", position);
            toFullScreenImageView.putExtra("title", bannersItem.getTitle());
            context.startActivity(toFullScreenImageView);
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

}
