package com.healthyengage.patientcare;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import utils.Constants;
import vidyo.VideoConferenceActivity;

import static utils.Constants.NOTIFICATION_ID;

public class MessageReceiver extends FirebaseMessagingService {

    public static final String ANDROID_CHANNEL_ID = "com.healthyengage.pushnotifications.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

    private static final String TAG = "FCM Service";
    private static int count = 0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String topic = remoteMessage.getFrom();
        showNotifications(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(),topic);
    }






    private void showNotifications(String msg, String title,String topic) {
        if(msg.equalsIgnoreCase(Constants.VIDEO_CALL_INITIATED)){
            Intent intent = new Intent(getApplicationContext(), VideoConferenceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("pushnotification", "yes");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel("Sesame", "Sesame", importance);
                mChannel.setDescription(msg);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotifyManager.createNotificationChannel(mChannel);
            }else{
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Seasame");
                mBuilder.setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(R.mipmap.appicon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.appicon))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setColor(Color.parseColor("#FFD600"))
                        .setContentIntent(pendingIntent)
                        .setChannelId("Sesame")
                        .setPriority(NotificationCompat.PRIORITY_LOW);

                mNotifyManager.notify(count, mBuilder.build());
                count++;
            }
        }







/*
        if(msg.equalsIgnoreCase(Constants.VIDEO_CALL_INITIATED)){
            Intent intent = new Intent(this, VideoConferenceActivity.class);
            intent.putExtra("isFromNotification", true);
            PendingIntent  pendingIntent = PendingIntent.getActivity(this, 102 , intent, 0);
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
        }else{
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

*/


    }



    void showAlertMessage(final String message){
        new IOSDialogBuilder(this)
                .setTitle("Alert")
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
