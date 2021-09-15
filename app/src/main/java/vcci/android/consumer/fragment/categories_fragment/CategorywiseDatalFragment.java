package vcci.android.consumer.fragment.categories_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vcci.android.consumer.R;
import vcci.android.consumer.activity.MainActivity;
import vcci.android.consumer.activity.NewsDetailsFromNotificationActivity;
import vcci.android.consumer.adapter.CategoryWiseNewsAdapter;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.interfaces.NewsSelectionListener;
import vcci.android.consumer.model.news_by_category.NewsByCategoryIDResponse;
import vcci.android.consumer.model.news_by_category.NewsItem;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CategorywiseDatalFragment extends Fragment implements NewsSelectionListener {

    @BindView(R.id.iv_main_image)
    ImageView iv_main_image;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private String news_id = "";

    private RecyclerView.LayoutManager layoutManager;
    private CategoryWiseNewsAdapter categoryWiseNewsAdapter;
    private RecyclerView rv_news;
    private RelativeLayout rl_leading_news;

    public CategorywiseDatalFragment(int categoryID, String title) {
        Bundle args = new Bundle();
        args.putInt("categoryID", categoryID);
        args.putString("title", title);
        setArguments(args);
    }

    /* */

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //categoryID Parameter 1.
     * @return A new instance of fragment HomeFragment.
     *//*
    // TODO: Rename and change types and number of parameters
    public static CategorywiseDatalFragment newInstance(int categoryID) {
        CategorywiseDatalFragment fragment = new CategorywiseDatalFragment();
        Bundle args = new Bundle();
        args.putInt("categoryID", categoryID);
        fragment.setArguments(args);
        return fragment;
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_detail_listing_layout, container, false);
        ((MainActivity) Objects.requireNonNull(getActivity())).showBackIcon();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_news = view.findViewById(R.id.rv_category_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv_news.setHasFixedSize(false);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rv_news.setLayoutManager(layoutManager);

        //String title = Objects.requireNonNull(getArguments()).getString("title");
        //((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle(title);
        ((MainActivity) Objects.requireNonNull(getActivity())).showPopUpMenuIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(true);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(getResources().getString(R.string.txt_more_news));
        String catID = String.valueOf(Objects.requireNonNull(getArguments()).getInt("categoryID", 0));
        Log.d(TAG, "onViewCreated: " + catID);
        getCategorywiseDataList(catID);

        rl_leading_news = view.findViewById(R.id.rl_leading_news);
        rl_leading_news.setOnClickListener(v -> {
            Stash.put("news_type", "news_detail");
            /*NewsDetailFragment nextFrag = new NewsDetailFragment(news_id);
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();*/
            Intent toDetails = new Intent(getActivity(), NewsDetailsFromNotificationActivity.class);
            toDetails.putExtra("news_id", news_id);
            toDetails.putExtra("from_notification", true);
            startActivity(toDetails);
        });
    }

    private void getCategorywiseDataList(String catID) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<NewsByCategoryIDResponse> call = apiService.getNewsByCategoryID("1.0", Configuration.DEVICE_TYPE, "get-news",
                token, "123456", "1", "100", catID);
        call.enqueue(new Callback<NewsByCategoryIDResponse>() {
            @Override
            public void onResponse(Call<NewsByCategoryIDResponse> call, Response<NewsByCategoryIDResponse> response) {
                assert response.body() != null;
                List<NewsItem> news = response.body().getNews();
                news_id = news.get(0).getNewsId();
                if (news != null && news.size() > 0) {
                    Picasso.get().load(news.get(0).getThumb()).into(iv_main_image);
                    tv_title.setText(news.get(0).getTitle());
                }

                categoryWiseNewsAdapter = new CategoryWiseNewsAdapter(getContext(), news, CategorywiseDatalFragment.this);
                rv_news.setAdapter(categoryWiseNewsAdapter);
            }

            @Override
            public void onFailure(Call<NewsByCategoryIDResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
}
