package com.example.lindenstirk.customalarmmanagerv6;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.Calendar;

public class AddEditAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    public static final String EXTRA_ID =
            "com.example.lindenstirk.customalarmmanagerv6.EXTRA_ID";

    public static final String EXTRA_TITLE =
            "com.example.lindenstirk.customalarmmanagerv6.EXTRA_TITLE";

    public static final String EXTRA_TIME =
            "com.example.lindenstirk.customalarmmanagerv6.EXTRA_TIME";

    public static final String EXTRA_DATE =
            "com.example.lindenstirk.customalarmmanagerv6.EXTRA_DATE";

    public static final String EXTRA_STATE =
            "com.example.lindenstirk.customalarmmanagerv6.EXTRA_STATE";

    public static final String EXTRA_WIG =
            "com.example.lindenstirk.customalarmmanagerv6.EXTRA_WIG";


    private EditText editTextTitle;
    private TextView textViewTime;
    private TextView textViewDate;
    private int alarmStatus;

    public int id = -1;
    public String title = "";
    public static int ALARM_WARNING = 60000; // time in milliseconds (60 seconds)


    private Calendar calendar = Calendar.getInstance();
    private ToggleButton toggleStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        editTextTitle = findViewById(R.id.edit_text_title);
        textViewTime = findViewById(R.id.text_view_time);
        textViewDate = findViewById(R.id.text_view_date);

        toggleStatus = findViewById(R.id.toggle_status);



        getSupportActionBar().setHomeAsUpIndicator((R.drawable.ic_close));

        Intent intent = getIntent();

        // if there's an EXTRA ID then it's not the first time, thus you're editing an alarm
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Alarm");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            textViewTime.setText(intent.getStringExtra(EXTRA_TIME));
            textViewDate.setText(intent.getStringExtra(EXTRA_DATE));


            alarmStatus = intent.getIntExtra(EXTRA_STATE, alarmStatus);
            Toast.makeText(this, "Alarm Status " + alarmStatus, Toast.LENGTH_SHORT).show();

        }
        else {
            setTitle("Add Alarm");
        }

/*

        // Check alarm status for toggle button

        if (alarmStatus == 0) {
            alarmOff();
           // Toast.makeText(this, "Alarm is off", Toast.LENGTH_SHORT).show();

        }

        else if (alarmStatus == 1){
            alarmOn();
           // Toast.makeText(this, "Alarm is on", Toast.LENGTH_SHORT).show();

        }

        // Alarm not off or on
        else {
            alarmOff();
           // Toast.makeText(this, "Alarm is NOT on or off", Toast.LENGTH_SHORT).show();
        }
*/





        // Time Picker Fragment
        Button buttonSetTime = findViewById(R.id.button_set_time);
        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });


        // Date Picker Fragment
        Button buttonSetDate = findViewById(R.id.button_set_date);
        buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        // Day Picker Fragment
        // TODO button for the user to select days of the week for a repeating alarm



        // Toggle switch for alarm status
        toggleStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleStatus.isChecked()) {
                    alarmOn();
                }
                else {
                    alarmOff();
                }
            }
        });







    }


    // Alarm On / Off methods

    public void alarmOn() {
        alarmStatus = 1;
        Toast.makeText(this, "alarmOn", Toast.LENGTH_SHORT).show();
        toggleStatus.setBackgroundColor(Color.CYAN);
        toggleStatus.setTextOn("On");
        startAlarm(calendar);
    }

    public void alarmOff() {
        alarmStatus = 0;
        Toast.makeText(this, "alarmOff", Toast.LENGTH_SHORT).show();
        toggleStatus.setBackgroundColor(Color.RED);
        toggleStatus.setTextOff("Off");
        cancelAlarm();
    }










    private void saveAlarm() {
        title = editTextTitle.getText().toString();
        String time = textViewTime.getText().toString();
        String date = textViewDate.getText().toString();
        int state = alarmStatus;


        if (title.trim().isEmpty() || time.trim().isEmpty() || date.trim().isEmpty()) {
            Toast.makeText(this, "Insert a title, time, and date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_STATE, state);


        id = getIntent().getIntExtra(EXTRA_ID, -1);

        // only updates id if not -1
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_alarm_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_alarm:
                saveAlarm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        TextView textView = findViewById(R.id.text_view_date);
        textView.setText(currentDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = findViewById(R.id.text_view_time);

        // Makes sure there are two zeros if minute < 10
        if (minute < 10) {
            textView.setText(hourOfDay + ":" + 0 + minute);
        }
        else {
            textView.setText(hourOfDay + ":" + minute);
        }

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

       // updateTimeText(calendar);
        startAlarm(calendar);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar calendar){

        title = editTextTitle.getText().toString();

        // each pending intent needs a unique request id
        // which is the id of the alarm in the database
       // int id = getIntent().getIntExtra(EXTRA_ID, -1);



        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        // Extra string into intent for alarm title
        intent.putExtra("alarm_title", title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1); // if time has already passed
        }

        // sends intent to alarm manager
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - ALARM_WARNING, 60000, pendingIntent);
        // alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - ALARM_WARNING, pendingIntent);


    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.cancel(pendingIntent);

    }








    }












