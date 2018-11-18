package com.example.lindenstirk.customalarmmanagerv6;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class RingtoneService extends Service {

    MediaPlayer ringtone;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            //Log.i("LocalService", "Received start id " + startId + ": " + intent);

            // creates instance of media player and starts the ringtone
            ringtone = MediaPlayer.create(this, R.raw.alarm_default);
            ringtone.setLooping(true); // repeats forever
            ringtone.start();


            return START_NOT_STICKY;
        }

        @Override
        public void onDestroy() {
            // Tell the user we stopped.
            Toast.makeText(this, "On Destroy Called", Toast.LENGTH_SHORT).show();


        }
    }


