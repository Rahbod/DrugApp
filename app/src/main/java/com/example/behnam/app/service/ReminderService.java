package com.example.behnam.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(new BroadcastReceivers(), intentFilter);

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Calendar startTime = Calendar.getInstance();
        startTime.add(Calendar.SECOND, 10);
        Intent intent1 = new Intent(getApplicationContext(), BroadcastReceivers.class);
        intent1.setAction("BROADCAST_RESTART_APP");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), pendingIntent);

        return START_STICKY;
    }
}
