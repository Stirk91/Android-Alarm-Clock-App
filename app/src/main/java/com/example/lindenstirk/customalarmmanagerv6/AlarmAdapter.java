package com.example.lindenstirk.customalarmmanagerv6;



import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
    private List<Alarm> alarms = new ArrayList<>();
    private OnAlarmClickListener listener;

    @NonNull
    @Override
    public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_item, parent, false);
        return new AlarmHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmHolder holder, int position) {
        Alarm currentAlarm = alarms.get(position);
        holder.textViewTitle.setText(currentAlarm.getTitle());
        holder.textViewTime.setText(currentAlarm.getTime());
        holder.textViewDate.setText(String.valueOf(currentAlarm.getDate()));


        long currentTime = Calendar.getInstance().getTimeInMillis();
        long alarmTimeInMilli = Long.parseLong(currentAlarm.getAlarmTimeInMilli());

        Log.d(TAG, "      LogCurrentTime = " + currentTime);
        Log.d(TAG, " LogAlarmTimeInMilli = " + alarmTimeInMilli);


        // Icons to for alarm state

        if (currentAlarm.getState().equals("0"))
            alarmOff(holder.alarmIconRingingBlack, holder.alarmIconRingingRed, holder.alarmIconSet, holder.alarmIconOff);

        /*
        else if ( (currentTime > alarmTimeInMilli) || (currentAlarm.getState().equals("2")) ) {
            alarmRinging(holder.alarmIconRingingBlack, holder.alarmIconRingingRed, holder.alarmIconSet, holder.alarmIconOff);
            currentAlarm.setState("2");
        }

        else if ( (currentTime < alarmTimeInMilli) || (currentAlarm.getState().equals("1")) ) {
            alarmSet(holder.alarmIconRingingBlack, holder.alarmIconRingingRed, holder.alarmIconSet, holder.alarmIconOff);
            currentAlarm.setState("1");
        }
*/

    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    public Alarm getAlarmAt(int index) {
        return alarms.get(index);
    }


    class AlarmHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewTime;
        private TextView textViewDate;
        private ImageView alarmIconRingingBlack;
        private ImageView alarmIconRingingRed;
        private ImageView alarmIconSet;
        private ImageView alarmIconOff;

        public AlarmHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            alarmIconRingingBlack = itemView.findViewById(R.id.icon_ringing_black);
            alarmIconRingingRed = itemView.findViewById(R.id.icon_ringing_red);
            alarmIconSet = itemView.findViewById(R.id.icon_set);
            alarmIconOff = itemView.findViewById(R.id.icon_off);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    // checks to make sure index exists
                    if (listener != null && index != RecyclerView.NO_POSITION) {
                        listener.onAlarmClick(alarms.get(index));
                    }
                }
            });
        }
    }

    public interface OnAlarmClickListener {
        void onAlarmClick(Alarm alarm);
    }

    public void setOnAlarmClickListener(OnAlarmClickListener listener) {
        this.listener = listener;
    }


    public void alarmRinging(final ImageView alarm_ringing_black, final ImageView alarm_ringing_red,
                         final ImageView alarm_set, final ImageView alarm_off) {

        alarm_ringing_red.setAlpha(1f);
        alarm_ringing_black.setAlpha(1f);
        alarm_set.setAlpha(0f);
        alarm_off.setAlpha(0f);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;

        alarm_ringing_black.startAnimation(fadeIn);
        alarm_ringing_red.startAnimation(fadeOut);
        fadeIn.setDuration(1000);
        fadeOut.setDuration(1000);
        fadeIn.setRepeatCount(Animation.INFINITE);
        fadeOut.setRepeatCount(Animation.INFINITE);

    }

    public void alarmSet(final ImageView alarm_ringing_black, final ImageView alarm_ringing_red,
                         final ImageView alarm_set, final ImageView alarm_off) {
        alarm_ringing_black.setAlpha(0f);
        alarm_ringing_red.setAlpha(0f);
        alarm_set.setAlpha(1f);
        alarm_off.setAlpha(0f);
    }

    public void alarmOff(final ImageView alarm_ringing_black, final ImageView alarm_ringing_red,
                         final ImageView alarm_set, final ImageView alarm_off) {
        alarm_ringing_black.setAlpha(0f);
        alarm_ringing_red.setAlpha(0f);
        alarm_set.setAlpha(0f);
        alarm_off.setAlpha(1f);
    }


    private Date convertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return convertedDate;
    }


    private long convertTimeToMilli(String timeString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:MM:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(timeString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        long timeInMilli = convertedDate.getTime();

        return timeInMilli;
    }




}

