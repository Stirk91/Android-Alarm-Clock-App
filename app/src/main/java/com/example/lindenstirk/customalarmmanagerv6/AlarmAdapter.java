package com.example.lindenstirk.customalarmmanagerv6;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    public Alarm getNoteAt(int index) {
        return alarms.get(index);
    }


    class AlarmHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewTime;
        private TextView textViewDate;

        public AlarmHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewDate = itemView.findViewById(R.id.text_view_date);

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

    public void setOnAlarmClickListner(OnAlarmClickListener listner) {
        this.listener = listner;
    }


}