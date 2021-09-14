package com.ibphub.vcci_new.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.api.ApiClient;
import com.ibphub.vcci_new.api.ApiInterface;
import com.ibphub.vcci_new.model.login.LoginResponse;
import com.ibphub.vcci_new.model.login.Member;
import com.ibphub.vcci_new.util.MyProgressDialog;
import com.ibphub.vcci_new.util.Stash;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.ibphub.vcci_new.config.Configuration.DEVICE_TYPE;

public class LoginFragment extends Fragment {

    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.tv_password_label)
    TextView tv_password_label;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_response)
    TextView tv_response;

    private String isVerified;

    public LoginFragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) Objects.requireNonNull(getActivity())).showHamburgerIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).hidePopUpMenuIcon();
        ((MainActivity) Objects.requireNonNull(getActivity())).showHomeLogoIcon();
        // Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Contact");
        ButterKnife.bind(this, view);

        /*if (Stash.getBoolean("isVerified")){
            tv_password_label.setText("Passowrd :");
        } else {
            tv_password_label.setText("Temporary Password :");
        }*/

        /*Class clazz = LogoutActivity.class;
        Intent toFullScreenImageView = new Intent(getContext(), clazz);
        startActivity(toFullScreenImageView);*/

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tv_login)
    void onLoginClicked() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (isLoginDataValid(email, password)) {
            makeLogin(email, password);
        }
    }

    public void makeLogin(String str_email, String str_password) {
        MyProgressDialog.show(getContext(), "Loading");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        String token = Stash.getString("fcm_token");
        Log.d(TAG, "getSliderMenu: " + token);

        Call<LoginResponse> call = apiService.makeLogin("1.0", DEVICE_TYPE, "member-login",
                token, "123456", str_email, str_password);
        call.enqueue(new Callback<LoginResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                MyProgressDialog.dismiss();
                assert response.body() != null;
                if (response.body().getError() == 0) {
                    Member member = response.body().getMember();
                    Stash.put("isLoginUserVerified", member);
                    if (member.getVarified() != null) {
                        if ("No".equals(member.getVarified())) {
                            tv_response.setVisibility(View.VISIBLE);
                            Stash.put("isVerified", false);
                        } else {
                            tv_response.setVisibility(View.GONE);
                            Stash.put("isVerified", true);
                            Intent intent = ((MainActivity) Objects.requireNonNull(getActivity())).getIntent();
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    public boolean isLoginDataValid(String email, String password) {
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "Enter valid email Id", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
