package com.ibphub.vcci_new.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.adapter.CommitteeListAdapter;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.model.committee.CommitteeDataItem;
import com.ibphub.vcci_new.model.committee.CommitteeDataResponse;
import com.ibphub.vcci_new.model.committee.Committees;
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


public class CommitteeFragment extends Fragment {

    @BindView(R.id.tv_menu_title)
    TextView tv_menu_title;

    @BindView(R.id.rv_committee)
    RecyclerView rv_committee;
    private RecyclerView.LayoutManager layoutManager;

    private CommitteeListAdapter committeeListAdapter;

    private int id = 0;
    private String value = "";

    public CommitteeFragment() {
        // Required empty public constructor
    }

    public CommitteeFragment(int ID, String title) {
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
        return inflater.inflate(R.layout.fragment_committee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).showPopUpMenuIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Committee");
        ButterKnife.bind(this, view);
        Stash.put("menu_for", "committee");

        id = Objects.requireNonNull(getArguments()).getInt("ID");
        value = Objects.requireNonNull(getArguments()).getString("value", "");

        Log.d(TAG, "onViewCreated: " + id + " " + value);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rv_committee.setHasFixedSize(true);

        if (id == 7) {
            tv_menu_title.setText(getResources().getString(R.string.office_bearers));
        } else if(id == 1){
            tv_menu_title.setText(getResources().getString(R.string.working_committee_2));
        }else if(id == 2){
            tv_menu_title.setText(getResources().getString(R.string.co_opted_members));
        }else if(id == 3){
            tv_menu_title.setText(getResources().getString(R.string.invitee_members));
        }else if(id == 4){
            tv_menu_title.setText(getResources().getString(R.string.special_invitee_members));
        }else if(id == 5){
            tv_menu_title.setText(getResources().getString(R.string.advisory_board));
        }else if(id == 6){
            tv_menu_title.setText(getResources().getString(R.string.working_committee));
        } else {
            tv_menu_title.setText(getResources().getString(R.string.office_bearers));
        }


        getCommitteeList(String.valueOf(id));
    }

    private void getCommitteeList(String id) {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<CommitteeDataResponse> call = apiService.getCommitteeList("1.0", DEVICE_TYPE, "get-committees-page",
                token, "123456", id);
        call.enqueue(new Callback<CommitteeDataResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<CommitteeDataResponse> call, Response<CommitteeDataResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                Committees committees = response.body().getCommittees();
                //tv_menu_title.setText(value);
//                tv_menu_title.setText(committees.getTitle());
                rv_committee.setLayoutManager(layoutManager);
                List<CommitteeDataItem> committeeDataItems = committees.getData();
                committeeListAdapter = new CommitteeListAdapter(getContext(), committeeDataItems);
                rv_committee.setAdapter(committeeListAdapter);
            }

            @Override
            public void onFailure(Call<CommitteeDataResponse> call, Throwable t) {
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
