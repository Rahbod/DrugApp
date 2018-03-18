package com.example.behnam.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.behnam.app.adapter.AdapterInterferenceStep3;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActivityInterferenceStep3 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterInterferenceStep3 adapter;
    private List<Drug> list;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interference_step3);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DbHelper dbHelper = new DbHelper(this);

        recyclerView = findViewById(R.id.recInterferenceStep3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String strConflicts = SessionManager.getExtrasPref(this).getString("conflicts");
        JSONObject jsonList = null;
        try {
            jsonList = new JSONObject(strConflicts);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Iterator<String> keys = jsonList.keys();
        JSONArray arrayKey = new JSONArray();
        while (keys.hasNext()) {
            String strKey = keys.next();
            arrayKey.put(strKey);
        }
        list = dbHelper.getDrugs(arrayKey);
        adapter = new AdapterInterferenceStep3(this, list);
        recyclerView.setAdapter(adapter);
    }
}
