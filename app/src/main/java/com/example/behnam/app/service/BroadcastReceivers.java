package com.example.behnam.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.behnam.app.ActivityHome;
import com.example.behnam.app.ActivityReminderDialog;

public class BroadcastReceivers extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("TAG", "onReceive: broadcast!!!!!!!!");
        final String action = intent.getAction();
        assert action != null;
        switch (action) {
            // Connect to internet
            case ConnectivityManager.CONNECTIVITY_ACTION:
                Toast.makeText(context, "You are online!", Toast.LENGTH_SHORT).show();
                ActivityHome activityHome = new ActivityHome();
                activityHome.showDrugs(context);
                break;

            // Reminder broadcasts
            case Intent.ACTION_BOOT_COMPLETED:
            case TelephonyManager.ACTION_PHONE_STATE_CHANGED:
            case "BROADCAST_RESTART_APP":
                Intent reminderDialog = new Intent(context, ActivityReminderDialog.class);
                context.startActivity(reminderDialog);
                break;
        }
    }
}