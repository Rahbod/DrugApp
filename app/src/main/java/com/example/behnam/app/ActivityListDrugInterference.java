package com.example.behnam.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterListDrugInterference;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Drug2;
import com.example.behnam.app.helper.DbHelper;

import java.util.List;

public class ActivityListDrugInterference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView txt = findViewById(R.id.txt);
        txt.setText("داروی مورد نظر را انتخاب کنید");

        DbHelper dbHelper = new DbHelper(this);
        List<Drug> list = dbHelper.getListDrugInterference();
        AdapterListDrugInterference adapter = new AdapterListDrugInterference(this, list);
        RecyclerView recyclerView = findViewById(R.id.recFavorite);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
