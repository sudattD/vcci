package vcci.android.consumer.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vcci.android.consumer.R;
import vcci.android.consumer.adapter.PhotosAdapter;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.model.gallery.photos.gallery_detail.GalleryDetailResponse;
import vcci.android.consumer.model.gallery.photos.gallery_detail.GalleryDetails;
import vcci.android.consumer.util.MyProgressDialog;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GalleryDetailsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;

    @BindView(R.id.tv_event_title)
    TextView tv_event_title;

    @BindView(R.id.iv_event)
    ImageView iv_event;

    @BindView(R.id.tv_start_date)
    TextView tv_start_date;

    @BindView(R.id.tv_end_date)
    TextView tv_end_date;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_venue)
    TextView tv_venue;

    @BindView(R.id.tv_in_association)
    TextView tv_in_association;

    /*@BindView(R.id.tv_description)
    TextView tv_description;*/

    @BindView(R.id.wv_description)
    WebView wv_description;

    private boolean from_notification = false;
    int abc = 0;

    @BindView(R.id.rv_photos)
    RecyclerView rv_photos;
    private RecyclerView.LayoutManager layoutManager;
    private PhotosAdapter photosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_detail_fragment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTitle.setText(getResources().getString(R.string.txt_photo_detail));
        String getID = getIntent().getStringExtra("id");
        Log.d("getID", "onCreate: " + getID);

        from_notification = getIntent().getBooleanExtra("from_notification", false);

        layoutManager = new LinearLayoutManager(this);
        rv_photos.setLayoutManager(layoutManager);
        rv_photos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        getGalleryDetail(getID);
    }

    private void getGalleryDetail(String getID) {
        MyProgressDialog.show(this, "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<GalleryDetailResponse> call = apiService.getGalleryDetail("1.0", Configuration.DEVICE_TYPE, "get-gallery-details",
                token, "123456", getID);
        call.enqueue(new Callback<GalleryDetailResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<GalleryDetailResponse> call, Response<GalleryDetailResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                GalleryDetails galleryDetails = response.body().getGalleryDetails();
                tv_event_title.setText(galleryDetails.getTitle());
                photosAdapter = new PhotosAdapter(GalleryDetailsActivity.this, galleryDetails.getPhotos());
                rv_photos.setAdapter(photosAdapter);
                Picasso.get().load(galleryDetails.getThumb()).placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder).into(iv_event);
                tv_start_date.setText(galleryDetails.getStartDate());
                tv_end_date.setText(galleryDetails.getEndDate());
                tv_time.setText(galleryDetails.getEventTime());
                tv_venue.setText(galleryDetails.getVenue());
                tv_in_association.setText(galleryDetails.getAssoWith());
                //tv_description.setText(eventDetails.getDescription());

                wv_description.getSettings().setJavaScriptEnabled(true);
                wv_description.getSettings().setDefaultFontSize(16);
                wv_description.loadData(galleryDetails.getDescription(), "text/html; charset=utf-8", "UTF-8");
                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_description.setText(Html.fromHtml(galleryDetails.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_description.setText(Html.fromHtml(galleryDetails.getDescription()));
                }*/
            }

            @Override
            public void onFailure(Call<GalleryDetailResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
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
            this.onBackPressed();
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
        } else {
            Log.d(TAG, "onBackPressed: " + "I am here");
        }
    }
}
