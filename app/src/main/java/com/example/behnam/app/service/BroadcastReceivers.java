package com.example.behnam.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.example.behnam.app.ActivityHome;
import com.example.behnam.app.ActivitySplashScreen;
import com.example.behnam.app.helper.Components;

public class BroadcastReceivers extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        assert action != null;
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Toast.makeText(context, "You are online!", Toast.LENGTH_SHORT).show();
            Components components = new Components();
            components.getDrugs(context);
        }
    }
}