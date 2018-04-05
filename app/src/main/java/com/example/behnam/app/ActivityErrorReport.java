package com.example.behnam.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.behnam.app.helper.DbHelper;

import static com.example.behnam.app.ActivityReminderStep2.isAppAvailable;

public class ActivityErrorReport extends AppCompatActivity {
    DbHelper dbHelper ;
    String appName = "org.telegram.messenger";
    Button telegramBtn, favoriteBtn ;
    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_report);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        telegramBtn= findViewById(R.id.telegram);
        favoriteBtn= findViewById(R.id.favorite);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new DbHelper(getApplicationContext());

        telegramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isAppInstalled = isAppAvailable(ActivityErrorReport.this, appName);
                if (isAppInstalled)
                {
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    myIntent.setPackage(appName);
                    myIntent.putExtra(Intent.EXTRA_TEXT, "متن تلگرام");//
                    startActivity(Intent.createChooser(myIntent, "Share with"));
                }
                else
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=org.telegram.messenger"));
                    startActivity(i);
                }
            }
        });
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dbHelper.updateDrug();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}