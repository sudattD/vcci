package vcci.android.consumer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

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
import vcci.android.consumer.adapter.MembershipFormAdapter;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.interfaces.ItemSelectionListener;
import vcci.android.consumer.model.membership_form.Membership;
import vcci.android.consumer.model.membership_form.MembershipForm;
import vcci.android.consumer.model.membership_form.MembershipFormsResponse;
import vcci.android.consumer.model.membership_form.OtherDocs;
import vcci.android.consumer.util.MyProgressDialog;
import vcci.android.consumer.util.NetworkConstants;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MembershipFragment extends Fragment implements ItemSelectionListener {

    @BindView(R.id.rv_membership)
    RecyclerView rv_membership;

    @BindView(R.id.wv_description)
    WebView wv_description;

    @BindView(R.id.tv_other_associations)
    TextView tv_other_associations;

    private RecyclerView.LayoutManager layoutManager;
    private MembershipFormAdapter membershipFormAdapter;

    public MembershipFragment() {
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
        return inflater.inflate(R.layout.fragment_membership, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hidePopUpMenuIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle(getResources().getString(R.string.membership_form));
        ButterKnife.bind(this, view);
        //Stash.put("menu_for", "contact");

        layoutManager = new LinearLayoutManager(getContext());
        rv_membership.setHasFixedSize(true);

        getMembershipForms();
    }

    private void getMembershipForms() {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<MembershipFormsResponse> call = apiService.getMembershipForms(NetworkConstants.version, Configuration.DEVICE_TYPE, "get-membership",
                token, "123456");
        call.enqueue(new Callback<MembershipFormsResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<MembershipFormsResponse> call, Response<MembershipFormsResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                Membership membership = response.body().getMembership();
                rv_membership.setLayoutManager(layoutManager);
                List<MembershipForm> membershipFormList = membership.getData();
                if (membershipFormList != null && membershipFormList.size() > 0) {
                    membershipFormAdapter = new MembershipFormAdapter(getContext(), membershipFormList,
                            MembershipFragment.this);
                    rv_membership.setAdapter(membershipFormAdapter);

                }
                OtherDocs otherDocs = response.body().getOthers();
                tv_other_associations.setText(otherDocs.getTitle());
                wv_description.getSettings().setJavaScriptEnabled(true);
                wv_description.getSettings().getAllowContentAccess();
                wv_description.getSettings().getDefaultTextEncodingName();
                wv_description.getSettings().setDefaultFontSize(18);
                String htmlContent = otherDocs.getData();
                String encodedHtml = Base64.encodeToString(htmlContent.getBytes(), Base64.NO_PADDING);
                wv_description.loadData(encodedHtml, "text/html", "base64");
            }

            @Override
            public void onFailure(Call<MembershipFormsResponse> call, Throwable t) {
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
    public void onItemSelected(String id, String value) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
        startActivity(browserIntent);
    }
}