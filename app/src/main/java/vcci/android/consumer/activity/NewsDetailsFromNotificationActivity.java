package vcci.android.consumer.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vcci.android.consumer.R;
import vcci.android.consumer.adapter.NewsDetailBottomAdsSliderAdapter;
import vcci.android.consumer.adapter.NewsDetailLeftAdsSliderAdapter;
import vcci.android.consumer.adapter.NewsDetailRightAdsSliderAdapter;
import vcci.android.consumer.adapter.RelatedAdsFromNewsDetailAdapter;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.interfaces.NewsSelectionListener;
import vcci.android.consumer.model.newsDetail.BottomAdsItem;
import vcci.android.consumer.model.newsDetail.LeftAdsItem;
import vcci.android.consumer.model.newsDetail.NewsDetailResponse;
import vcci.android.consumer.model.newsDetail.NewsDetails;
import vcci.android.consumer.model.newsDetail.RelatedNewsItem;
import vcci.android.consumer.model.newsDetail.RightAdsItem;
import vcci.android.consumer.util.NetworkConstants;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewsDetailsFromNotificationActivity extends BaseActivity implements NewsSelectionListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;

    @BindView(R.id.tv_news_title)
    TextView tv_news_title;

    @BindView(R.id.tv_date_views)
    TextView tv_date_views;

    @BindView(R.id.iv_news)
    ImageView iv_news;

    @BindView(R.id.wv_description)
    WebView wv_description;

    /*@BindView(R.id.tv_news_detail)
    TextView tv_news_detail;*/

    @BindView(R.id.rv_related_news)
    RecyclerView rv_related_news;

    @BindView(R.id.avt_bottom)
    ViewPager avt_bottom;

    @BindView(R.id.indicator_avt_bottom)
    TabLayout indicator_avt_bottom;

    @BindView(R.id.avt_right)
    ViewPager avt_right;

    @BindView(R.id.indicator_avt_right)
    TabLayout indicator_avt_right;

    @BindView(R.id.avt_left)
    ViewPager avt_left;

    @BindView(R.id.indicator_avt_left)
    TabLayout indicator_avt_left;

    @BindView(R.id.rl_previous)
    RelativeLayout rl_previous;

    @BindView(R.id.iv_previous_news)
    ImageView iv_previous_news;

    @BindView(R.id.tv_prev_title)
    TextView tv_prev_title;

    @BindView(R.id.rl_next)
    RelativeLayout rl_next;

    @BindView(R.id.iv_next_news)
    ImageView iv_next_news;

    @BindView(R.id.tv_next_title)
    TextView tv_next_title;

    private String news_id = "";
    private String prev_news_id = "";
    private String next_news_id = "";
    private String str_share_news_link = "";

    @BindView(R.id.iv_share)
    ImageView iv_share;

    @BindView(R.id.tv_next)
    TextView tv_next;

    private RelatedAdsFromNewsDetailAdapter relatedAdsFromNewsDetailAdapter;

    private boolean from_notification = false;

    private List<BottomAdsItem> bottomAdsItems;
    private List<RightAdsItem> rightAdsItems;
    private List<LeftAdsItem> leftAdsItems;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_from_notification);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTitle.setText(getResources().getString(R.string.txt_news_detail));
        String getID = getIntent().getStringExtra("news_id");
        Log.d("getID", "onCreate: " + getID);
        news_id = getIntent().getStringExtra("news_id");

        from_notification = getIntent().getBooleanExtra("from_notification", false);

        getNewsDetail(news_id);
    }

    @OnClick(R.id.rl_next)
    void onNextNewsClick() {
        if (!next_news_id.isEmpty()) {
            getNewsDetail(next_news_id);
        }
    }

    @OnClick(R.id.rl_previous)
    void onpreviuosNewsClick() {
        if (!prev_news_id.isEmpty()) {
            getNewsDetail(prev_news_id);
        }
    }

    @OnClick(R.id.iv_share)
    void onShareNewsClicked() {
        /*Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str_share_news_link);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);*/

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, str_share_news_link);
        startActivity(Intent.createChooser(share, "Share link!"));
    }

    private void getNewsDetail(String news_id) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<NewsDetailResponse> call = apiService.getNewsDetail(NetworkConstants.version, Configuration.DEVICE_TYPE, "get-news-details",
                token, "123456", news_id);
        call.enqueue(new Callback<NewsDetailResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<NewsDetailResponse> call, Response<NewsDetailResponse> response) {
                assert response.body() != null;

                NewsDetails newsDetails = response.body().getNewsDetails();
                next_news_id = newsDetails.getNextNews().getNewsId();
                prev_news_id = newsDetails.getPrevNews().getNewsId();
                str_share_news_link = newsDetails.getShareLink();

                tv_news_title.setText(newsDetails.getTitle());
                tv_date_views.setText(String.format("%s | %s views", newsDetails.getDate(), newsDetails.getViews()));
                Picasso.get().load(newsDetails.getThumb()).into(iv_news);

                wv_description.getSettings().setJavaScriptEnabled(true);
                wv_description.getSettings().getAllowContentAccess();
                wv_description.getSettings().getDefaultTextEncodingName();
                wv_description.getSettings().setDefaultFontSize(18);
                String htmlContent = newsDetails.getDescription();
                String encodedHtml = Base64.encodeToString(htmlContent.getBytes(), Base64.NO_PADDING);
                wv_description.loadData(encodedHtml, "text/html", "base64");
                //wv_description.loadData(newsDetails.getDescription(), "text/html; charset=utf-8", "UTF-8");
                // wv_description.loadDataWithBaseURL(null,newsDetails.getDescription(),"text/html", "utf-8", null);

                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_news_detail.setText(Html.fromHtml(newsDetails.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_news_detail.setText(Html.fromHtml(newsDetails.getDescription()));
                }

                tv_news_detail.setClickable(true);
                Linkify.addLinks(tv_news_detail, Linkify.WEB_URLS);
                tv_news_detail.setMovementMethod(LinkMovementMethod.getInstance());*/

                List<RelatedNewsItem> trendingNewsItems = response.body().getNewsDetails().getRelatedNews();
                relatedAdsFromNewsDetailAdapter = new RelatedAdsFromNewsDetailAdapter(NewsDetailsFromNotificationActivity.this, trendingNewsItems, NewsDetailsFromNotificationActivity.this);
                rv_related_news.setAdapter(relatedAdsFromNewsDetailAdapter);

                rightAdsItems = response.body().getNewsDetails().getRightAds();
                avt_right.setAdapter(new NewsDetailRightAdsSliderAdapter(NewsDetailsFromNotificationActivity.this, rightAdsItems));
                indicator_avt_right.setupWithViewPager(avt_right, true);
                Timer timer2 = new Timer();
                timer2.scheduleAtFixedRate(new SliderTimer2(), 6000, 6000);

                leftAdsItems = response.body().getNewsDetails().getLeftAds();
                avt_left.setAdapter(new NewsDetailLeftAdsSliderAdapter(NewsDetailsFromNotificationActivity.this, leftAdsItems));
                indicator_avt_left.setupWithViewPager(avt_left, true);
                Timer timer3 = new Timer();
                timer3.scheduleAtFixedRate(new SliderTimer3(), 6000, 6000);

                bottomAdsItems = response.body().getNewsDetails().getBottomAds();
                avt_bottom.setAdapter(new NewsDetailBottomAdsSliderAdapter(NewsDetailsFromNotificationActivity.this, bottomAdsItems));
                indicator_avt_bottom.setupWithViewPager(avt_bottom, true);
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new SliderTimer(), 6000, 6000);

                if (response.body().getNewsDetails().getNextNews().getTitle() != null) {
                    rl_next.setVisibility(View.VISIBLE);
                    tv_next.setText(getResources().getString(R.string.next));
                    tv_next_title.setText(response.body().getNewsDetails().getNextNews().getTitle());
                    Picasso.get().load(response.body().getNewsDetails().getNextNews().getThumb()).into(iv_next_news);
                } else {
                    tv_next.setText("");
                    rl_next.setVisibility(View.GONE);
                }

                if (response.body().getNewsDetails().getPrevNews().getTitle() != null) {
                    rl_previous.setVisibility(View.VISIBLE);
                    tv_prev_title.setText(response.body().getNewsDetails().getPrevNews().getTitle());
                    Picasso.get().load(response.body().getNewsDetails().getPrevNews().getThumb()).into(iv_previous_news);
                } else {
                    rl_previous.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NewsDetailResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("newsdeatil_error", t.toString());
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from_notification) {
            Intent toMain = new Intent(this, MainActivity.class);
            startActivity(toMain);
            this.finish();
        }
    }

    @Override
    public void onNewsSelected(String news_id) {
        Intent toDetails = new Intent(this, NewsDetailsFromNotificationActivity.class);
        toDetails.putExtra("news_id", news_id);
        toDetails.putExtra("from_notification", true);
        startActivity(toDetails);
        this.finish();
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if (avt_bottom.getCurrentItem() < bottomAdsItems.size() - 1) {
                    avt_bottom.setCurrentItem(avt_bottom.getCurrentItem() + 1);
                } else {
                    avt_bottom.setCurrentItem(0);
                }
            });
        }
    }

    private class SliderTimer2 extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if (avt_right.getCurrentItem() < rightAdsItems.size() - 1) {
                    avt_right.setCurrentItem(avt_right.getCurrentItem() + 1);
                } else {
                    avt_right.setCurrentItem(0);
                }
            });
        }
    }

    private class SliderTimer3 extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if (avt_left.getCurrentItem() < leftAdsItems.size() - 1) {
                    avt_left.setCurrentItem(avt_left.getCurrentItem() + 1);
                } else {
                    avt_left.setCurrentItem(0);
                }
            });
        }
    }
}
