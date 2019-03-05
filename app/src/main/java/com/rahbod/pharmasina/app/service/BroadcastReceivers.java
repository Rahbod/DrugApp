package com.rahbod.pharmasina.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.rahbod.pharmasina.app.ActivityReminderDialog;
import com.rahbod.pharmasina.app.database.Reminder;
import com.rahbod.pharmasina.app.helper.DbHelper;

import java.util.List;

public class BroadcastReceivers extends BroadcastReceiver {

    int reminderID;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        DbHelper dbHelper = new DbHelper(context);
        final String action = intent.getAction();
        assert action != null;
        switch (action) {
            // Connect to internet
//            case ConnectivityManager.CONNECTIVITY_ACTION:
//                ConnectivityManager connectivityManager;
//                connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//                if (networkInfo != null && networkInfo.isConnected()) {
//                    AppController.getInstance().getSQLiteDb(context);
//                }
//                break;

            // Reminder broadcasts
            case "android.intent.action.BOOT_COMPLETED":
                List<Integer> list = dbHelper.getCurrentReminders();
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        int id = list.get(i);
                        Intent serviceIntent = new Intent(context, ReminderService.class);
                        serviceIntent.putExtra("reminderID", id);
                        serviceIntent.putExtra("BOOT_COMPLETED", "Boot");
                        context.startService(serviceIntent);
                    }
                }
                break;
            case TelephonyManager.ACTION_PHONE_STATE_CHANGED:
                break;
            case "BROADCAST_RESTART_APP":
                reminderID = intent.getIntExtra("reminderID", 0);
                if (reminderID > 0) {
                    Intent reminderDialog = new Intent(context, ActivityReminderDialog.class);
                    reminderDialog.putExtra("reminderID", reminderID);
                    reminderDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Reminder reminder = dbHelper.getReminder(reminderID);
                    if (reminder.getId() == 0) {
                        break;
                    } else {
                        context.startActivity(reminderDialog);
                        break;
                    }
                }
                break;
        }
    }
}