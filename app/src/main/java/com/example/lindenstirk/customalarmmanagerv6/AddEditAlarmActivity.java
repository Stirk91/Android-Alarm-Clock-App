package com.example.lindenstirk.customalarmmanagerv6;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

    public static final String EXTRA_ALARM_TIME_IN_MILLIS =
            "com.example.lindenstirk.customalarmmanagerv6.EXTRA_ALARM_TIME_IN_MILLIS";


    private EditText editTextTitle;
    private TextView textViewTime;
    private TextView textViewDate;
    private TextView textViewState;

    public Button buttonAlarmStop;
    public Calendar calendar = Calendar.getInstance();
    public String alarmTimeInMillis;
    public int id;
    public String title;
    public String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        editTextTitle = findViewById(R.id.edit_text_title);
        textViewTime = findViewById(R.id.text_view_time);
        textViewDate = findViewById(R.id.text_view_date);
        textViewState = findViewById(R.id.text_view_state);

        getSupportActionBar().setHomeAsUpIndicator((R.drawable.ic_close));

        final Intent intent = getIntent();

        // sets an 'off' state if the alarm is new
        if (!intent.hasExtra(EXTRA_STATE)) {
            state = "0";
            textViewState.setText("State: " + state);
        }
        else {
            state = intent.getStringExtra(EXTRA_STATE);
        }

/*
        if (calendar.before(Calendar.getInstance())) {
            Toast.makeText(this, "Alarm has passed: " + state, Toast.LENGTH_SHORT).show();
            state = "2";
        }
*/
        //Toast.makeText(this, "State: " + state, Toast.LENGTH_SHORT).show();





        // if there's no EXTRA ID then it's the first time, thus you're adding an alarm
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Alarm");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            textViewTime.setText(intent.getStringExtra(EXTRA_TIME));
            textViewDate.setText(intent.getStringExtra(EXTRA_DATE));

            textViewState.setText("State: " + state);

        }
        else {
            setTitle("Add Alarm");
        }






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


        // Alarm On / Off
        final Button buttonAlarmOn = findViewById(R.id.alarm_on);
        final Button buttonAlarmOff = findViewById(R.id.alarm_off);
        buttonAlarmStop = findViewById(R.id.alarm_stop);





        if (state.equals("1")) {
            alarmButtonOn(buttonAlarmOn, buttonAlarmOff);
            buttonAlarmStop.setVisibility(View.VISIBLE);
        }

        else if (state.equals("0")) {
            alarmButtonOff(buttonAlarmOn, buttonAlarmOff);
            buttonAlarmStop.setVisibility(View.GONE);
        }

        else if (state.equals("2")) {
            alarmButtonOn(buttonAlarmOn, buttonAlarmOff);
            buttonAlarmStop.setVisibility(View.VISIBLE);
        }

        else {
            Toast.makeText(this, "Alarm is neither on nor off. It is: " + state, Toast.LENGTH_SHORT).show();
        }




        // Alarm On
        buttonAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            alarmOn(buttonAlarmOn, buttonAlarmOff);
            }
        });

        // Alarm Off
        buttonAlarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmOff(buttonAlarmOn, buttonAlarmOff);
            }
        });


        final Intent stopAlarmIntent = new Intent(this, AlarmReceiver.class);
        stopAlarmIntent.putExtra("EXTRA_ON/OFF", "off");


        buttonAlarmStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "0";
                textViewState.setText("State: " + state);

                // stop ringtone
                sendBroadcast(stopAlarmIntent);

            }
        });

    }




    private void saveAlarm() {
        String title = editTextTitle.getText().toString();
        String time = textViewTime.getText().toString();
        String date = textViewDate.getText().toString();

        alarmTimeInMillis = String.valueOf(calendar.getTimeInMillis());

        if (title.trim().isEmpty() || time.equals("time") || time.equals("date") ) {
            Toast.makeText(this, "Insert time and date", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO remove
        Toast.makeText(this, "Saved State:" + state, Toast.LENGTH_LONG).show();


        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_STATE, state);
        data.putExtra(EXTRA_ALARM_TIME_IN_MILLIS, alarmTimeInMillis);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        // only updates id if not -1
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();

        if (state.equals("1")) {
            startAlarm(calendar);
        }

        else {
            cancelAlarm();
        }

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
       // Calendar calendar = Calendar.getInstance();
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

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar calendar){

        title = editTextTitle.getText().toString();


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        // Extra string into intent for alarm title
        intent.putExtra("alarm_title", title);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);


        // sends intent to alarm manager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.cancel(pendingIntent);
        state = "0";

    }



    private void alarmButtonOn(Button button_on, Button button_off) {
        button_on.setBackgroundColor(Color.CYAN);
        button_off.setBackgroundColor(Color.WHITE);
     //   buttonAlarmStop.setVisibility(View.VISIBLE);
    }

    private void alarmButtonOff(Button button_on, Button button_off) {
        button_on.setBackgroundColor(Color.WHITE);
        button_off.setBackgroundColor(Color.CYAN);
     //   buttonAlarmStop.setVisibility(View.GONE);
    }

    private void alarmOn(Button button_on, Button button_off) {
       // button_on.setBackgroundColor(Color.CYAN);
       // button_off.setBackgroundColor(Color.WHITE);
        alarmButtonOn(button_on, button_off);
     //  buttonAlarmStop.setVisibility(View.VISIBLE);
        state = "1";
    }

    private void alarmOff(Button button_on, Button button_off) {
       // button_on.setBackgroundColor(Color.WHITE);
       // button_off.setBackgroundColor(Color.CYAN);
        alarmButtonOff(button_on, button_off);
      //  buttonAlarmStop.setVisibility(View.GONE);
        state = "0";
    }










    }












