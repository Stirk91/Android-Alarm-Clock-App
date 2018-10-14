package com.example.lindenstirk.customalarmmanagerv6;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_ALARM_REQUEST = 1;

    private AlarmViewModel alarmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab_addAlarm = findViewById(R.id.fab_add_alarm);
        fab_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ALARM_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddAlarmActivity.EXTRA_TITLE);
            String time = data.getStringExtra(AddAlarmActivity.EXTRA_TIME);
            String date = data.getStringExtra(AddAlarmActivity.EXTRA_DATE);

            Alarm alarm = new Alarm(title, time, date);
            alarmViewModel.insert(alarm);

            Toast.makeText(this, "Alarm saved", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Alarm not saved", Toast.LENGTH_SHORT).show();
        }

    }
}