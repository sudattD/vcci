package com.ibphub.vcci_new.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ibphub.vcci_new.activity.EventDetailsActivity;
import com.ibphub.vcci_new.activity.GalleryDetailsActivity;
import com.ibphub.vcci_new.activity.NewsDetailsFromNotificationActivity;
import com.ibphub.vcci_new.activity.VideoPlayActivity;
import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.BaseActivity;
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.image_slider.ViewPagerActivity;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationWrapper extends BaseActivity {

    private String id = "";
    private String type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_wrapper_layout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            type = intent.getStringExtra("type");
        }
        intentAction(Objects.requireNonNull(type), id);
    }

    private void intentAction(String objectType, String objectId) {
        switch (objectType) {
            case "news": {
                Intent toDetails = new Intent(this, NewsDetailsFromNotificationActivity.class);
                toDetails.putExtra("news_id", objectId);
                toDetails.putExtra("from_notification", true);
                startActivity(toDetails);
                this.finish();
                break;
            }
            case "event":
                Intent toDetails = new Intent(this, EventDetailsActivity.class);
                toDetails.putExtra("id", objectId);
                toDetails.putExtra("from_notification", true);
                startActivity(toDetails);
                this.finish();
                break;
            case "circular":

                break;
            case "bulletin":

                break;
            case "video":
                Intent toVideo = new Intent(this, VideoPlayActivity.class);
                toVideo.putExtra("id", id);
                toVideo.putExtra("from_notification", true);
                startActivity(toVideo);
                this.finish();
                break;
            case "photo":
                Intent toGallery = new Intent(this, GalleryDetailsActivity.class);
                toGallery.putExtra("id", id);
                toGallery.putExtra("from_notification", true);
                startActivity(toGallery);
                this.finish();
                break;
            case "advt":
                Class clazz = ViewPagerActivity.class;
                Context context = this;
                Intent toFullScreenImageView = new Intent(context, clazz);
                ArrayList<String> myList = new ArrayList<>();
                myList.add(objectId);
                toFullScreenImageView.putExtra("mylist", myList);
                toFullScreenImageView.putExtra("current_pos", 0);
                toFullScreenImageView.putExtra("from_notification", true);
                toFullScreenImageView.putExtra("title", Config.title);
                context.startActivity(toFullScreenImageView);
                this.finish();
                break;
            default:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent toMain = new Intent(this, MainActivity.class);
        startActivity(toMain);
        this.finish();
    }
}
