package com.example.behnam.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.behnam.app.adapter.AdapterListReminder;
import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;

import java.util.List;

public class ReminderListActivity extends AppCompatActivity {
    List<Reminder> reminderList;
    RecyclerView recyclerView;
    AdapterListReminder adapterListReminder;
    DbHelper dbHelper;
    ImageView btnBack;

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

        dbHelper = new DbHelper(this);
        reminderList = dbHelper.getAllReminder();
        adapterListReminder = new AdapterListReminder(reminderList, this, dbHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterListReminder);
    }
}