package com.example.lindenstirk.customalarmmanagerv6;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AlarmRepository {
    private AlarmDao alarmDao;
    private LiveData<List<Alarm>> allAlarms;

    public AlarmRepository(Application application) {
        AlarmDatabase database = AlarmDatabase.getInstance(application);
        alarmDao = database.alarmDao();
        allAlarms = alarmDao.getAllAlarms();
    }

    public void insert(Alarm alarm) {
        new InsertAlarmAsyncTask(alarmDao).execute(alarm);
    }

    public void update(Alarm alarm) {
        new UpdateAlarmAsyncTask(alarmDao).execute(alarm);
    }

    public void delete(Alarm alarm) {
        new DeleteAlarmAsyncTask(alarmDao).execute(alarm);
    }

    public void deleteAllAlarms() {
        new DeleteAllAlarmsAsyncTask(alarmDao).execute();
    }

    public LiveData<List<Alarm>> getAllAlarms() {
        return allAlarms;
    }

    private static class InsertAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao alarmDao;

        private InsertAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(Alarm... alarms) {
            alarmDao.insert(alarms[0]);
            return null;
        }
    }

    private static class UpdateAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao alarmDao;

        private UpdateAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(Alarm... alarms) {
            alarmDao.update(alarms[0]);
            return null;
        }
    }

    private static class DeleteAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao alarmDao;

        private DeleteAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(Alarm... alarms) {
            alarmDao.delete(alarms[0]);
            return null;
        }
    }

    private static class DeleteAllAlarmsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AlarmDao alarmDao;

        private DeleteAllAlarmsAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmDao.deleteAllAlarms();
            return null;
        }
    }
}