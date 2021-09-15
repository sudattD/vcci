package com.ibphub.vcci_new.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.adapter.PastPresidentAdapter;
import com.ibphub.vcci_new.adapter.PersonnelDataAdapter;
import com.ibphub.vcci_new.adapter.SecretriatDataAdapter;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.model.about_us.about_us_content.About;
import com.ibphub.vcci_new.model.about_us.about_us_content.AboutUsContentResponse;
import com.ibphub.vcci_new.model.about_us.about_us_personnel.AboutPersonnel;
import com.ibphub.vcci_new.model.about_us.about_us_personnel.DataItem;
import com.ibphub.vcci_new.model.about_us.about_us_personnel.PersonnelsDataResponse;
import com.ibphub.vcci_new.model.about_us.about_us_secretariat.Secretariat;
import com.ibphub.vcci_new.model.about_us.about_us_secretariat.SecretariatItem;
import com.ibphub.vcci_new.model.about_us.about_us_secretariat.SecretariatResponse;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutUsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutUsFragment#//} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment {
    @BindView(R.id.rl_content)
    RelativeLayout rl_content;
    @BindView(R.id.rl_pdf)
    RelativeLayout rl_pdf;
    @BindView(R.id.rl_personnel_listing)
    RelativeLayout rl_personnel_listing;
    @BindView(R.id.rl_past_presidents_listing)
    RelativeLayout rl_past_presidents_listing;
    @BindView(R.id.rl_secreteriat)
    RelativeLayout rl_secreteriat;

    @BindView(R.id.tv_menu_title)
    TextView tv_menu_title;
    @BindView(R.id.wv_content)
    WebView wv_content;
    /*@BindView(R.id.tv_content)
    TextView tv_content;*/
    @BindView(R.id.wv_pdf)
    WebView wv_pdf;

    @BindView(R.id.rv_personnel)
    RecyclerView rv_personnel;
    private RecyclerView.LayoutManager layoutManager;
    private PersonnelDataAdapter personnelDataAdapter;
    @BindView(R.id.rv_past_president)
    RecyclerView rv_past_president;
    private PastPresidentAdapter pastPresidentAdapter;
    @BindView(R.id.rv_secreteriat)
    RecyclerView rv_secreteriat;
    private SecretriatDataAdapter secretriatDataAdapter;
    @BindView(R.id.wv_secreteriat_content)
    WebView wv_secreteriat_content;

    private int id = 0;
    private String value = "";

    public AboutUsFragment() {
    }

    public AboutUsFragment(int ID, String title) {
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
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showPopUpMenuIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(getResources().getString(R.string.nav_about_us));
        ButterKnife.bind(this, view);
        Stash.put("menu_for", "about_us");
        //tv_content.setMovementMethod(new ScrollingMovementMethod());

        id = Objects.requireNonNull(getArguments()).getInt("ID");
        value = Objects.requireNonNull(getArguments()).getString("value", "");

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rv_personnel.setHasFixedSize(true);
        rv_past_president.setHasFixedSize(true);

        getAboutUsApi(String.valueOf(id));

    }

    private void getAboutUsApi(String id) {
        switch (id) {
            case "1":
            case "2":
            case "3":
                rl_content.setVisibility(View.VISIBLE);
                rl_pdf.setVisibility(View.GONE);
                rl_personnel_listing.setVisibility(View.GONE);
                rl_past_presidents_listing.setVisibility(View.GONE);
                rl_secreteriat.setVisibility(View.GONE);
                getAboutUsContentData(id);
                break;

            case "4":
            case "5":
                rl_personnel_listing.setVisibility(View.VISIBLE);
                rl_content.setVisibility(View.GONE);
                rl_pdf.setVisibility(View.GONE);
                rl_past_presidents_listing.setVisibility(View.GONE);
                rl_secreteriat.setVisibility(View.GONE);
                getAboutUsPersonnelData(id);
                break;

            case "6":
                rl_personnel_listing.setVisibility(View.GONE);
                rl_content.setVisibility(View.GONE);
                rl_pdf.setVisibility(View.GONE);
                rl_secreteriat.setVisibility(View.GONE);
                rl_past_presidents_listing.setVisibility(View.VISIBLE);
                getAboutUsPersonnelData(id);
                break;

            case "7":
                rl_past_presidents_listing.setVisibility(View.GONE);
                rl_personnel_listing.setVisibility(View.GONE);
                rl_content.setVisibility(View.GONE);
                rl_secreteriat.setVisibility(View.VISIBLE);
                rl_pdf.setVisibility(View.GONE);
                getSecreteriatData(id);
                break;


           /* case "7":
                rl_past_presidents_listing.setVisibility(View.GONE);
                rl_personnel_listing.setVisibility(View.GONE);
                rl_content.setVisibility(View.GONE);
                rl_secreteriat.setVisibility(View.GONE);
                rl_pdf.setVisibility(View.VISIBLE);
                getGcciPdf(id);
                break;*/
        }
    }

    private void getSecreteriatData(String id) {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<SecretariatResponse> call = apiService.getSecretariatData("1.0", DEVICE_TYPE, "get-about-us",
                token, "123456", id);
        call.enqueue(new Callback<SecretariatResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<SecretariatResponse> call, Response<SecretariatResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                Secretariat secretariat = response.body().getSecretariat();
                tv_menu_title.setText(value);
                wv_secreteriat_content.getSettings().setJavaScriptEnabled(true);
                wv_secreteriat_content.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                wv_secreteriat_content.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                wv_secreteriat_content.getSettings().setAppCacheEnabled(true);
                wv_secreteriat_content.getSettings().setBlockNetworkImage(true);
                wv_secreteriat_content.getSettings().setLoadsImagesAutomatically(true);
                wv_secreteriat_content.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                wv_secreteriat_content.getSettings().setGeolocationEnabled(false);
                wv_secreteriat_content.getSettings().setNeedInitialFocus(false);
                wv_secreteriat_content.getSettings().setSaveFormData(false);
                wv_secreteriat_content.getSettings().setFixedFontFamily("HindVadodara-Regular.ttf");
                wv_secreteriat_content.getSettings().setDefaultFontSize(16);
                wv_secreteriat_content.getSettings().setDomStorageEnabled(true);
                wv_secreteriat_content.loadData(secretariat.getData(), "text/html; charset=utf-8", "UTF-8");
//                wv_content.loadDataWithBaseURL("", about.getData(), "text/html", "UTF-8", "");
                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_content.setText(Html.fromHtml(about.getData(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_content.setText(Html.fromHtml(about.getData()));
                }*/

                rv_secreteriat.setLayoutManager(layoutManager);
                List<SecretariatItem> members = secretariat.getMembers();
                secretriatDataAdapter = new SecretriatDataAdapter(getContext(), members);
                rv_secreteriat.setAdapter(secretriatDataAdapter);
            }

            @Override
            public void onFailure(Call<SecretariatResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getAboutUsPersonnelData(String id) {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);
        Call<PersonnelsDataResponse> call = apiService.getAbouUsPersonnelData("1.0", DEVICE_TYPE, "get-about-us",
                token, "123456", id);
        call.enqueue(new Callback<PersonnelsDataResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<PersonnelsDataResponse> call, Response<PersonnelsDataResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                AboutPersonnel about = response.body().getAbout();
                tv_menu_title.setText(value);
                if ("6".equals(id)) {
                    rv_past_president.setLayoutManager(layoutManager);
                    List<DataItem> dataItemList = about.getData();
                    pastPresidentAdapter = new PastPresidentAdapter(getContext(), dataItemList);
                    rv_past_president.setAdapter(pastPresidentAdapter);
                } else {
                    rv_personnel.setLayoutManager(layoutManager);
                    List<DataItem> dataItemList = about.getData();
                    personnelDataAdapter = new PersonnelDataAdapter(getContext(), dataItemList);
                    rv_personnel.setAdapter(personnelDataAdapter);
                }
            }

            @Override
            public void onFailure(Call<PersonnelsDataResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getGcciPdf(String id) {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<AboutUsContentResponse> call = apiService.getAbouUsContent("1.0", DEVICE_TYPE, "get-about-us",
                token, "123456", id);
        call.enqueue(new Callback<AboutUsContentResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<AboutUsContentResponse> call, Response<AboutUsContentResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                About about = response.body().getAbout();
                tv_menu_title.setText(value);
                wv_pdf.getSettings().setJavaScriptEnabled(true);
                wv_pdf.loadUrl("https://docs.google.com/gview?embedded=true&url=" + about.getData());
            }

            @Override
            public void onFailure(Call<AboutUsContentResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getAboutUsContentData(String id) {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<AboutUsContentResponse> call = apiService.getAbouUsContent("1.0", DEVICE_TYPE, "get-about-us",
                token, "123456", id);
        call.enqueue(new Callback<AboutUsContentResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<AboutUsContentResponse> call, Response<AboutUsContentResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                About about = response.body().getAbout();
                tv_menu_title.setText(value);
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
                wv_content.loadData(about.getData(), "text/html; charset=utf-8", "UTF-8");
//                wv_content.loadDataWithBaseURL("", about.getData(), "text/html", "UTF-8", "");
                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_content.setText(Html.fromHtml(about.getData(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_content.setText(Html.fromHtml(about.getData()));
                }*/
            }

            @Override
            public void onFailure(Call<AboutUsContentResponse> call, Throwable t) {
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
}
