package com.example.lindenstirk.customalarmmanagerv6;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ALARM!", Toast.LENGTH_LONG).show();

        Intent serviceIntent = new Intent(context, RingtoneService.class);
        context.startService(serviceIntent);

    }
}
