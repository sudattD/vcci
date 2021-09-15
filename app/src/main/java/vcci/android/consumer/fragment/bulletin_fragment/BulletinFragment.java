package vcci.android.consumer.fragment.bulletin_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vcci.android.consumer.R;
import vcci.android.consumer.activity.MainActivity;
import vcci.android.consumer.adapter.BulletinsAdapter;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.interfaces.BulletinSelectedListener;
import vcci.android.consumer.model.bulletin.BulletinResponse;
import vcci.android.consumer.model.bulletin.BulletinsItem;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BulletinFragment extends Fragment implements BulletinSelectedListener {

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

    public BulletinFragment(String bulletin_id, String video_url, String youtube_id) {
        Bundle args = new Bundle();
        args.putString("bulletin_id", bulletin_id);
        args.putString("video_url", video_url);
        args.putString("youtube_id", youtube_id);
        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Makes Progress bar Visible
        getActivity().getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
/*        getActivity().getWindow().requestFeature(Window.FEATURE_PROGRESS);
        // Makes Progress bar Visible
        getActivity().getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);*/
        View view = inflater.inflate(R.layout.bulletin_frag_layout, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showBackIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hidePopUpMenuIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Bulletin");
        ButterKnife.bind(this, view);

        bulletin_id = Objects.requireNonNull(getArguments()).getString("bulletin_id", "");
        video_url = getArguments().getString("video_url", "");
        youtube_id = getArguments().getString("youtube_id", "");

        getBulletinNews(bulletin_id, video_url);
    }

    private void getBulletinNews(String bulletin_id, String video_url) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<BulletinResponse> call = apiService.getBulletinNews("1.0", Configuration.DEVICE_TYPE, "get-news-bulletin",
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

                    dashboardBulletinsAdapter = new BulletinsAdapter(getContext(), bulletins, BulletinFragment.this);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }
}
