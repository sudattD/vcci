package vcci.android.consumer.fragment.circular_fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import vcci.android.consumer.R;
import vcci.android.consumer.activity.MainActivity;

public class CircularDetailFragment extends Fragment {


    public CircularDetailFragment(String news_id) {
        Bundle args = new Bundle();
        args.putString("news_id", news_id);
        setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_detail_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showBackIcon();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Event Detail");
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }
}
