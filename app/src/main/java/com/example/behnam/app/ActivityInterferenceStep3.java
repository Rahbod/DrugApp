package com.example.behnam.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterInterferenceStep3;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ActivityInterferenceStep3 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterInterferenceStep3 adapter;
    private List<Drug> list;
    private ImageView btnBack;
    TextView txtInterference3;

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

        String name = SessionManager.getExtrasPref(this).getString("mainName");

        txtInterference3 = findViewById(R.id.txtInterference3);
        txtInterference3.setText(name);


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

//        sort item
        Collections.sort(list, new Comparator<Drug>() {
            @Override
            public int compare(Drug o1, Drug o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        adapter = new AdapterInterferenceStep3(this, list);
        recyclerView.setAdapter(adapter);
    }
}
