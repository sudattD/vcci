package vcci.android.consumer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vcci.android.consumer.R;
import vcci.android.consumer.activity.MainActivity;
import vcci.android.consumer.util.LocaleManager;
import vcci.android.consumer.util.Stash;

public class SettingsFragment extends Fragment {

    @BindView(R.id.tv_apply_settings)
    TextView tv_apply_settings;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;

    @BindView(R.id.radio_english)
    RadioButton radio_english;

    @BindView(R.id.radio_gujarati)
    RadioButton radio_gujarati;

    @BindView(R.id.radio_hindi)
    RadioButton radio_hindi;

    private String user_selected_lang;

    public SettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hideHomeLogoIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hidePopUpMenuIcon();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Settings");
        ButterKnife.bind(this, view);
        Stash.put("menu_for", "settings");

        user_selected_lang = Stash.getString("language");
        if ("".equals(user_selected_lang) || ("eng").equals(user_selected_lang)) {
            radio_english.setChecked(true);
            radio_gujarati.setChecked(false);
            radio_hindi.setChecked(false);
        } else if (("guj").equals(user_selected_lang)) {
            radio_english.setChecked(false);
            radio_gujarati.setChecked(true);
            radio_hindi.setChecked(false);
        } else if (("hin").equals(user_selected_lang))  {
            radio_english.setChecked(false);
            radio_gujarati.setChecked(false);
            radio_hindi.setChecked(true);
        }

        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_english:
                    Stash.put("language", "eng");
                    //setNewLocale((MainActivity) getActivity(), LocaleManager.ENGLISH);
                    break;
                case R.id.radio_gujarati:
                    Stash.put("language", "guj");
                    //setNewLocale((MainActivity) getActivity(), LocaleManager.GUJARATI);
                    break;
                case R.id.radio_hindi:
                    Stash.put("language", "hin");
                    //setNewLocale((MainActivity) getActivity(), LocaleManager.GUJARATI);
                    break;
            }
        });
    }

    @OnClick(R.id.tv_apply_settings)
    void onApplySettingsClicked() {
        String language = Stash.getString("language");
        switch (language) {
            case "eng":
                Stash.put("language", "eng");
                setNewLocale((MainActivity) getActivity(), LocaleManager.ENGLISH);
                break;
            case "guj":
                Stash.put("language", "guj");
                setNewLocale((MainActivity) getActivity(), LocaleManager.GUJARATI);
                break;

            case "hin":
                Stash.put("language", "hin");
                setNewLocale((MainActivity) getActivity(), LocaleManager.HINDI);
                break;
        }
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(mContext, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    @OnClick(R.id.tv_apply_settings)
    void onApplySettingClicked() {

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
