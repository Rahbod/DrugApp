package com.example.behnam.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterListDrugInterference;
import com.example.behnam.app.adapter.AdapterListInterference;
import com.example.behnam.app.adapter.AdapterReminder;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Index;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.map.MapActivity;

import java.io.File;
import java.util.List;

public class ActivityListDrugInterference extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // navigationView
        drawerLayout = findViewById(R.id.DrawerLayout);
        ImageView imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
        imgOpenNvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

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
        List<Index> list = dbHelper.getListDrugInterference();
        AdapterListInterference adapter = new AdapterListInterference(this, list);
        RecyclerView recyclerView = findViewById(R.id.recFavorite);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void openNv(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.item1:
                drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.item2:
                startActivity(new Intent(this, MapActivity.class));
                closeNv();
                break;
            case R.id.item3:
                Intent goToReminder = new Intent(this, ActivityReminderList.class);
                startActivity(goToReminder);
                closeNv();
                break;
            case R.id.item4:
                Intent goToFavorite = new Intent(this, ActivityFavorite.class);
                startActivity(goToFavorite);
                closeNv();
                break;
            case R.id.item5:
                shareApplication();
                closeNv();
                break;
            case R.id.item6:
                Intent goToErrorReport = new Intent(this, ActivityErrorReport.class);
                startActivity(goToErrorReport);
                closeNv();
                break;
            case R.id.item7:
                Intent goToAbout = new Intent(this, ActivityAbout.class);
                startActivity(goToAbout);
                closeNv();
                break;
            case R.id.item8:
                Intent intentVegetalDrug = new Intent(this, ActivityDrug.class);
                startActivity(intentVegetalDrug);
                closeNv();
                break;
            case R.id.item9:
                Intent intentDrug = new Intent(this, ActivityHome.class);
                startActivity(intentDrug);
                closeNv();
                break;
            case R.id.item10:
                Intent intentSearch = new Intent(this, ActivityDrug.class);
                intentSearch.putExtra("search", "search");
                startActivity(intentSearch);
                closeNv();
                break;
            case R.id.item11:
                Intent intentPharma = new Intent(this, ActivityCategories.class);
                intentPharma.putExtra("type", 1);
                startActivity(intentPharma);
                closeNv();
                break;
            case R.id.item12:
                Intent intentHealing = new Intent(this, ActivityCategories.class);
                intentHealing.putExtra("type", 0);
                startActivity(intentHealing);
                closeNv();
                break;
        }
    }

    private void closeNv() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        }, 250);
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "Share app via"));
    }
}
