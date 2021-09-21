package vcci.android.consumer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
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
import vcci.android.consumer.activity.MainActivity;
import vcci.android.consumer.adapter.ContactNumberAdapter;
import vcci.android.consumer.adapter.EmailIdsAdapter;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.model.contact.Contact;
import vcci.android.consumer.model.contact.ContactInfoResponse;
import vcci.android.consumer.util.MyProgressDialog;
import vcci.android.consumer.util.NetworkConstants;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ContactFragment extends Fragment {

    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.lv_call)
    ListView lv_call;

    @BindView(R.id.lv_email)
    ListView lv_email;

    @BindView(R.id.wv_contact_form)
    WebView wv_contact_form;

    private ContactNumberAdapter contactNumberAdapter;
    private EmailIdsAdapter emailIdsAdapter;

    public ContactFragment() {
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
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Contact");
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
        wv_contact_form.loadUrl("http://vcci.ibphub.com/contact?app=1");

        //getContactsData();

    }

    private void getContactsData() {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<ContactInfoResponse> call = apiService.getContactInfo(NetworkConstants.version, Configuration.DEVICE_TYPE, "get-contact",
                token, "123456");
        call.enqueue(new Callback<ContactInfoResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<ContactInfoResponse> call, Response<ContactInfoResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                Contact contact = response.body().getContact();
                tv_address.setText(contact.getAddress());
                contactNumberAdapter = new ContactNumberAdapter(Objects.requireNonNull(getContext()), ContactFragment.this);
                contactNumberAdapter.setData(contact.getMobile());
                lv_call.setAdapter(contactNumberAdapter);
                emailIdsAdapter = new EmailIdsAdapter(getContext(), ContactFragment.this);
                emailIdsAdapter.setData(contact.getEmail());
                lv_email.setAdapter(emailIdsAdapter);
            }

            @Override
            public void onFailure(Call<ContactInfoResponse> call, Throwable t) {
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
