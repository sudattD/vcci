package com.ibphub.vcci_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.adapter.CategoriesAdapter;
import com.ibphub.vcci_new.adapter.CategoryWiseNewsAdapter;
import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.interfaces.CategorySelectionListener;
import com.ibphub.vcci_new.interfaces.NewsSelectionListener;
import com.ibphub.vcci_new.model.category.CategoriesItem;
import com.ibphub.vcci_new.model.category.CategoriesResponse;
import com.ibphub.vcci_new.model.news_by_category.NewsByCategoryIDResponse;
import com.ibphub.vcci_new.model.news_by_category.NewsItem;
import com.ibphub.vcci_new.util.Stash;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.ibphub.vcci_new.config.Configuration.DEVICE_TYPE;

public class CategorywiseDetailActivity extends BaseActivity implements NewsSelectionListener, CategorySelectionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;

    @BindView(R.id.iv_main_image)
    ImageView iv_main_image;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private String news_id = "";
    private int categoryID;
    private String title = "";

    private RecyclerView.LayoutManager layoutManager;
    private CategoryWiseNewsAdapter categoryWiseNewsAdapter;

    @BindView(R.id.rv_category_list)
    RecyclerView rv_news;

    @BindView(R.id.rl_leading_news)
    RelativeLayout rl_leading_news;

    private ImageView iv_category;
    private ImageView iv_category_close;

    private CategoriesAdapter categoriesAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager category_layoutManager;

    private LinearLayout ll_close_category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_detail_listing_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        categoryID = getIntent().getIntExtra("categoryID", 0);
        Log.d(TAG, "categoryID: " + categoryID);
        title = getIntent().getStringExtra("title");
        tvTitle.setText(title);

        rv_news.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        rv_news.setLayoutManager(layoutManager);

        getCategorywiseDataList(String.valueOf(categoryID));

        iv_category = findViewById(R.id.iv_category);
        iv_category_close = findViewById(R.id.iv_category_close);

        iv_category_close.setOnClickListener(v -> {
            iv_category.setVisibility(View.VISIBLE);
            iv_category_close.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            ll_close_category.setVisibility(View.GONE);
        });

        iv_category.setOnClickListener(v -> {
            iv_category.setVisibility(View.GONE);
            iv_category_close.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            ll_close_category.setVisibility(View.VISIBLE);
            getSliderMenu();

        });

        recyclerView = findViewById(R.id.lv_category);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        category_layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(category_layoutManager);

        ll_close_category = findViewById(R.id.ll_close_category);

        ll_close_category.setOnClickListener(v -> {
            recyclerView.setVisibility(View.GONE);
            ll_close_category.setVisibility(View.GONE);
            iv_category_close.setVisibility(View.GONE);
            iv_category.setVisibility(View.VISIBLE);

        });
    }

    public void getSliderMenu() {
        Log.d(TAG, "getSliderMenu: " + Stash.getString("menu_for"));
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);
        Call<CategoriesResponse> call = apiService.getCategoriesList("1.0", DEVICE_TYPE, "get-categories",
                "token", "123456", "1", "100");
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                assert response.body() != null;
                List<CategoriesItem> categoriesItemsWithHeader = new ArrayList<>();
                CategoriesItem categoriesItem = new CategoriesItem();
                categoriesItem.setId(-1);
                categoriesItem.setTitle("");
                categoriesItemsWithHeader.add(categoriesItem);
                List<CategoriesItem> categoriesItemsList = response.body().getCategories();
                categoriesItemsWithHeader.addAll(categoriesItemsList);
                Log.d(TAG, "Categories received: " + categoriesItemsWithHeader.size());
                categoriesAdapter = new CategoriesAdapter(CategorywiseDetailActivity.this, categoriesItemsWithHeader, CategorywiseDetailActivity.this);
                recyclerView.setAdapter(categoriesAdapter);

            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

    @OnClick(R.id.rl_leading_news)
    public void onLeadingNewsClicked() {
        Stash.put("news_type", "news_detail");
            /*NewsDetailFragment nextFrag = new NewsDetailFragment(news_id);
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();*/
        Intent toDetails = new Intent(this, NewsDetailsFromNotificationActivity.class);
        toDetails.putExtra("news_id", news_id);
        toDetails.putExtra("from_notification", true);
        startActivity(toDetails);
    }

    private void getCategorywiseDataList(String catID) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<NewsByCategoryIDResponse> call = apiService.getNewsByCategoryID("1.0", DEVICE_TYPE, "get-news",
                token, "123456", "1", "100", catID);
        call.enqueue(new Callback<NewsByCategoryIDResponse>() {
            @Override
            public void onResponse(Call<NewsByCategoryIDResponse> call, Response<NewsByCategoryIDResponse> response) {
                assert response.body() != null;
                List<NewsItem> news = response.body().getNews();
                if (news.size() > 0){
                    if (news.get(0).getNewsId() != null){
                        news_id = news.get(0).getNewsId();
                        if (news != null && news.size() > 0) {
                            Picasso.get().load(news.get(0).getThumb()).into(iv_main_image);
                            tv_title.setText(news.get(0).getTitle());
                        }

                        categoryWiseNewsAdapter = new CategoryWiseNewsAdapter(CategorywiseDetailActivity.this, news, CategorywiseDetailActivity.this);
                        rv_news.setAdapter(categoryWiseNewsAdapter);
                    }
                } else {
                    Toast.makeText(CategorywiseDetailActivity.this, "No news found for " + title, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsByCategoryIDResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onNewsSelected(String news_id) {
        /*NewsDetailFragment nextFrag = new NewsDetailFragment(news_id);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();*/

        Intent toDetails = new Intent(this, NewsDetailsFromNotificationActivity.class);
        toDetails.putExtra("news_id", news_id);
        toDetails.putExtra("from_notification", false);
        startActivity(toDetails);
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
        this.finish();
    }

    @Override
    public void onCategorySelected(int id, String title) {
        recyclerView.setVisibility(View.GONE);
        ll_close_category.setVisibility(View.GONE);
        iv_category.setVisibility(View.VISIBLE);
        iv_category_close.setVisibility(View.GONE);

        /*Fragment someFragment = new CategorywiseDatalFragment(id, title);
         *//* Bundle args = new Bundle();
        args.putString("categoryID", String.valueOf(id));
        someFragment.setArguments(args);*//*
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, someFragment, TAG_HOME); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.detach(someFragment);
        transaction.attach(someFragment);
        transaction.commitAllowingStateLoss();*/

        Intent toDetails = new Intent(this, CategorywiseDetailActivity.class);
        toDetails.putExtra("categoryID", id);
        toDetails.putExtra("title", title);
        startActivity(toDetails);
        this.finish();
    }
}
