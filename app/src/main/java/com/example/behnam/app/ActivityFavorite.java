package com.example.behnam.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.behnam.app.adapter.AdapterFavorite;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;

import java.util.List;

public class ActivityFavorite extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView recyclerView;
    private List<Drug> list;
    private AdapterFavorite adapter;
    private DbHelper dbHelper;
    LinearLayout linFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        linFavorite = findViewById(R.id.linFavorite);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dbHelper = new DbHelper(this);

        list = dbHelper.getFavorite();
        adapter = new AdapterFavorite(this, list);
        recyclerView = findViewById(R.id.recFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setBackGround();
    }

    public void setBackGround() {
        if (list.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            linFavorite.setVisibility(View.VISIBLE);
        } else {
            linFavorite.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        list = dbHelper.getFavorite();
        adapter = new AdapterFavorite(this, list);
        recyclerView = findViewById(R.id.recFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setBackGround();
    }
}
