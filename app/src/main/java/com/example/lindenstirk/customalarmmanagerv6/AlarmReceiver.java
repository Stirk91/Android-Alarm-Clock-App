package com.example.lindenstirk.customalarmmanagerv6;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;



public class AlarmReceiver extends BroadcastReceiver {

    public String alarmTitle;



    @Override
    public void onReceive(Context context, Intent receivedIntent) {

        alarmTitle = receivedIntent.getExtras().getString("alarm_title");


        showNotification(context);

        Intent serviceIntent = new Intent(context, RingtoneService.class);
        context.startService(serviceIntent);

    }



    private void showNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(context, MainActivity.CHANNEL_01_ID)
                .setSmallIcon(R.drawable.n_icon_01)
                .setContentTitle("Alarm:" + alarmTitle)
                .setContentText("is ringing")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification);

    }





}

