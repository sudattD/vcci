package com.ibphub.vcci_new.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.adapter.BulletinsAdapter;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.interfaces.BulletinSelectedListener;
import com.ibphub.vcci_new.model.bulletin.BulletinResponse;
import com.ibphub.vcci_new.model.bulletin.BulletinsItem;
import com.ibphub.vcci_new.util.Stash;
import com.ibphub.vcci_new.R;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BulletinActivity extends BaseActivity implements BulletinSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;

    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.rv_bulletins)
    RecyclerView rv_bulletins;

    private String bulletin_id = "";
    private String video_url = "";
    private String youtube_id = "";

    private BulletinsAdapter dashboardBulletinsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletin_activity_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTitle.setText(getResources().getString(R.string.txt_bulletins));
        bulletin_id = getIntent().getStringExtra("bulletin_id");
        video_url = getIntent().getStringExtra("video_url");
        youtube_id = getIntent().getStringExtra("youtube_id");

        getBulletinNews(bulletin_id, video_url);
    }

    private void getBulletinNews(String bulletin_id, String video_url) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<BulletinResponse> call = apiService.getBulletinNews("1.0", "2", "get-news-bulletin",
                token, "123456", "1", "100");
        call.enqueue(new Callback<BulletinResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<BulletinResponse> call, Response<BulletinResponse> response) {
                List<BulletinsItem> bulletins = Objects.requireNonNull(response.body()).getBulletins();
                if (bulletins != null && bulletins.size() > 0) {
                    if (!video_url.isEmpty() && !youtube_id.isEmpty()) {
                        loadWebContent(youtube_id, video_url);
                    } else {
                        loadWebContent(bulletins.get(0).getYoutube_id(), bulletins.get(0).getVideoUrl());
                    }

                    dashboardBulletinsAdapter = new BulletinsAdapter(BulletinActivity.this, bulletins, BulletinActivity.this);
                    rv_bulletins.setAdapter(dashboardBulletinsAdapter);
                }

            }

            @Override
            public void onFailure(Call<BulletinResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebContent(String id, String video_url) {
       /* WebSettings webSettings = webview.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webview.setWebViewClient(new myWebClient());
        webview.loadUrl(video_url);*/

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setAppCacheEnabled(true);
        webview.setWebViewClient(new myWebClient());
        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webview.loadUrl("https://www.youtube.com/embed/" + id);

    }

    @Override
    public void onBulletinSelected(String id, String value, String youtube_id) {
        loadWebContent(youtube_id, value);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
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
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
        this.finish();
    }
}
