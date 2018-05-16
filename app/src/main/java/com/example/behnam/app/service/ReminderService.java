package com.example.behnam.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class ReminderService extends Service {
    private static WeakReference<ReminderService> instance;

    @Nullable
    public static ReminderService getInstance() {
        return instance == null ? null : instance.get();
    }

    @Override
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        instance = new WeakReference<>(this);
        // Convert start time to second

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int reminderID = intent.getIntExtra("reminderID", 0);
        boolean shouldClose = intent.getBooleanExtra("close", false);
        if (shouldClose) {
            this.stopSelf();
        } else {
            if (reminderID == 0) {
                Log.e("TAG", "onStartCommand: ");
            } else {
                DbHelper dbHelper = new DbHelper(getApplicationContext());
                intent.putExtra("reminderID", intent.getIntExtra("reminderID", 0));

                if (dbHelper.getReminder(reminderID) != null) {
                    Reminder reminder = dbHelper.getReminder(reminderID);
                    int runsNum = reminder.getRow_count();
                    Calendar alarmTime = Calendar.getInstance();
                    if (runsNum != reminder.getCount()) {
                        Intent broadcastIntent = new Intent(getApplicationContext(), BroadcastReceivers.class);
                        broadcastIntent.setAction("BROADCAST_RESTART_APP");
                        broadcastIntent.putExtra("reminderID", reminderID);

                        //create id for request code
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminderID, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        long now = Calendar.getInstance().getTimeInMillis();
                        if (runsNum == 0) {
                            alarmTime.add(Calendar.SECOND, (int) ((reminder.getStart_time() - now) / 1000));
                        } else {
                            //long nextTime = reminder.getPeriod_time() * (1000);
                            alarmTime.add(Calendar.SECOND, 10);
                        }
                        dbHelper.incrementRowCountReminder(runsNum + 1, reminderID);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
                    }
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.stopSelf();
    }
}