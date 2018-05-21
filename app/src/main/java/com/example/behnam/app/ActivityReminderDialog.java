package com.example.behnam.app;

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

import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.service.ReminderService;
import com.example.behnam.fonts.FontTextView;

public class ActivityReminderDialog extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(android.R.style.Theme_Dialog);
        setContentView(R.layout.activity_reminder_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setBackgroundDrawableResource(R.drawable.radius);

        DbHelper dbHelper = new DbHelper(getApplicationContext());
        Intent intent = getIntent();
        int reminderID = intent.getIntExtra("reminderID", 0);
        FontTextView drugName = findViewById(R.id.line2);
        FontTextView okButton = findViewById(R.id.ok_button);
        Reminder reminder = dbHelper.getReminder(reminderID);
        drugName.setText(dbHelper.getDrug(reminder.getDrugID()).getName());

        Intent intentService = new Intent(ActivityReminderDialog.this, ReminderService.class);
        intentService.putExtra("reminderID", reminderID);
        startService(intentService);
        okButton.setOnClickListener(new View.OnClickListener() {
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
}