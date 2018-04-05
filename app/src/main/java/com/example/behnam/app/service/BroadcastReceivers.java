package com.example.behnam.app.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.behnam.app.ActivityErrorReport;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BroadcastReceivers extends BroadcastReceiver {
    private static final int AZAN_NOTIFICATION_ID = 1;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (intent != null && intent.getAction() != null && !TextUtils.isEmpty(intent.getAction())) {
            String action = intent.getAction();
            Log.e("masoud", "broadcast execute");
            if (action.equals(Intent.ACTION_BOOT_COMPLETED) ||
                    action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED) ||
                    action.equals("BROADCAST_RESTART_APP")) {
//                if (!SessionManager.getExtrasPref(context).getBoolean("ReminderIsStarted")) {
                    context.startService(new Intent(context, ReminderService.class));
//                }
                Intent intent1 = new Intent(context, ActivityErrorReport.class);
                context.startActivity(intent1);

            } else if (action.equals(Intent.ACTION_TIME_TICK) ||
                    action.equals(Intent.ACTION_SCREEN_ON)) {
                //update alarm
                //updateUtils.update(false);

            } else if (action.equals(Intent.ACTION_TIME_CHANGED)){
                //update alarm
                //updateUtils.update(true);

            }else if (action.equals(Intent.ACTION_DATE_CHANGED) ||
                    action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {

//                updateUtils.update(true);
//                utils.loadApp();
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent("LOCAL_INTENT_DAY_PASSED"));

            } else if (action.equals("BROADCAST_ALARM_FAJR")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 5);
                int current = (calendar.get(Calendar.HOUR_OF_DAY)*100) + calendar.get(Calendar.MINUTE);

                Integer time = 5;

                if (time < current){
                    startAthanActivity("FAJR");
                }

            } else if (action.equals("BROADCAST_ALARM_DHUHR")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 5);
                int current = (calendar.get(Calendar.HOUR_OF_DAY)*100) + calendar.get(Calendar.MINUTE);

                Integer time = 12;

                if (time != null){
                    if (time < current){
                        startAthanActivity("DHUHR");
                    }
                }
            } else if (action.equals("BROADCAST_ALARM_MAGHRIB")) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 5);
                int current = (calendar.get(Calendar.HOUR_OF_DAY)*100) + calendar.get(Calendar.MINUTE);

                Integer time = 19;

                if (time != null){
                    if (time < current){
                        startAthanActivity("MAGHRIB");
                    }
                }
            } else if(action.equals("notification_cancelled")) {
                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                //utils.stop();
                manager.cancel(AZAN_NOTIFICATION_ID);
            }
        }
    }

    private void startAthanActivity(String name) {
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        boolean isSceenAwake = (Build.VERSION.SDK_INT < 20? powerManager.isScreenOn():powerManager.isInteractive());
        if( isSceenAwake) {
            Log.e("masoud", "azanNotification");
            //utils.azanNotification(true, name);
        } else {
            Log.e("masoud", "start azan activity");
//            Intent intent = new Intent(context, AthanActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Constants.KEY_EXTRA_PRAYER_KEY, name);
//            context.startActivity(intent);
        }
    }
}
