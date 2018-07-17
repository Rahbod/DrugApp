package com.example.behnam.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.service.ReminderService;
import com.example.behnam.reminder.ReminderModel;

import java.sql.Time;
import java.text.SimpleDateFormat;

public class ActivityReminderDialog extends Activity {
    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.massage_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setBackgroundDrawableResource(R.drawable.background_reminder_dialog);

        final DbHelper dbHelper = new DbHelper(getApplicationContext());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss aa");
        String dateString = formatter.format(new Time(System.currentTimeMillis()));

        LinearLayout linReminder = findViewById(R.id.linReminder);
        linReminder.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        final int reminderID = intent.getIntExtra("reminderID", 0);
        TextView drugName = findViewById(R.id.txt);
        Button btnOk = findViewById(R.id.Ok);
        final ReminderModel reminder = dbHelper.getReminder(reminderID);
        String str = dbHelper.getDrug(reminder.getDrugID()).getName();
        drugName.setText("زمان مصرف داروی " + str + " فرا رسیده است.");

        Intent intentService = new Intent(ActivityReminderDialog.this, ReminderService.class);
        intentService.putExtra("reminderID", reminderID);
        startService(intentService);
        // add one to showCount
        dbHelper.incrementRowCountReminder(reminder.getShowCount() + 1, reminderID);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set Notification sound for Alarm
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification != null) {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
            mp.start();
        }
    }

    public String getTimes(long time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss aa");
        return formatter.format(new Time(time));
    }
}