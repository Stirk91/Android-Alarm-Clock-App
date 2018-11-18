package com.example.lindenstirk.customalarmmanagerv6;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "alarm_table")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String time;
    private String date;
    private String state;
    private String alarmTimeInMilli;

    public Alarm(String title, String time, String date, String state, String alarmTimeInMilli) {
        this.title = title;
        this.time = time;
        this.date = date;
        this.state = state;
        this.alarmTimeInMilli = alarmTimeInMilli;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(String state) { this.state = state; }

    public void setAlarmTimeInMilli(String alarmTimeInMilli) {this.alarmTimeInMilli = alarmTimeInMilli; }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDate() { return date; }

    public String getState() { return state; }

    public String getAlarmTimeInMilli() { return alarmTimeInMilli; }

}