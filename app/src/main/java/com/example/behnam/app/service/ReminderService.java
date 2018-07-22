package com.example.behnam.app.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.reminder.ReminderModel;

import java.lang.ref.WeakReference;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderService extends IntentService {

    private static WeakReference<ReminderService> instance;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ReminderService() {
        super("ReminderService");
    }

    @Nullable
    public static ReminderService getInstance() {
        return instance == null ? null : instance.get();
    }

    @Override
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = new WeakReference<>(this);
        // Convert start time to second

        int reminderID = intent.getIntExtra("reminderID", 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        DbHelper dbHelper = new DbHelper(getApplicationContext());
        intent.putExtra("reminderID", reminderID);
        ReminderModel reminder = dbHelper.getReminder(reminderID);
        Intent broadcastIntent = new Intent(getApplicationContext(), BroadcastReceivers.class);
        broadcastIntent.setAction("BROADCAST_RESTART_APP");
        broadcastIntent.putExtra("reminderID", reminderID);
        Calendar alarmTime = Calendar.getInstance();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminderID, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        long now = System.currentTimeMillis();
        long periodTime = reminder.getPeriodTime() * 360000;
        long endTime = (reminder.getStartTime() + ((reminder.getCount() - 1) * periodTime));

        if (reminder.getShowCount() < reminder.getCount()) {
            if (now >= endTime) {
                dbHelper.incrementRowCountReminder(reminder.getCount(), reminderID);
            } else if (now < reminder.getStartTime()) {
                alarmTime.add(Calendar.MILLISECOND, (int) (reminder.getStartTime() - now));
            } else {
                int showCount = (int) (((now - reminder.getStartTime()) / periodTime) + 1);
                long next = reminder.getStartTime() + (showCount * periodTime);
                dbHelper.incrementRowCountReminder(showCount, reminderID);
                alarmTime.add(Calendar.MILLISECOND, (int) (next - now));
            }

            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        alarmTime.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
            }
        } else {
            this.stopSelf();
        }
        return START_NOT_STICKY;
    }
}