package vcci.android.consumer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import vcci.android.consumer.R;
import vcci.android.consumer.activity.AdsSitesActivity;
import vcci.android.consumer.image_slider.ViewPagerActivity;
import vcci.android.consumer.model.newsDetail.LeftAdsItem;


public class NewsDetailLeftAdsSliderAdapter extends PagerAdapter {

    private final Context context;
    private final List<LeftAdsItem> bottomAdsItemList;

    public NewsDetailLeftAdsSliderAdapter(Context context, List<LeftAdsItem> bottomAdsItemList) {
        this.context = context;
        this.bottomAdsItemList = bottomAdsItemList;
    }

    @Override
    public int getCount() {
        return bottomAdsItemList.size();
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

        LeftAdsItem adItem = bottomAdsItemList.get(position);

        Picasso.get().load(adItem.getThumbImage()).placeholder(R.drawable.ibp_logo_splash)
                .error(R.drawable.ibp_logo_splash).into(iv_banner_img);

        tv_news_title.setText(adItem.getTitle());
        tv_news_title.setOnClickListener(v -> {
            //adsLinkSelectedListener.onAdsLinkSelected(adItem.getTitle(), adItem.getUrl());
            if (!"".equals(adItem.getUrl()) || !adItem.getUrl().isEmpty()) {
                Intent toDetails = new Intent(context, AdsSitesActivity.class);
                toDetails.putExtra("ads_site_name", adItem.getTitle());
                toDetails.putExtra("ads_url", adItem.getUrl());
                context.startActivity(toDetails);
            } else {
                Log.d("url", "instantiateItem: " + "Hello");
            }
        });

        iv_banner_img.setOnClickListener(v -> {
            Class clazz = ViewPagerActivity.class;
            Context context = view.getContext();
            Intent toFullScreenImageView = new Intent(context, clazz);
            ArrayList<String> myList = new ArrayList<>();
            for (LeftAdsItem item : bottomAdsItemList){
                myList.add(item.getPopupImage());
            }
            toFullScreenImageView.putExtra("mylist", myList);
            toFullScreenImageView.putExtra("current_pos", position);
            toFullScreenImageView.putExtra("title", adItem.getTitle());
            context.startActivity(toFullScreenImageView);
        });

       // Picasso.get().load(adItem.getUrl()).into(iv_banner_img);

        //linearLayout.setBackground(context.getResources().getDrawable(R.drawable.banner));
        //iv_banner_img.setImageDrawable(context.getResources().getDrawable(R.drawable.banner));
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
