package com.example.lindenstirk.customalarmmanagerv6;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Notification Channels
    public static final String CHANNEL_01_ID = "channel01";
    public static final String CHANNEL_02_ID = "channel02";


    public static final int ADD_ALARM_REQUEST = 1;
    public static final int EDIT_ALARM_REQUEST = 2;
    private AlarmViewModel alarmViewModel;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab_addAlarm = findViewById(R.id.fab_add_alarm);
        fab_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditAlarmActivity.class);
                startActivityForResult(intent, ADD_ALARM_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AlarmAdapter adapter = new AlarmAdapter();
        recyclerView.setAdapter(adapter);

        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        alarmViewModel.getAllAlarms().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(@Nullable List<Alarm> alarms) {
                adapter.setAlarms(alarms);
            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                alarmViewModel.delete(adapter.getAlarmAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Alarm deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnAlarmClickListener(new AlarmAdapter.OnAlarmClickListener() {
            @Override
            public void onAlarmClick(Alarm alarm) {
                Intent intent = new Intent(MainActivity.this, AddEditAlarmActivity.class);
                intent.putExtra(AddEditAlarmActivity.EXTRA_ID, alarm.getId());
                intent.putExtra(AddEditAlarmActivity.EXTRA_TITLE, alarm.getTitle());
                intent.putExtra(AddEditAlarmActivity.EXTRA_TIME, alarm.getTime());
                intent.putExtra(AddEditAlarmActivity.EXTRA_DATE, alarm.getDate());
                intent.putExtra(AddEditAlarmActivity.EXTRA_STATE, alarm.getState());
                startActivityForResult(intent, EDIT_ALARM_REQUEST);
            }
        });

        // Create Notification Channels
        createNotificationChannels();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ALARM_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditAlarmActivity.EXTRA_TITLE);
            String time = data.getStringExtra(AddEditAlarmActivity.EXTRA_TIME);
            String date = data.getStringExtra(AddEditAlarmActivity.EXTRA_DATE);
            String state = data.getStringExtra(AddEditAlarmActivity.EXTRA_STATE);

            Alarm alarm = new Alarm(title, time, date, state);
            alarmViewModel.insert(alarm);

            Toast.makeText(this, "Alarm saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_ALARM_REQUEST && resultCode == RESULT_OK)  {
            int id = data.getIntExtra(AddEditAlarmActivity.EXTRA_ID, -1);

            // id is invalid so the alarm cannot be updated
            if (id == -1) {
                Toast.makeText(this, "Alarm cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditAlarmActivity.EXTRA_TITLE);
            String time = data.getStringExtra(AddEditAlarmActivity.EXTRA_TIME);
            String date = data.getStringExtra(AddEditAlarmActivity.EXTRA_DATE);
            String state = data.getStringExtra(AddEditAlarmActivity.EXTRA_STATE);


            Alarm alarm = new Alarm(title, time, date, state);
            alarm.setId(id);
            alarmViewModel.update(alarm);

            Toast.makeText(this, "Alarm updated", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Alarm not saved", Toast.LENGTH_SHORT).show();
        }

    }


    // Notifications

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