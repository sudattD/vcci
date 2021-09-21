package vcci.android.consumer.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import vcci.android.consumer.R;

public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;


    @BindView(R.id.web_view)
    WebView web_view;

    private boolean from_notification = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String getID = getIntent().getStringExtra("id");
        Log.d("getID", "onCreate: " + getID);

        tvTitle.setText(getResources().getString(R.string.video_clip));

        from_notification = getIntent().getBooleanExtra("from_notification", false);
        getGalleryDetail(getID);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void getGalleryDetail(String getID) {
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web_view.getSettings().setAppCacheEnabled(true);
        web_view.getSettings().setGeolocationEnabled(false);
        web_view.getSettings().setNeedInitialFocus(false);
        web_view.loadUrl("https://www.youtube.com/embed/" + getID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from_notification){
            Intent toMain = new Intent(this, MainActivity.class);
            startActivity(toMain);
            this.finish();
        }
    }
}
