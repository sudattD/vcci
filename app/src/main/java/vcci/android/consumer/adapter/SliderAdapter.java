package vcci.android.consumer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import vcci.android.consumer.R;


public class SliderAdapter extends PagerAdapter {

    private final Context context;
    private final List<Integer> image;

    public SliderAdapter(Context context, List<Integer> image) {
        this.context = context;
        this.image = image;
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        ImageView iv_banner_img = view.findViewById(R.id.iv_banner_img);

        //linearLayout.setBackground(context.getResources().getDrawable(R.drawable.banner));
        iv_banner_img.setImageDrawable(context.getResources().getDrawable(R.drawable.banner));
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
