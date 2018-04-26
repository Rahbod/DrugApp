package com.example.behnam.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.behnam.app.ActivityHome;
import com.example.behnam.app.ActivityReminderDialog;
import com.example.behnam.app.helper.Components;

public class BroadcastReceivers extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

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
            case TelephonyManager.ACTION_PHONE_STATE_CHANGED:
            case "BROADCAST_RESTART_APP":
                Intent intentService = new Intent(context, ReminderService.class);
                int reminderID = intent.getIntExtra("reminderID", 0);

                intentService.putExtra("reminderID", reminderID);
                Intent reminderDialog = new Intent(context, ActivityReminderDialog.class);
                reminderDialog.putExtra("reminderID", reminderID);
                reminderDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(intentService);
                context.startActivity(reminderDialog);
                break;
        }
    }
}