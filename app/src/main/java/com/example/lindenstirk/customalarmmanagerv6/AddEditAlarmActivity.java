package com.example.lindenstirk.customalarmmanagerv6;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import org.w3c.dom.Text;

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

    private EditText editTextTitle;
    private TextView textViewTime;
    private TextView textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        editTextTitle = findViewById(R.id.edit_text_title);
        textViewTime = findViewById(R.id.text_view_time);
        textViewDate = findViewById(R.id.text_view_date);

        getSupportActionBar().setHomeAsUpIndicator((R.drawable.ic_close));

        Intent intent = getIntent();

        // if there's no EXTRA ID then it's the first time, thus you're adding an alarm
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Alarm");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            textViewTime.setText(intent.getStringExtra(EXTRA_TIME));
            textViewDate.setText(intent.getStringExtra(EXTRA_DATE));
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
    }




    private void saveAlarm() {
        String title = editTextTitle.getText().toString();
        String time = textViewTime.getText().toString();
        String date = textViewDate.getText().toString();

        if (title.trim().isEmpty() || time.trim().isEmpty() || date.trim().isEmpty()) {
            Toast.makeText(this, "Insert a title, time, and date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_DATE, date);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);

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

        // TODO need to format date correctly with leading zeros for 24-clock

        // Makes sure there are two zeros if minute = 0
        if (minute == 0) {
            textView.setText(hourOfDay + ":" + minute + 0);
        }
        else {
            textView.setText(hourOfDay + ":" + minute);
        }



        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

       // updateTimeText(calendar);
       // startAlarm(calendar);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar calendar){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1); // if time has already passed
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


}
