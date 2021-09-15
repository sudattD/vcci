package vcci.android.consumer.adapter.dashboard_ads_adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import vcci.android.consumer.R;
import vcci.android.consumer.interfaces.NewsSelectionListener;
import vcci.android.consumer.model.dashboard_news.FeaturedNewsItem;


public class FeaturedNewsSliderAdapter extends PagerAdapter {

    private final Context context;
    private final List<FeaturedNewsItem> featuredNewsItemList;
    private final NewsSelectionListener newsSelectionListener;

    public FeaturedNewsSliderAdapter(Context context, List<FeaturedNewsItem> featuredNewsItemList, NewsSelectionListener newsSelectionListener) {
        this.context = context;
        this.featuredNewsItemList = featuredNewsItemList;
        this.newsSelectionListener = newsSelectionListener;
    }

    @Override
    public int getCount() {
        return featuredNewsItemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_featured_news_slider, null);

        RelativeLayout relativeLayout = view.findViewById(R.id.linearLayout);
        ImageView iv_banner_img = view.findViewById(R.id.iv_banner_img);
        TextView tv_news_title = view.findViewById(R.id.tv_news_title);

        FeaturedNewsItem featuredNewsItem = featuredNewsItemList.get(position);

        Picasso.get().load(featuredNewsItem.getThumb()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).into(iv_banner_img);

        tv_news_title.setText(featuredNewsItem.getTitle());
        Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(context).getAssets(), "fonts/OpenSans-Regular.ttf");
        tv_news_title.setTypeface(custom_font);

        relativeLayout.setOnClickListener(v -> newsSelectionListener.onNewsSelected(featuredNewsItem.getNewsId()));

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
