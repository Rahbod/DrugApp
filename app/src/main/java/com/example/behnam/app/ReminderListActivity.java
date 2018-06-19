package com.example.behnam.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.behnam.app.adapter.AdapterListReminder;
import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.fonts.FontTextView;

import java.util.List;

public class ReminderListActivity extends AppCompatActivity {
    List<Reminder> reminderList;
    RecyclerView recyclerView;
    AdapterListReminder adapterListReminder;
    DbHelper dbHelper;
    ImageView btnBack;
    FloatingActionButton floatAddReminder;
    FontTextView noAlarmTitle;
    ImageView noAlarmPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView = findViewById(R.id.list_reminder_recycler);
        floatAddReminder = findViewById(R.id.float_add_reminder);
        noAlarmPic = findViewById(R.id.no_alarm_pic);
        noAlarmTitle = findViewById(R.id.no_alarm_title);

        floatAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReminderListActivity.this, ActivityReminderStep1.class));
            }
        });

        dbHelper = new DbHelper(this);
        reminderList = dbHelper.getAllReminder();
        checkReminder();
    }

    public void checkReminder()
    {
        if (reminderList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noAlarmPic.setVisibility(View.GONE);
            noAlarmTitle.setVisibility(View.GONE);
            adapterListReminder = new AdapterListReminder(reminderList, this, dbHelper);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapterListReminder);
        } else {
            noAlarmPic.setVisibility(View.VISIBLE);
            noAlarmTitle.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}