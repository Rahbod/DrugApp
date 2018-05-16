package com.example.behnam.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.behnam.app.ActivityReminderDialog;
import com.example.behnam.app.helper.Components;

public class BroadcastReceivers extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("mohammadmoien", "111111111111");
        final String action = intent.getAction();
        assert action != null;
        switch (action) {
            // Connect to internet
            case ConnectivityManager.CONNECTIVITY_ACTION:
                ConnectivityManager connectivityManager;
                connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
                if (networkInfo != null && networkInfo.isConnected()) {
                    Components.getDrugs(context);
                }
                break;

            // Reminder broadcasts
            case Intent.ACTION_BOOT_COMPLETED:
                break;
            case TelephonyManager.ACTION_PHONE_STATE_CHANGED:
                break;
//            case Intent.ACTION_BOOT_COMPLETED:
            case "BROADCAST_RESTART_APP":
                if (intent != null && context != null) {
                    context.stopService(new Intent(context.getApplicationContext(), ReminderService.class));
                    int reminderID = intent.getIntExtra("reminderID", 0);
                    if (reminderID > 0) {
                        Intent reminderDialog = new Intent(context, ActivityReminderDialog.class);
                        reminderDialog.putExtra("reminderID", reminderID);
                        reminderDialog.putExtra("startID",intent.getIntExtra("startID",1000));
                        reminderDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(reminderDialog);
                    }
                }
                break;
        }
    }
}