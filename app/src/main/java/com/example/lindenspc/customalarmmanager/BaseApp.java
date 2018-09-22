package com.example.lindenspc.customalarmmanager;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class BaseApp extends Application {

    public static final String CHANNEL_01_ID = "channel01";
    public static final String CHANNEL_02_ID = "channel02";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel01 = new NotificationChannel(
                    CHANNEL_01_ID,
                    "Channel 01",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel01.setDescription("This is Channel 01");

            NotificationChannel channel02 = new NotificationChannel(
                    CHANNEL_02_ID,
                    "Channel 02",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel02.setDescription("This is Channel 02");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel01);
            manager.createNotificationChannel(channel02);

        }
    }


}
