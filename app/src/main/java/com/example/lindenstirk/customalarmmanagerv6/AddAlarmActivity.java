package com.example.lindenstirk.customalarmmanagerv6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddAlarmActivity extends AppCompatActivity {
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
        setTitle("Add Note");
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
}
