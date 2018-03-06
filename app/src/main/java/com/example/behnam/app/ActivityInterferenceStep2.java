package com.example.behnam.app;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.behnam.app.adapter.AdapterInterferenceStep2;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;

import java.util.List;

public class ActivityInterferenceStep2 extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interference_step_2);

        DbHelper dbHelper = new DbHelper(this);

        List<Drug> drugList = dbHelper.getAllDrugs();

        RecyclerView recyclerView = findViewById(R.id.recDrugInteraction2);
        AdapterInterferenceStep2 adapter = new AdapterInterferenceStep2(this, drugList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}