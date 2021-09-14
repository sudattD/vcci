package com.ibphub.vcci_new.fragment.news_detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.adapter.NewsDetailBottomAdsSliderAdapter;
import com.ibphub.vcci_new.adapter.NewsDetailLeftAdsSliderAdapter;
import com.ibphub.vcci_new.adapter.NewsDetailRightAdsSliderAdapter;
import com.ibphub.vcci_new.adapter.RelatedAdsFromNewsDetailAdapter;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.interfaces.NewsSelectionListener;
import com.ibphub.vcci_new.model.newsDetail.BottomAdsItem;
import com.ibphub.vcci_new.model.newsDetail.LeftAdsItem;
import com.ibphub.vcci_new.model.newsDetail.NewsDetailResponse;
import com.ibphub.vcci_new.model.newsDetail.NewsDetails;
import com.ibphub.vcci_new.model.newsDetail.RelatedNewsItem;
import com.ibphub.vcci_new.model.newsDetail.RightAdsItem;
import com.ibphub.vcci_new.util.Stash;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewsDetailFragment extends Fragment implements NewsSelectionListener {

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

    private MainActivity mActivity;
    private List<BottomAdsItem> bottomAdsItems;
    private List<RightAdsItem> rightAdsItems;
    private List<LeftAdsItem> leftAdsItems;

    private RelatedAdsFromNewsDetailAdapter relatedAdsFromNewsDetailAdapter;

    public NewsDetailFragment(String news_id) {
        Bundle args = new Bundle();
        args.putString("news_id", news_id);
        setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_detail_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showBackIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hidePopUpMenuIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(getResources().getString(R.string.txt_news_detail));
        ButterKnife.bind(this, view);
        initViews(view);
        news_id = Objects.requireNonNull(getArguments()).getString("news_id");
        Log.d(TAG, "onViewCreated: " + news_id);
        getNewsDetail(news_id);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    private void initViews(View view) {
        rv_related_news.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void getNewsDetail(String news_id) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<NewsDetailResponse> call = apiService.getNewsDetail("1.0", "2", "get-news-details",
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

                Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/OpenSans-Bold.ttf");
                tv_news_title.setText(newsDetails.getTitle());
                tv_news_title.setTypeface(custom_font);
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


               /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_news_detail.setTypeface(custom_font);
                    tv_news_detail.setText(Html.fromHtml(newsDetails.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_news_detail.setTypeface(custom_font);
                    tv_news_detail.setText(Html.fromHtml(newsDetails.getDescription()));
                }

                tv_news_detail.setClickable(true);
                //Linkify.addLinks(tv_news_detail, Linkify.WEB_URLS);
                tv_news_detail.setMovementMethod(LinkMovementMethod.getInstance());*/

                List<RelatedNewsItem> trendingNewsItems = response.body().getNewsDetails().getRelatedNews();
                relatedAdsFromNewsDetailAdapter = new RelatedAdsFromNewsDetailAdapter(getContext(), trendingNewsItems, NewsDetailFragment.this);
                rv_related_news.setAdapter(relatedAdsFromNewsDetailAdapter);

                rightAdsItems = response.body().getNewsDetails().getRightAds();
                avt_right.setAdapter(new NewsDetailRightAdsSliderAdapter(getContext(), rightAdsItems));
                indicator_avt_right.setupWithViewPager(avt_right, true);
                Timer timer2 = new Timer();
                timer2.scheduleAtFixedRate(new SliderTimer2(), 6000, 6000);

                leftAdsItems = response.body().getNewsDetails().getLeftAds();
                avt_left.setAdapter(new NewsDetailLeftAdsSliderAdapter(getContext(), leftAdsItems));
                indicator_avt_left.setupWithViewPager(avt_left, true);
                Timer timer3 = new Timer();
                timer3.scheduleAtFixedRate(new SliderTimer3(), 6000, 6000);

                bottomAdsItems = response.body().getNewsDetails().getBottomAds();
                avt_bottom.setAdapter(new NewsDetailBottomAdsSliderAdapter(getContext(), bottomAdsItems));
                indicator_avt_bottom.setupWithViewPager(avt_bottom, true);
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new SliderTimer(), 6000, 6000);

                if (response.body().getNewsDetails().getNextNews() != null) {
                    if (response.body().getNewsDetails().getNextNews().getTitle() != null) {
                        rl_next.setVisibility(View.VISIBLE);
                        tv_next.setText(getResources().getString(R.string.next));
                        tv_next_title.setText(response.body().getNewsDetails().getNextNews().getTitle());
                        Picasso.get().load(response.body().getNewsDetails().getNextNews().getThumb()).into(iv_next_news);
                    } else {
                        tv_next.setText("");
                        rl_next.setVisibility(View.GONE);
                    }
                } else {
                    tv_next.setText("");
                    rl_next.setVisibility(View.GONE);
                }

                if (response.body().getNewsDetails().getPrevNews() != null) {
                    if (response.body().getNewsDetails().getPrevNews().getTitle() != null) {
                        rl_previous.setVisibility(View.VISIBLE);
                        tv_prev_title.setText(response.body().getNewsDetails().getPrevNews().getTitle());
                        Picasso.get().load(response.body().getNewsDetails().getPrevNews().getThumb()).into(iv_previous_news);
                    } else {
                        rl_previous.setVisibility(View.GONE);
                    }
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;

    }

    @Override
    public void onNewsSelected(String news_id) {
        NewsDetailFragment nextFrag = new NewsDetailFragment(news_id);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();

    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            mActivity.runOnUiThread(() -> {
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
            mActivity.runOnUiThread(() -> {
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
            mActivity.runOnUiThread(() -> {
                if (avt_left.getCurrentItem() < leftAdsItems.size() - 1) {
                    avt_left.setCurrentItem(avt_left.getCurrentItem() + 1);
                } else {
                    avt_left.setCurrentItem(0);
                }
            });
        }
    }
}
