package com.example.behnam.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.behnam.app.adapter.AdapterFavorite;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivityFavorite extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView recyclerView;
    private List<Drug> list;
    private AdapterFavorite adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DbHelper dbHelper = new DbHelper(this);
        dbHelper.updateDrug(56);
        dbHelper.updateDrug(60);
        dbHelper.updateDrug(53);

        list = dbHelper.getFavorite();
        adapter = new AdapterFavorite(this, list);
        recyclerView = findViewById(R.id.recFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
