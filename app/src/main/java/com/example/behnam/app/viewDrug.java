package com.example.behnam.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.behnam.app.adapter.AdapterTabBar;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.map.MapActivity;
import com.example.behnam.fonts.FontTextView;
import com.example.behnam.fonts.FontTextViewBold;
import com.example.behnam.fragment.FragmentInterferenceDrug;
import com.example.behnam.fragment.FragmentViewDrug;

import java.io.File;

public class viewDrug extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drug);

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

        //btnBack
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // create TabBar
        TabLayout tabLayout = findViewById(R.id.tabBar);
        MyViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setEnableSwipe(false);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager(), this);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabViewDrug(i));
        }

        int VEGETAL = getIntent().getIntExtra("vegetal", 0);

        if (VEGETAL == 1) {
            btnBack.setBackground(getResources().getDrawable(R.drawable.background_focus_vegetal));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.greenVegetal));
            RelativeLayout rel = findViewById(R.id.header);
            rel.setBackgroundColor(getResources().getColor(R.color.greenVegetal));
            imgOpenNvDraw.setBackground(getResources().getDrawable(R.drawable.background_focus_vegetal));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.greenVegetalStatus));
            }
        }

        //set TextTitle
        FontTextViewBold nameDrug = findViewById(R.id.name_drug);
        FontTextView persianName = findViewById(R.id.persian_name);
        final int ID = getIntent().getIntExtra("id", 0);
        DbHelper dbHelper = new DbHelper(this);
        final Drug drug = dbHelper.getDrug(ID);
        nameDrug.setText(drug.getName());
        persianName.setText(drug.getFaName());

    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager());

        adapter.addFragment(new FragmentViewDrug(), "توضیحات دارو");
        adapter.addFragment(new FragmentInterferenceDrug(), "تداخل با دارو");
        viewPager.setAdapter(adapter);
    }

    public void openNv(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.item1:
                Intent goToListDrugInteractions = new Intent(this, ActivityListDrugInterference.class);
                startActivity(goToListDrugInteractions);
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
                Intent intentHome = new Intent(this, ActivityHome.class);
                startActivity(intentHome);
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
}
