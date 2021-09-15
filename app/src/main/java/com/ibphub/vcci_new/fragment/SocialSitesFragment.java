package com.ibphub.vcci_new.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.MainActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SocialSitesFragment extends Fragment {

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.lv_call)
    ListView lv_call;

    @BindView(R.id.lv_email)
    ListView lv_email;

    @BindView(R.id.wv_contact_form)
    WebView wv_contact_form;

    public SocialSitesFragment() {
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
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hidePopUpMenuIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Vcci MemberList");
        ButterKnife.bind(this, view);
        //Stash.put("menu_for", "contact");

        wv_contact_form.getSettings().setJavaScriptEnabled(true);
        /*wv_contact_form.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv_contact_form.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_contact_form.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv_contact_form.getSettings().setAppCacheEnabled(true);
        wv_contact_form.getSettings().setBlockNetworkImage(true);
        wv_contact_form.getSettings().setLoadsImagesAutomatically(true);
        wv_contact_form.getSettings().setGeolocationEnabled(false);
        wv_contact_form.getSettings().setNeedInitialFocus(false);
        wv_contact_form.getSettings().setSaveFormData(false);
        wv_contact_form.getSettings().setDefaultFontSize(14);
        wv_contact_form.getSettings().setDomStorageEnabled(true);

        wv_contact_form.getSettings().setBuiltInZoomControls(true);*/

        //wv_contact_form.loadData(about.getData(), "text/html; charset=utf-8", "UTF-8");
        wv_contact_form.loadUrl("http://vccimembers.ibphub.com/");

        //getContactsData();

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
