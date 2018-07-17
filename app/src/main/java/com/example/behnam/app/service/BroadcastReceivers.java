package com.example.behnam.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.behnam.app.ActivityReminderDialog;
import com.example.behnam.app.helper.Components;
import com.example.behnam.app.helper.DbHelper;

import java.util.List;

public class BroadcastReceivers extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        assert action != null;

        switch (action) {

            // Connect to internet
            case ConnectivityManager.CONNECTIVITY_ACTION:
                Toast.makeText(context, "teeee", Toast.LENGTH_SHORT).show();
                ConnectivityManager connectivityManager;
                connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Components.getDrugs(context);
                }
                break;

            // Reminder broadcasts
            case "android.intent.action.BOOT_COMPLETED":
                DbHelper dbHelper = new DbHelper(context);
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
                int reminderID = intent.getIntExtra("reminderID", 0);
                if (reminderID > 0) {
                    Intent reminderDialog = new Intent(context, ActivityReminderDialog.class);
                    reminderDialog.putExtra("reminderID", reminderID);
                    reminderDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(reminderDialog);
                }
                break;
        }
    }
}