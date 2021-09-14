/*
 Copyright 2011, 2012 Chris Banes.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.ibphub.vcci_new.image_slider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.PhotoView;
import com.ibphub.vcci_new.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    public List<String> myList;
    public List<String> titles;
    public int current_pos = 0;

    private ImageView iv_cancel;
    private TextView tv_title;

    private String title = "";
    private boolean from_notification = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iv_cancel = findViewById(R.id.iv_cancel);
        tv_title = findViewById(R.id.tv_title);
        ViewPager viewPager = findViewById(R.id.view_pager);
        myList = (List<String>) getIntent().getSerializableExtra("mylist");
        // titles = (List<String>) getIntent().getSerializableExtra("titles");
        current_pos = getIntent().getIntExtra("current_pos", 0);
        /*title = getIntent().getStringExtra("title");
        tv_title.setText(title);*/

        viewPager.setAdapter(new SamplePagerAdapter(myList));
        viewPager.setCurrentItem(current_pos, true);

        from_notification = getIntent().getBooleanExtra("from_notification", false);

        iv_cancel.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class SamplePagerAdapter extends PagerAdapter {

        List<String> myList;

        /*private static final int[] sDrawables = {R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
            R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper};*/

        public SamplePagerAdapter(List<String> myList) {
            this.myList = myList;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Picasso.get().load(myList.get(position)).into(photoView);
            /*photoView.setImageResource(sDrawables[position]);*/
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
