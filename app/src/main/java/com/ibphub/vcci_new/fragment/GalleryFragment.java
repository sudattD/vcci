package com.ibphub.vcci_new.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.GalleryDetailsActivity;
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.activity.VideoPlayActivity;
import com.ibphub.vcci_new.adapter.gallery_adapter.PhotoGalleryListAdapter;
import com.ibphub.vcci_new.adapter.gallery_adapter.VideoGalleryListAdapter;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.interfaces.ItemSelectionListener;
import com.ibphub.vcci_new.model.gallery.photos.gallery_list.Gallery;
import com.ibphub.vcci_new.model.gallery.photos.gallery_list.GalleryItem;
import com.ibphub.vcci_new.model.gallery.photos.gallery_list.GalleryResponse;
import com.ibphub.vcci_new.model.gallery.videos.video_list.VideoGallery;
import com.ibphub.vcci_new.model.gallery.videos.video_list.VideoItem;
import com.ibphub.vcci_new.model.gallery.videos.video_list.VideoResponse;
import com.ibphub.vcci_new.util.MyProgressDialog;
import com.ibphub.vcci_new.util.Stash;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.ibphub.vcci_new.config.Configuration.DEVICE_TYPE;


public class GalleryFragment extends Fragment implements ItemSelectionListener {

    @BindView(R.id.tv_menu_title)
    TextView tv_menu_title;

    @BindView(R.id.rv_gallery)
    RecyclerView rv_gallery;
    private RecyclerView.LayoutManager layoutManager;

    private PhotoGalleryListAdapter photoGalleryListAdapter;
    private VideoGalleryListAdapter videoGalleryListAdapter;

    private int id = 0;
    private String value = "";


    public GalleryFragment() {
        // Required empty public constructor
    }

    public GalleryFragment(int ID, String title) {
        Bundle args = new Bundle();
        args.putInt("ID", ID);
        args.putString("value", title);
        setArguments(args);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).showPopUpMenuIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Gallery");
        ButterKnife.bind(this, view);
        Stash.put("menu_for", "gallery");

        id = Objects.requireNonNull(getArguments()).getInt("ID");
        value = Objects.requireNonNull(getArguments()).getString("value", "");

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rv_gallery.setHasFixedSize(true);

        //getGalleryData(String.valueOf(id));
    }

    @Override
    public void onResume() {
        super.onResume();
        getGalleryData(String.valueOf(id));
    }

    private void getGalleryData(String id) {
        if ("1".equals(id)) {
            MyProgressDialog.show(getContext(), "Loading");
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            String token = Stash.getString("fcm_token");
            Log.d(TAG, "getSliderMenu: " + token);

            Call<GalleryResponse> call = apiService.getGalleryList("1.0", DEVICE_TYPE, "get-gallery",
                    token, "123456", id);
            call.enqueue(new Callback<GalleryResponse>() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                    MyProgressDialog.dismiss();
                    assert response.body() != null;
                    Gallery galleryItem = response.body().getGallery();
                    tv_menu_title.setText(value);
                    rv_gallery.setLayoutManager(layoutManager);
                    List<GalleryItem> galleryItemList = galleryItem.getData();
                    if (galleryItemList != null && galleryItemList.size() > 0) {
                        photoGalleryListAdapter = new PhotoGalleryListAdapter(getContext(), galleryItemList, GalleryFragment.this);
                        rv_gallery.setAdapter(photoGalleryListAdapter);
                    }
                }

                @Override
                public void onFailure(Call<GalleryResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
        } else {
            MyProgressDialog.show(getContext(), "Loading");
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            String token = Stash.getString("fcm_token");
            Log.d(TAG, "getSliderMenu: " + token);

            Call<VideoResponse> call = apiService.getGalleryVideoList("1.0", DEVICE_TYPE, "get-gallery",
                    token, "123456", "2");
            call.enqueue(new Callback<VideoResponse>() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    MyProgressDialog.dismiss();
                    assert response.body() != null;
                    VideoGallery gallery_video = response.body().getVideoGallery();
                    tv_menu_title.setText(value);
                    rv_gallery.setLayoutManager(layoutManager);
                    List<VideoItem> galleryItemList = gallery_video.getData();
                    if (galleryItemList != null && galleryItemList.size() > 0) {
                        videoGalleryListAdapter = new VideoGalleryListAdapter(getContext(), galleryItemList, GalleryFragment.this);
                        rv_gallery.setAdapter(videoGalleryListAdapter);
                    }
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
        }

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
    public void onItemSelected(String id, String value) {
        if ("photos".equals(value)) {
            Intent toDetails = new Intent(getActivity(), GalleryDetailsActivity.class);
            toDetails.putExtra("id", id);
            toDetails.putExtra("from_notification", false);
            startActivity(toDetails);
        } else {
            Intent toDetails = new Intent(getActivity(), VideoPlayActivity.class);
            toDetails.putExtra("id", id);
            toDetails.putExtra("from_notification", false);
            startActivity(toDetails);
        }

    }
}
