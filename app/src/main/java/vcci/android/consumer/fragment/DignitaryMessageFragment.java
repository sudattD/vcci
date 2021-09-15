package vcci.android.consumer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vcci.android.consumer.R;
import vcci.android.consumer.activity.GalleryDetailsActivity;
import vcci.android.consumer.activity.MainActivity;
import vcci.android.consumer.activity.VideoPlayActivity;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.interfaces.ItemSelectionListener;
import vcci.android.consumer.model.dignitary.dig_photo.DignitaryPhotoResponse;
import vcci.android.consumer.model.dignitary.dig_video.DignitaryVideoResponse;
import vcci.android.consumer.util.MyProgressDialog;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DignitaryMessageFragment extends Fragment implements ItemSelectionListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_title)
    TextView tv_title;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.web_view)
    WebView web_view;

    private int id = 0;
    private String value = "";


    public DignitaryMessageFragment() {
        // Required empty public constructor
    }

    public DignitaryMessageFragment(int ID, String title) {
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
        return inflater.inflate(R.layout.fragment_dignitary_msg, container, false);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        getDignitaryData(String.valueOf(id));
    }

    private void getDignitaryData(String id) {
        if ("2".equals(id)) {
            MyProgressDialog.show(getContext(), "Loading");
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            String token = Stash.getString("fcm_token");
            Log.d(TAG, "getSliderMenu: " + token);

            Call<DignitaryPhotoResponse> call = apiService.getDignitaryPhotoData("1.0", Configuration.DEVICE_TYPE,
                    "get-dignitary",
                    token, "123456", id);
            call.enqueue(new Callback<DignitaryPhotoResponse>() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public void onResponse(Call<DignitaryPhotoResponse> call, Response<DignitaryPhotoResponse> response) {
                    MyProgressDialog.dismiss();
                    assert response.body() != null;
                    tv_title.setText(response.body().getDignitary().getTitle());
                    web_view.getSettings().setJavaScriptEnabled(true);
                    web_view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                    web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    web_view.getSettings().setAppCacheEnabled(true);
                    web_view.getSettings().setGeolocationEnabled(false);
                    web_view.getSettings().setNeedInitialFocus(false);
                    web_view.loadUrl(response.body().getDignitary().getPopup());
                }

                @Override
                public void onFailure(Call<DignitaryPhotoResponse> call, Throwable t) {
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

            Call<DignitaryVideoResponse> call = apiService.getDignitaryVideoData("1.0", Configuration.DEVICE_TYPE, "get-dignitary",
                    token, "123456", "1");
            call.enqueue(new Callback<DignitaryVideoResponse>() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public void onResponse(Call<DignitaryVideoResponse> call, Response<DignitaryVideoResponse> response) {
                    MyProgressDialog.dismiss();
                    assert response.body() != null;
                    tv_title.setText(response.body().getDignitaryVideo().getTitle());
                    web_view.getSettings().setJavaScriptEnabled(true);
                    web_view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                    web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    web_view.getSettings().setAppCacheEnabled(true);
                    web_view.getSettings().setGeolocationEnabled(false);
                    web_view.getSettings().setNeedInitialFocus(false);
                    web_view.loadUrl("https://www.youtube.com/embed/" + response.body().getDignitaryVideo().getData().get(0).getYoutubeId());
                }

                @Override
                public void onFailure(Call<DignitaryVideoResponse> call, Throwable t) {
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
