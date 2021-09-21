package vcci.android.consumer.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vcci.android.consumer.R;
import vcci.android.consumer.activity.BulletinActivity;
import vcci.android.consumer.activity.CategorywiseDetailActivity;
import vcci.android.consumer.activity.MainActivity;
import vcci.android.consumer.activity.NewsDetailsFromNotificationActivity;
import vcci.android.consumer.adapter.DashboardBulletinsAdapter;
import vcci.android.consumer.adapter.GCCITrendingAdapter;
import vcci.android.consumer.adapter.LatestNewsAdapter;
import vcci.android.consumer.adapter.dashboard_ads_adapter.BottomAdsSliderAdapter;
import vcci.android.consumer.adapter.dashboard_ads_adapter.DashboardBannersSliderAdapter;
import vcci.android.consumer.adapter.dashboard_ads_adapter.FeaturedNewsSliderAdapter;
import vcci.android.consumer.adapter.dashboard_ads_adapter.LeftAdsSliderAdapter;
import vcci.android.consumer.adapter.dashboard_ads_adapter.RightAdsSliderAdapter;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.interfaces.BulletinSelectedListener;
import vcci.android.consumer.interfaces.NewsSelectionListener;
import vcci.android.consumer.model.dashboard_news.BannersItem;
import vcci.android.consumer.model.dashboard_news.BottomAdsItem;
import vcci.android.consumer.model.dashboard_news.BulletinsItem;
import vcci.android.consumer.model.dashboard_news.DashboardResponse;
import vcci.android.consumer.model.dashboard_news.FeaturedNewsItem;
import vcci.android.consumer.model.dashboard_news.LatestNewsItem;
import vcci.android.consumer.model.dashboard_news.LeftAdItem;
import vcci.android.consumer.model.dashboard_news.RightAdItem;
import vcci.android.consumer.model.dashboard_news.TrendingNewsItem;
import vcci.android.consumer.util.MyProgressDialog;
import vcci.android.consumer.util.NetworkConstants;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements NewsSelectionListener, BulletinSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ViewPager viewPager1, viewPager2, avt_right, avt_left, avt_bottom;
    private TabLayout indicator1, indicator2, indicator_avt_right, indicator_avt_left, indicator_avt_bottom;

    List<Integer> image;

    private RecyclerView rv_news, rv_bulletins, rv_gcci_trending;
    private RecyclerView.LayoutManager layoutManager;
    private LatestNewsAdapter latestNewsAdapter;
    private DashboardBulletinsAdapter dashboardBulletinsAdapter;
    private GCCITrendingAdapter gcciTrendingAdapter;

    @BindView(R.id.tv_more_bulletins)
    TextView tv_more_bulletins;

    @BindView(R.id.tv_more_news)
    TextView tv_more_news;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<BannersItem> bannersItemList;
    private List<FeaturedNewsItem> featuredNewsItems;
    private List<RightAdItem> rightAdItems;
    private List<LeftAdItem> leftAdItems;
    private List<BottomAdsItem> bottomAdsItems;

    private MainActivity mActivity;

    @BindView(R.id.tv_label_news)
    TextView tv_label_news;

    @BindView(R.id.tv_ad1)
    TextView tv_ad1;
    @BindView(R.id.tv_bltns)
    TextView tv_bltns;

    @BindView(R.id.tv_gcci)
    TextView tv_gcci;
    @BindView(R.id.tv_ad2)
    TextView tv_ad2;
    @BindView(R.id.tv_ad3)
    TextView tv_ad3;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showPopUpMenuIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).showHomeLogoIcon();
        ButterKnife.bind(this, view);

        initViews(view);

        Stash.put("menu_for", "home");
        Stash.put("news_type", "news_detail");

        getNewsDataList();

    }

    private void getNewsDataList() {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<DashboardResponse> call = apiService.getDashboardData(NetworkConstants.version, Configuration.DEVICE_TYPE, "get-home-data",
                token, "123456", "1", "100");
        call.enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;

                bannersItemList = response.body().getBanners();
                viewPager1.setAdapter(new DashboardBannersSliderAdapter(getContext(), bannersItemList));
                indicator1.setupWithViewPager(viewPager1, true);
                Timer timer1 = new Timer();
                timer1.scheduleAtFixedRate(new SliderTimer1(), 6000, 6000);

                featuredNewsItems = response.body().getFeaturedNews();
                viewPager2.setAdapter(new FeaturedNewsSliderAdapter(getContext(), featuredNewsItems, HomeFragment.this));
                indicator2.setupWithViewPager(viewPager2, true);
                Timer timer2 = new Timer();
                timer2.scheduleAtFixedRate(new SliderTimer2(), 6000, 6000);

                List<LatestNewsItem> newsItemList = response.body().getLatestNews();
                latestNewsAdapter = new LatestNewsAdapter(getContext(), newsItemList, HomeFragment.this);
                rv_news.setAdapter(latestNewsAdapter);

                List<BulletinsItem> bulletinsItemList = response.body().getBulletins();
                dashboardBulletinsAdapter = new DashboardBulletinsAdapter(getContext(), bulletinsItemList, HomeFragment.this);
                rv_bulletins.setAdapter(dashboardBulletinsAdapter);

                List<TrendingNewsItem> trendingNewsItems = response.body().getTrendingNews();
                gcciTrendingAdapter = new GCCITrendingAdapter(getContext(), trendingNewsItems, HomeFragment.this);
                rv_gcci_trending.setAdapter(gcciTrendingAdapter);

                leftAdItems = response.body().getLeftAd();
                avt_left.setAdapter(new LeftAdsSliderAdapter(getContext(), leftAdItems));
                indicator_avt_left.setupWithViewPager(avt_left, true);
                Timer timer4 = new Timer();
                timer4.scheduleAtFixedRate(new SliderTimer4(), 6000, 6000);

                rightAdItems = response.body().getRightAd();
                avt_right.setAdapter(new RightAdsSliderAdapter(getContext(), rightAdItems));
                indicator_avt_right.setupWithViewPager(avt_right, true);
                Timer timer3 = new Timer();
                timer3.scheduleAtFixedRate(new SliderTimer3(), 6000, 6000);

                bottomAdsItems = response.body().getBottomAds();
                avt_bottom.setAdapter(new BottomAdsSliderAdapter(getContext(), bottomAdsItems));
                indicator_avt_bottom.setupWithViewPager(avt_bottom, true);
                Timer timer5 = new Timer();
                timer5.scheduleAtFixedRate(new SliderTimer5(), 6000, 6000);
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                MyProgressDialog.dismiss();
                Toast.makeText(getActivity(), "No news found", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initViews(View view) {
        viewPager1 = view.findViewById(R.id.home_viewPager);
        indicator1 = view.findViewById(R.id.indicator);

        viewPager2 = view.findViewById(R.id.home_viewPager2);
        indicator2 = view.findViewById(R.id.indicator2);

        avt_right = view.findViewById(R.id.avt_right);
        indicator_avt_right = view.findViewById(R.id.indicator_avt_right);

        avt_left = view.findViewById(R.id.avt_left);
        indicator_avt_left = view.findViewById(R.id.indicator_avt_left);

        avt_bottom = view.findViewById(R.id.avt_bottom);
        indicator_avt_bottom = view.findViewById(R.id.indicator_avt_bottom);

        rv_news = view.findViewById(R.id.rv_news);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv_news.setHasFixedSize(true);

        rv_bulletins = view.findViewById(R.id.rv_bulletins);
        rv_bulletins.setHasFixedSize(true);

        rv_gcci_trending = view.findViewById(R.id.rv_gcci_trending);
        rv_gcci_trending.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rv_news.setLayoutManager(layoutManager);
        rv_bulletins.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_gcci_trending.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/OpenSans-Bold.ttf");
        tv_label_news.setTypeface(custom_font);
        tv_more_news.setTypeface(custom_font);
        tv_more_bulletins.setTypeface(custom_font);
        tv_ad1.setTypeface(custom_font);
        tv_ad2.setTypeface(custom_font);
        tv_ad3.setTypeface(custom_font);
        tv_bltns.setTypeface(custom_font);
        tv_gcci.setTypeface(custom_font);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNewsSelected(String news_id) {
        /*NewsDetailFragment nextFrag = new NewsDetailFragment(news_id);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();*/
        Intent toDetails = new Intent(getActivity(), NewsDetailsFromNotificationActivity.class);
        toDetails.putExtra("news_id", news_id);
        toDetails.putExtra("from_notification", true);
        startActivity(toDetails);
    }

    @Override
    public void onBulletinSelected(String id, String value, String youtube_id) {
        /*BulletinFragment nextFrag = new BulletinFragment(id, value, youtube_id);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, nextFrag, "BulletinFrag")
                .addToBackStack(null)
                .commit();*/

        Intent toDetails = new Intent(getActivity(), BulletinActivity.class);
        toDetails.putExtra("bulletin_id", id);
        toDetails.putExtra("video_url", value);
        toDetails.putExtra("youtube_id", youtube_id);
        startActivity(toDetails);
    }

    @OnClick(R.id.tv_more_bulletins)
    void onMoreBulletinsClicked() {
        /*BulletinFragment nextFrag = new BulletinFragment("", "", "");
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, nextFrag, "BulletinFrag")
                .addToBackStack(null)
                .commit();*/

        Intent toDetails = new Intent(getActivity(), BulletinActivity.class);
        toDetails.putExtra("bulletin_id", "");
        toDetails.putExtra("video_url", "");
        toDetails.putExtra("youtube_id", "");
        startActivity(toDetails);
    }

    @OnClick(R.id.tv_more_news)
    void onMoreNewsClicked() {
        Stash.put("news_type", "more_news");
        /*CategorywiseDatalFragment nextFrag = new CategorywiseDatalFragment(0, "All");
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, nextFrag, "CategorywiseDatalFrag")
                .addToBackStack(null)
                .commit();*/

        Intent toDetails = new Intent(getContext(), CategorywiseDetailActivity.class);
        toDetails.putExtra("categoryID", 0);
        toDetails.putExtra("title", "All News");
        startActivity(toDetails);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class SliderTimer1 extends TimerTask {
        @Override
        public void run() {
            mActivity.runOnUiThread(() -> {
                if (viewPager1.getCurrentItem() < bannersItemList.size() - 1) {
                    viewPager1.setCurrentItem(viewPager1.getCurrentItem() + 1);
                } else {
                    viewPager1.setCurrentItem(0);
                }
            });
        }
    }

    private class SliderTimer2 extends TimerTask {
        @Override
        public void run() {
            mActivity.runOnUiThread(() -> {
                if (viewPager2.getCurrentItem() < featuredNewsItems.size() - 1) {
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                } else {
                    viewPager2.setCurrentItem(0);
                }
            });
        }
    }

    private class SliderTimer3 extends TimerTask {
        @Override
        public void run() {
            mActivity.runOnUiThread(() -> {
                if (avt_right.getCurrentItem() < rightAdItems.size() - 1) {
                    avt_right.setCurrentItem(avt_right.getCurrentItem() + 1);
                } else {
                    avt_right.setCurrentItem(0);
                }
            });
        }
    }

    private class SliderTimer4 extends TimerTask {
        @Override
        public void run() {
            mActivity.runOnUiThread(() -> {
                if (avt_left.getCurrentItem() < leftAdItems.size() - 1) {
                    avt_left.setCurrentItem(avt_left.getCurrentItem() + 1);
                } else {
                    avt_left.setCurrentItem(0);
                }
            });
        }
    }

    private class SliderTimer5 extends TimerTask {
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
}
