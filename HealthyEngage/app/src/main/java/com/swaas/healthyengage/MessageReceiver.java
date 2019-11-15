package com.swaas.healthyengage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;

import static utils.Constants.NOTIFICATION_ID;

public class MessageReceiver extends FirebaseMessagingService {

    public static final String ANDROID_CHANNEL_ID = "com.healthyengage.pushnotifications.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    private NotificationManager mManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String topic = remoteMessage.getFrom();
        createChannels();
        showNotifications(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(),topic);
        //getAndroidChannelNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotifications(String msg, String title,String topic) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("isFromNotification", true);
      PendingIntent  pendingIntent = PendingIntent.getActivity(this, 101 , intent, 0);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Notification notification = new NotificationCompat.Builder(MessageReceiver.this)
                .setContentText(msg)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.mipmap.appicon)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify(NOTIFICATION_ID, notification);


    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getAndroidChannelNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);


    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {

        // create android channel
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);


    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    void showAlertMessage(final String message,String title){
        new IOSDialogBuilder(GlobalApplication.sThis)
                .setTitle(title)
                .setSubtitle(message)
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setSingleButtonView(true)
                .setPositiveListener("",null)
                .setNegativeListener("",null)
                .setSinglePositiveListener("OK", new IOSDialogClickListener() {
                    @Override
                    public void onClick(IOSDialog dialog) {
                            dialog.dismiss();

                    }
                })
                .build().show();

    }

}
