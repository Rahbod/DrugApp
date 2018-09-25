package ir.rahbod.sinadrug.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.rahbod.sinadrug.app.database.Reminder;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.app.service.ReminderService;

public class ActivityReminderDialog extends Activity {
    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.massage_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setBackgroundDrawableResource(R.drawable.background_reminder_dialog);

        DbHelper dbHelper = new DbHelper(this);
        LinearLayout linReminder = findViewById(R.id.linReminder);
        linReminder.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        int reminderID = intent.getIntExtra("reminderID", 0);
        TextView drugName = findViewById(R.id.txt);
        Button btnOk = findViewById(R.id.Ok);
        Reminder reminder = dbHelper.getReminder(reminderID);
        String str = dbHelper.getDrug(reminder.getDrugID()).getName();
        drugName.setText("زمان مصرف داروی " + str + " فرا رسیده است.");

        Intent intentService = new Intent(this, ReminderService.class);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
        }
        return false;
    }
}