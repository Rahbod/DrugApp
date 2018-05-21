package com.example.behnam.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class ReminderService extends Service {
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings"; // Here you need to define the  package name

    private static final String SCREEN_CLASS_NAME = "com.android.settings.RunningServices"; // Here you need to define the class name but NOTICE!! you need to define its full name including  package name

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

        if (reminderID <= 0) {
            this.stopSelf();
        } else {
            DbHelper dbHelper = new DbHelper(getApplicationContext());
            intent.putExtra("reminderID", intent.getIntExtra("reminderID", 0));

            if (dbHelper.getReminder(reminderID) != null) {
                Reminder reminder = dbHelper.getReminder(reminderID);
                int runsNum = reminder.getRowCount();
                Calendar alarmTime = Calendar.getInstance();
                if (runsNum != reminder.getCount()) {
                    Intent broadcastIntent = new Intent(getApplicationContext(), BroadcastReceivers.class);
                    broadcastIntent.setAction("BROADCAST_RESTART_APP");
                    broadcastIntent.putExtra("reminderID", reminderID);

                    //create id for request code
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminderID, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(PendingIntent.getBroadcast(this, reminderID, broadcastIntent, 0));
                    long now = Calendar.getInstance().getTimeInMillis();
                    if (runsNum == 0) {
                        alarmTime.add(Calendar.SECOND, (int) ((reminder.getStartTime() - now) / 1000));
                    } else {
                        // alarmTime.add(Calendar.SECOND, reminder.getPeriodTime() * 3600);
                        alarmTime.add(Calendar.SECOND, 6);
                    }
                    dbHelper.incrementRowCountReminder(runsNum + 1, reminderID);
                    if (Build.VERSION.SDK_INT >= 23) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                alarmTime.getTimeInMillis(), pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
                    }
                   // alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
                }
            }
        }
        return START_STICKY;
    }
}