package com.ibphub.vcci_new.fragment.about_us_menuwise_fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.MainActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsByMenuFragment extends Fragment {

    @BindView(R.id.tv_menu_title)
    TextView tv_menu_title;
    @BindView(R.id.tv_content)
    TextView tv_content;

    public AboutUsByMenuFragment(int ID, String title) {
        Bundle args = new Bundle();
        args.putInt("ID", ID);
        args.putString("title", title);
        setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        getActivity().setTitle("AboutPersonnel Us");
        //Stash.put("menu_for", "about_us");
        ButterKnife.bind(this, view);

        tv_menu_title.setText(getArguments().getString("title"));


        /*String title = Objects.requireNonNull(getArguments()).getString("AboutPersonnel Us");
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(title);
        String ID = String.valueOf(Objects.requireNonNull(getArguments()).getInt("ID", 0));*/
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
