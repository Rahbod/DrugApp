package com.example.behnam.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.fonts.ButtonFont;
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
        DbHelper dbHelper = new DbHelper(this);
        Reminder reminder = new Reminder();
        FontTextView drugName = findViewById(R.id.line2);
        FontTextView okButton = findViewById(R.id.ok_button);
        Intent intent = getIntent();
        int reminderID = intent.getIntExtra("reminderID", 0);
        reminder = dbHelper.getReminder(reminderID);
        drugName.setText(dbHelper.getDrug(reminder.getDrugId()).getName());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
