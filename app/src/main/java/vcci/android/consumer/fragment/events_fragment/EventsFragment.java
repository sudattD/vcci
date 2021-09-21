package vcci.android.consumer.fragment.events_fragment;

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
import vcci.android.consumer.activity.EventDetailsActivity;
import vcci.android.consumer.activity.MainActivity;
import vcci.android.consumer.adapter.EventsListAdapter;
import vcci.android.consumer.api.ApiClient;
import vcci.android.consumer.api.ApiInterface;
import vcci.android.consumer.config.Configuration;
import vcci.android.consumer.interfaces.ItemSelectionListener;
import vcci.android.consumer.model.events.event_list.EventItem;
import vcci.android.consumer.model.events.event_list.EventListResponse;
import vcci.android.consumer.model.events.event_list.Events;
import vcci.android.consumer.util.MyProgressDialog;
import vcci.android.consumer.util.NetworkConstants;
import vcci.android.consumer.util.Stash;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EventsFragment extends Fragment implements ItemSelectionListener {

    @BindView(R.id.tv_menu_title)
    TextView tv_menu_title;

    @BindView(R.id.tv_no_events)
    TextView tv_no_events;

    @BindView(R.id.rv_events)
    RecyclerView rv_events;
    private RecyclerView.LayoutManager layoutManager;

    private EventsListAdapter eventsListAdapter;

    private int id = 0;
    private String value = "";

    public EventsFragment() {
        // Required empty public constructor
    }

    public EventsFragment(int ID, String title) {
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
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Events");
        ButterKnife.bind(this, view);
        Stash.put("menu_for", "events");

        id = Objects.requireNonNull(getArguments()).getInt("ID");
        value = Objects.requireNonNull(getArguments()).getString("value", "");

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        rv_events.setHasFixedSize(true);

        getEventList(String.valueOf(id));
    }

    private void getEventList(String id) {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<EventListResponse> call = apiService.getEventsList(NetworkConstants.version, Configuration.DEVICE_TYPE, "get-events",
                token, "123456", id);
        call.enqueue(new Callback<EventListResponse>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<EventListResponse> call, Response<EventListResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                Events events = response.body().getEvents();
                tv_menu_title.setText(value);
                rv_events.setLayoutManager(layoutManager);
                List<EventItem> eventItems = events.getData();
                if (eventItems != null && eventItems.size() > 0) {
                    tv_no_events.setVisibility(View.GONE);
                    rv_events.setVisibility(View.VISIBLE);
                    eventsListAdapter = new EventsListAdapter(getContext(), eventItems, EventsFragment.this);
                    rv_events.setAdapter(eventsListAdapter);
                } else {
                    tv_no_events.setVisibility(View.VISIBLE);
                    rv_events.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<EventListResponse> call, Throwable t) {
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
        Intent toDetails = new Intent(getActivity(), EventDetailsActivity.class);
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
