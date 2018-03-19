package com.example.behnam.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;

public class ActivityViewDrug extends AppCompatActivity {
    DbHelper dbHelper ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drug);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        dbHelper = new DbHelper(this);

        Drug thisDrug = dbHelper.getDrug(id);
        dbHelper.getCategoriesByDrug(intent.getIntExtra("id",0));
    }
}
