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
import com.ibphub.vcci_new.adapter.dignitary_adapter.PhotoDignitaryListAdapter;
import com.ibphub.vcci_new.adapter.dignitary_adapter.VideoDignitaryListAdapter;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.interfaces.ItemSelectionListener;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_photos.DataItemDignitaryPhoto;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_photos.DignitaryPhoto;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_photos.DignitaryPhotoResponseNew;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_video.DataItemDignitaryVideo;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_video.DignitaryVideo;
import com.ibphub.vcci_new.model.dignitary_new.dignitary_video.DignitaryVideoResponseNew;
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


public class DignitaryNewFragment extends Fragment implements ItemSelectionListener {

    @BindView(R.id.tv_menu_title)
    TextView tv_menu_title;

    @BindView(R.id.rv_gallery)
    RecyclerView rv_gallery;
    private RecyclerView.LayoutManager layoutManager;

    private PhotoDignitaryListAdapter photoGalleryListAdapter;
    private VideoDignitaryListAdapter videoGalleryListAdapter;

    private int id = 0;
    private String value = "";


    public DignitaryNewFragment() {
        // Required empty public constructor
    }

    public DignitaryNewFragment(int ID, String title) {
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
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Dignitary Message");
        ButterKnife.bind(this, view);
        Stash.put("menu_for", "dignitary");

        id = Objects.requireNonNull(getArguments()).getInt("ID");
        value = Objects.requireNonNull(getArguments()).getString("value", "");

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rv_gallery.setHasFixedSize(true);

        if (id == 1){
            tv_menu_title.setText(getResources().getString(R.string.dignitary_video_msg));
        } else {
            tv_menu_title.setText(getResources().getString(R.string.dignitary_photo_msg));
        }


        //getGalleryData(String.valueOf(id));
    }

    @Override
    public void onResume() {
        super.onResume();
        getGalleryData(String.valueOf(id));
    }

    private void getGalleryData(String id) {
        if ("2".equals(id)) {
            MyProgressDialog.show(getContext(), "Loading");
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            String token = Stash.getString("fcm_token");
            Log.d(TAG, "getSliderMenu: " + token);

            Call<DignitaryPhotoResponseNew> call = apiService.getDignitaryPhotoList("1.0", DEVICE_TYPE, "get-dignitary-ios",
                    token, "123456", id);
            call.enqueue(new Callback<DignitaryPhotoResponseNew>() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public void onResponse(Call<DignitaryPhotoResponseNew> call,
                                       Response<DignitaryPhotoResponseNew> response) {
                    MyProgressDialog.dismiss();
                    assert response.body() != null;
                    DignitaryPhoto galleryItem = response.body().getDignitaryPhoto();
                    //tv_menu_title.setText(galleryItem.getTitle());
                    rv_gallery.setLayoutManager(layoutManager);
                    List<DataItemDignitaryPhoto> galleryItemList = galleryItem.getData();
                    if (galleryItemList != null && galleryItemList.size() > 0) {
                        photoGalleryListAdapter = new PhotoDignitaryListAdapter(getContext(), galleryItemList, DignitaryNewFragment.this);
                        rv_gallery.setAdapter(photoGalleryListAdapter);
                    }
                }

                @Override
                public void onFailure(Call<DignitaryPhotoResponseNew> call, Throwable t) {
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

            Call<DignitaryVideoResponseNew> call = apiService.getDignitaryVideoList("1.0", DEVICE_TYPE, "get-dignitary-ios",
                    token, "123456", id);
            call.enqueue(new Callback<DignitaryVideoResponseNew>() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public void onResponse(Call<DignitaryVideoResponseNew> call, Response<DignitaryVideoResponseNew> response) {
                    MyProgressDialog.dismiss();
                    assert response.body() != null;
                    DignitaryVideo gallery_video = response.body().getDignitaryVideo();
                    //tv_menu_title.setText(gallery_video.getTitle());
                    rv_gallery.setLayoutManager(layoutManager);
                    List<DataItemDignitaryVideo> galleryItemList = gallery_video.getData();
                    if (galleryItemList != null && galleryItemList.size() > 0) {
                        videoGalleryListAdapter = new VideoDignitaryListAdapter(getContext(), galleryItemList, DignitaryNewFragment.this);
                        rv_gallery.setAdapter(videoGalleryListAdapter);
                    }
                }

                @Override
                public void onFailure(Call<DignitaryVideoResponseNew> call, Throwable t) {
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
