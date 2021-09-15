package com.ibphub.vcci_new.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.model.cm_msg.CmMessage;
import com.ibphub.vcci_new.model.cm_msg.CmMessageResponse;
import com.ibphub.vcci_new.util.MyProgressDialog;
import com.ibphub.vcci_new.util.Stash;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.ibphub.vcci_new.config.Configuration.DEVICE_TYPE;


public class CmMessageFragment extends Fragment {

    @BindView(R.id.iv_cm)
    ImageView iv_cm;

    @BindView(R.id.wv_content)
    WebView wv_content;

    /*@BindView(R.id.tv_cm_message)
    TextView tv_cm_message;*/


    public CmMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cm_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hidePopUpMenuIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(getResources().getString(R.string.nav_cm_message));
        ButterKnife.bind(this, view);
        Stash.put("menu_for", "cm_message");

        getCMmessage();
    }

    private void getCMmessage() {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<CmMessageResponse> call = apiService.getCmMessage("1.0", DEVICE_TYPE, "get-cm-message",
                token, "123456");
        call.enqueue(new Callback<CmMessageResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<CmMessageResponse> call, Response<CmMessageResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                CmMessage cmMessage = response.body().getCmMessage();

                Picasso.get().load(cmMessage.getThumb()).placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder).into(iv_cm);


                wv_content.getSettings().setJavaScriptEnabled(true);
                wv_content.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                wv_content.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                wv_content.getSettings().setAppCacheEnabled(true);
                wv_content.getSettings().setBlockNetworkImage(true);
                wv_content.getSettings().setLoadsImagesAutomatically(true);
                wv_content.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                wv_content.getSettings().setGeolocationEnabled(false);
                wv_content.getSettings().setNeedInitialFocus(false);
                wv_content.getSettings().setSaveFormData(false);
                wv_content.getSettings().setDefaultFontSize(16);
                wv_content.getSettings().setDomStorageEnabled(true);
                wv_content.loadData(cmMessage.getData(), "text/html; charset=utf-8", "UTF-8");

               /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_cm_message.setText(Html.fromHtml(cmMessage.getData(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_cm_message.setText(Html.fromHtml(cmMessage.getData()));
                }*/
            }

            @Override
            public void onFailure(Call<CmMessageResponse> call, Throwable t) {
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


}
