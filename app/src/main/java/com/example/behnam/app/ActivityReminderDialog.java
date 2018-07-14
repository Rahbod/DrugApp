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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.service.ReminderService;
import com.example.behnam.fonts.FontTextView;
import com.example.behnam.reminder.ReminderModel;

public class ActivityReminderDialog extends Activity {
    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.massage_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setBackgroundDrawableResource(R.drawable.background_reminder_dialog);

        LinearLayout linReminder = findViewById(R.id.linReminder);
        linReminder.setVisibility(View.VISIBLE);

        final DbHelper dbHelper = new DbHelper(getApplicationContext());
        Intent intent = getIntent();
        final int reminderID = intent.getIntExtra("reminderID", 0);
        TextView drugName = findViewById(R.id.txt);
        Button btnOk = findViewById(R.id.Ok);
        ReminderModel reminder = dbHelper.getReminder(reminderID);
        String str = dbHelper.getDrug(reminder.getDrugID()).getName();
        drugName.setText("زمان مصرف داروی " + str + " فرا رسیده است.");

        Intent intentService = new Intent(ActivityReminderDialog.this, ReminderService.class);
        intentService.putExtra("reminderID", reminderID);
        startService(intentService);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("behnam", String.valueOf(dbHelper.getCurrentReminders()));
                ReminderModel reminderModel = dbHelper.getReminder(reminderID);
                Log.e("behnam", "showCount= " + reminderModel.getShowCount());
                Log.e("behnam", "Count= " + reminderModel.getShowCount());
                Log.e("behnam", "periodTime= " + reminderModel.getPeriodTime());
                Log.e("behnam", "startTime= " + reminderModel.getStartTime());
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
}