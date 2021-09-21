package vcci.android.consumer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import vcci.android.consumer.R;
import vcci.android.consumer.util.LocaleManager;
import vcci.android.consumer.util.Stash;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        //setNewLocale(this, LocaleManager.ENGLISH);
        Stash.put("menu_for", "home");

        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task.getResult());
                    Log.d(TAG, token);
                    Log.d(TAG, "onCreate: " + token);
                    Stash.put("fcm_token", token);
                    //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                });
        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start your app main activity
            getFCMToken();

            /*if (Stash.getBoolean("isVerified")){
                Intent i = new Intent(SplashActivity.this, MainActivityWithLogin.class);
                startActivity(i);
                // close this activity
                finish();
            } else {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }*/

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            // close this activity
            finish();

        }, SPLASH_TIME_OUT);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.my_splash_logo_anim);
        imgLogo.startAnimation(myanim);
    }

    public void getFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    Log.d(TAG, token);
                    Log.d(TAG, "onCreate: " + token);
                    Stash.put("fcm_token", token);
                    //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                });
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        /*Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));*/
    }
}
