package com.rahbod.pharmasina.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rahbod.pharmasina.app.adapter.AdapterDrugByCategory;
import com.rahbod.pharmasina.app.database.Index;
import com.rahbod.pharmasina.app.helper.DbHelper;
import com.rahbod.pharmasina.app.helper.SessionManager;
import com.rahbod.pharmasina.app.map.MapActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityDrug extends AppCompatActivity{

    private TextView text;
    private List<Index> list;
    private AdapterDrugByCategory adapter;
    private DrawerLayout drawerLayout;
//    private SpeechProgressView progress;
//    private ConnectivityManager connectivityManager;
//    private ImageView btnListen;
//    private Speech speechInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        // Set user name and mobile
        NavigationView navigationView = findViewById(R.id.categoryDrugListNavView);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.txtNameNav);
        navUserName.setText(SessionManager.getExtrasPref(this).getString("name"));
        TextView navUserMobile = headerView.findViewById(R.id.txtMobileNav);
        navUserMobile.setText(SessionManager.getExtrasPref(this).getString("mobile"));

        TextView txtTitle = findViewById(R.id.txtTitle);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
        //Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recCategoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DbHelper dbHelper = new DbHelper(this);
        text = findViewById(R.id.editTextSearch);
//        btnListen = findViewById(R.id.imgVoice);

        if (getIntent().getStringExtra("search") != null) {
            txtTitle.setText("جستجو");
            list = dbHelper.getDrugs();
            adapter = new AdapterDrugByCategory(this, list);
            recyclerView.setAdapter(adapter);

            //sort list
            Collections.sort(list, new Comparator<Index>() {
                @Override
                public int compare(Index o1, Index o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else {
            list = dbHelper.getVegetalDrug();
            adapter = new AdapterDrugByCategory(this, list);
            recyclerView.setAdapter(adapter);

            //        sort item
            Collections.sort(list, new Comparator<Index>() {
                @Override
                public int compare(Index o1, Index o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            // Change Color Header
            RelativeLayout rel1 = findViewById(R.id.actionBarFavorite);
            RelativeLayout rel2 = findViewById(R.id.relHome1);
            rel1.setBackgroundColor(getResources().getColor(R.color.greenVegetal));
            rel2.setBackgroundColor(getResources().getColor(R.color.greenVegetal));

            //change color statusBar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.greenVegetalStatus));
            }

            //set Text Title
            txtTitle.setText("داروهای گیاهی");

            // set ColorBackGround btnBack
            btnBack.setBackground(getResources().getDrawable(R.drawable.background_focus_vegetal));
            imgOpenNvDraw.setBackground(getResources().getDrawable(R.drawable.background_focus_vegetal));
        }

        // search
        final ImageView searchIcon = findViewById(R.id.searchIcon);
        final ImageView closeIcon = findViewById(R.id.closeIcon);
        searchIcon.setVisibility(View.VISIBLE);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String strText = text.getText().toString();
                if (strText.matches("")) {
                    closeIcon.setVisibility(View.INVISIBLE);
                    searchIcon.setVisibility(View.VISIBLE);
                } else {
                    searchIcon.setVisibility(View.INVISIBLE);
                    closeIcon.setVisibility(View.VISIBLE);
                    closeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            text.setText("");
                            searchIcon.setVisibility(View.VISIBLE);
                            closeIcon.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                filter(s.toString());

            }
        });

        //NavigationView
        drawerLayout = findViewById(R.id.DrawerLayout);
        imgOpenNvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                Class<? extends View.OnClickListener> view = this.getClass();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }, 100);
            }
        });

        int notificationCount = dbHelper.getCountNotification();
        LinearLayout linCountNotification = findViewById(R.id.linCountNotification);
        TextView txtCountNotification = findViewById(R.id.txtCountNotification);
        if (notificationCount > 0) {
            linCountNotification.setVisibility(View.VISIBLE);
            txtCountNotification.setText(notificationCount + "");
        }else
            linCountNotification.setVisibility(View.GONE);
    }

    private void filter(String str) {
        ArrayList<Index> filterDrug = new ArrayList<>();
        for (Index index : list) {
            if (index.getName().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(index);
            } else if (index.getFa_name().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(index);
            } else if (index.getBrand().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(index);
            }
        }
        adapter.filterList(filterDrug);
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
                if (getIntent().getStringExtra("search") != null) {
                    Intent intentVegetalDrug = new Intent(this, ActivityVegetalDrug.class);
                    startActivity(intentVegetalDrug);
                    closeNv();
                } else drawerLayout.closeDrawer(Gravity.RIGHT);
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
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.pharmasin.ir/PharmaSina.apk");
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