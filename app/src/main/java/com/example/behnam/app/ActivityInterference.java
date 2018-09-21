package com.example.behnam.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterCategories;
import com.example.behnam.app.adapter.AdapterInterferenceDrug;
import com.example.behnam.app.adapter.AdapterTabBar;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Index;
import com.example.behnam.app.helper.Components;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;
import com.example.behnam.app.map.MapActivity;
import com.example.behnam.fragment.FragmentCategory;
import com.example.behnam.fragment.FragmentDrug;
import com.example.behnam.fragment.FragmentInterferenceDrug;
import com.example.behnam.fragment.FragmentViewDrug;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityInterference extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private static String currentTab = "Drug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interference);

        if (SessionManager.getExtrasPref(this).getString("interferenceIdList") != null) {
            SessionManager.getExtrasPref(this).remove("interferenceIdList");
        }

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(getIntent().getStringExtra("name"));
        drawerLayout = findViewById(R.id.DrawerLayout);
        ImageView imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);

        imgOpenNvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }, 100);
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

//        TabLayout tabLayout = findViewById(R.id.tabBar);
//        ViewPager viewPager = findViewById(R.id.viewPager);
//        tabLayout.setupWithViewPager(viewPager);
//        setupViewPager(viewPager);
//        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager(), this);
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setCustomView(adapter.getTabView(i));
//        }

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                currentTab = (String) tab.getText();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        //حذف این قسمت در هنگام تداخل با طبقه بندی
        int ID = getIntent().getIntExtra("id", 0);

        //set recyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DbHelper dbHelper = new DbHelper(this);
        List<Index> list = dbHelper.getAllInterferenceDrug(ID);
        AdapterInterferenceDrug adapter = new AdapterInterferenceDrug(this, list);
        recyclerView.setAdapter(adapter);

        if (list.isEmpty()){
            TextView txt = findViewById(R.id.txt);
            recyclerView.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
        }
    }

//    private void setupViewPager(ViewPager viewPager) {
//        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager());
//        adapter.addFragment(new FragmentDrug(), "Drug");
//        adapter.addFragment(new FragmentCategory(), "Category");
//        viewPager.setAdapter(adapter);
//    }

    public void openNv(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.item1:
                onBackPressed();
                closeNv();
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
                Intent intentAbout = new Intent(this, ActivityAbout.class);
                intentAbout.putExtra("type", "about");
                startActivity(intentAbout);
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
                Intent intentRules = new Intent(this, ActivityAbout.class);
                intentRules.putExtra("type", "rules");
                startActivity(intentRules);
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else if (currentTab.equals("Category")) {
            if (!FragmentCategory.goBack(this))
                super.onBackPressed();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}