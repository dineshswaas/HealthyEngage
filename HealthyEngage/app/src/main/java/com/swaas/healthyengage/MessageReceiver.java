package com.swaas.healthyengage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static utils.Constants.NOTIFICATION_ID;

public class MessageReceiver extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String topic = remoteMessage.getFrom();

        showNotifications(remoteMessage.getNotification().getBody(), remoteMessage.getNotification()
                .getTitle(),topic);
    }

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
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify(NOTIFICATION_ID, notification);


    }
}
