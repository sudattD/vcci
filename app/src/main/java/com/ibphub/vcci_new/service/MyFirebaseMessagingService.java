package com.ibphub.vcci_new.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.MainActivity;

import java.util.Date;
import java.util.Map;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private Intent intent;
    private NotificationCompat.Builder notificationBuilder;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "msg: " + remoteMessage.toString());
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            Config.objectId = data.get("object_id");
            //Config.body = data.get("object_body");
            Config.title = data.get("object_title");
            Config.objectType = data.get("object_type");
            showSmallNotification();
            /*if ("advt".equals(Config.objectType)){
                Class clazz = ViewPagerActivity.class;
                Context context = this;
                Intent toFullScreenImageView = new Intent(context, clazz);
                toFullScreenImageView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                ArrayList<String> myList = new ArrayList<>();
                myList.add(Config.objectId);
                toFullScreenImageView.putExtra("mylist", myList);
                toFullScreenImageView.putExtra("current_pos", 0);
                toFullScreenImageView.putExtra("title", Config.title);
                context.startActivity(toFullScreenImageView);
            } else {
                showSmallNotification();
            }*/
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
        } else {
            showSmallNotification();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showSmallNotification() {
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        inboxStyle.bigText(Config.title);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        /*intentAction(Config.objectType, Config.objectId);
        intent = new Intent(getApplicationContext(), BuySellActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        notificationBuilder = new NotificationCompat.Builder(this, Config.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(Config.title)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setAutoCancel(true)
                .setSound(defaultSound)
                /*.setContentText(Html.fromHtml(Config.body, Html.FROM_HTML_MODE_LEGACY))*/
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)        //Un-commented now for no reason
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);

        Intent notifyIntent = new Intent(this, NotificationWrapper.class);
        notifyIntent.putExtra("type", Config.objectType);
        notifyIntent.putExtra("id", Config.objectId);
        notifyIntent.setAction(Intent.ACTION_MAIN);
        notifyIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        //notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);


/* Add a thing to let MainActivity know that we came from a Notification.
Here we can add other data we desire as well. */
        notifyIntent.putExtra("intent_bool", true);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(notifyPendingIntent);

        int random = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(random, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void scheduleJob() {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        NotificationChannel notificationChannel = new NotificationChannel(Config.NOTIFICATION_CHANNEL_ID, Config.NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("Tdaux Notifications");
        notificationChannel.enableLights(true);
        notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
        notificationChannel.enableVibration(true);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
