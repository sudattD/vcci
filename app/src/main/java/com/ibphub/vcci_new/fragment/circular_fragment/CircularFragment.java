package com.ibphub.vcci_new.fragment.circular_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.ibphub.vcci_new.activity.CircularDetailsActivity;
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.adapter.CircularListAdapter;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.interfaces.ItemSelectionListener;
import com.ibphub.vcci_new.model.circulars.circular_list.CircularListResponse;
import com.ibphub.vcci_new.model.circulars.circular_list.Circulars;
import com.ibphub.vcci_new.model.circulars.circular_list.DataItem;
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

public class CircularFragment extends Fragment implements ItemSelectionListener {

    @BindView(R.id.tv_menu_title)
    TextView tv_menu_title;

    @BindView(R.id.tv_no_events)
    TextView tv_no_events;

    @BindView(R.id.rv_events)
    RecyclerView rv_events;
    private RecyclerView.LayoutManager layoutManager;

    private CircularListAdapter eventsListAdapter;

    private int id = 0;
    private String value = "";

    public CircularFragment() {
        // Required empty public constructor
    }

    public CircularFragment(int ID, String title) {
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
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).showPopUpMenuIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Circulars");
        ButterKnife.bind(this, view);
        Stash.put("menu_for", "circulars");

        id = Objects.requireNonNull(getArguments()).getInt("ID");
        value = Objects.requireNonNull(getArguments()).getString("value", "");

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rv_events.setHasFixedSize(true);

        getCircularsList(String.valueOf(id));
    }

    private void getCircularsList(String id) {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<CircularListResponse> call = apiService.getCircularList("1.0", DEVICE_TYPE, "get-circulars",
                token, "123456", id);
        call.enqueue(new Callback<CircularListResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<CircularListResponse> call, Response<CircularListResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                Circulars events = response.body().getCirculars();
                tv_menu_title.setText(value);
                rv_events.setLayoutManager(layoutManager);
                List<DataItem> eventItems = events.getData();
                if (eventItems != null && eventItems.size() > 0) {
                    tv_no_events.setVisibility(View.GONE);
                    rv_events.setVisibility(View.VISIBLE);
                    eventsListAdapter = new CircularListAdapter(getContext(), eventItems, CircularFragment.this);
                    rv_events.setAdapter(eventsListAdapter);
                } else {
                    tv_no_events.setVisibility(View.VISIBLE);
                    rv_events.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CircularListResponse> call, Throwable t) {
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
        Intent toDetails = new Intent(getActivity(), CircularDetailsActivity.class);
        toDetails.putExtra("id", id);
        startActivity(toDetails);
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
