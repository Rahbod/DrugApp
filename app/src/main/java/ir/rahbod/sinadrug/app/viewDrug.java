package ir.rahbod.sinadrug.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.rahbod.sinadrug.app.adapter.AdapterTabBar;
import ir.rahbod.sinadrug.app.database.Drug;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.app.helper.SessionManager;
import ir.rahbod.sinadrug.app.map.MapActivity;
import ir.rahbod.sinadrug.fonts.FontTextView;
import ir.rahbod.sinadrug.fonts.FontTextViewBold;
import ir.rahbod.sinadrug.fragment.FragmentInterferenceDrug;
import ir.rahbod.sinadrug.fragment.FragmentViewDrug;
import ir.rahbod.sinadrug.fragment.FragmentViewDrug2;

import java.io.File;

public class viewDrug extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drug);

        // Set user name and mobile
        NavigationView navigationView = findViewById(R.id.viewDrugNavView);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.txtNameNav);
        navUserName.setText(SessionManager.getExtrasPref(this).getString("name"));
        TextView navUserMobile = headerView.findViewById(R.id.txtMobileNav);
        navUserMobile.setText(SessionManager.getExtrasPref(this).getString("mobile"));

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

//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        FragmentViewDrug fragment = new FragmentViewDrug();
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit();

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

        int notificationCount = dbHelper.getCountNotification();
        LinearLayout linCountNotification = findViewById(R.id.linCountNotification);
        TextView txtCountNotification = findViewById(R.id.txtCountNotification);
        if (notificationCount > 0) {
            linCountNotification.setVisibility(View.VISIBLE);
            txtCountNotification.setText(notificationCount + "");
        }else
            linCountNotification.setVisibility(View.GONE);

    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager());

//        adapter.addFragment(new FragmentViewDrug(), "توضیحات دارو");
        adapter.addFragment(new FragmentViewDrug2(), "توضیحات دارو");
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
                Intent intentVegetalDrug = new Intent(this, ActivityVegetalDrug.class);
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
            case R.id.item13:
                Intent intent = new Intent(this, ActivityListNotifications.class);
                startActivity(intent);
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
        Intent intent = new Intent(Intent.ACTION_SEND);

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;

            intent.setType("*/*");

            File file = new File(filePath);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.rahbod.ir/PharmaSina.apk");
        }

        startActivity(Intent.createChooser(intent, "Share app via"));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            super.onBackPressed();
    }
}
