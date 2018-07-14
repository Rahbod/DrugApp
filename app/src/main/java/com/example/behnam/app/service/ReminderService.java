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
import com.example.behnam.reminder.ReminderModel;

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

        int reminderID = intent.getIntExtra("reminderID", 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (reminderID <= 0) {
            this.stopSelf();
        } else {
            DbHelper dbHelper = new DbHelper(getApplicationContext());
            intent.putExtra("reminderID", reminderID);
            ReminderModel reminder = dbHelper.getReminder(reminderID);
            if (reminder != null) {
                int showCount = reminder.getShowCount();
                Calendar alarmTime = Calendar.getInstance();
                if (showCount <= reminder.getCount()) {
                    Intent broadcastIntent = new Intent(getApplicationContext(), BroadcastReceivers.class);
                    broadcastIntent.setAction("BROADCAST_RESTART_APP");
                    broadcastIntent.putExtra("reminderID", reminderID);

                    //create id for request code
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminderID, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(PendingIntent.getBroadcast(this, reminderID, broadcastIntent, 0));
                    long now = Calendar.getInstance().getTimeInMillis();
                    if (showCount == 0) {
                        alarmTime.add(Calendar.SECOND, (int) ((reminder.getStartTime() - now) / 1000));
                    } else {
                        alarmTime.add(Calendar.SECOND, reminder.getPeriodTime() * 10);
                    }
                    dbHelper.incrementRowCountReminder(showCount + 1, reminderID);
                    if (Build.VERSION.SDK_INT >= 23) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                alarmTime.getTimeInMillis(), pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
                    }
                } else
                    onDestroy();
            }
        }
        return START_NOT_STICKY;
    }
}