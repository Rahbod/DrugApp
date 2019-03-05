package com.rahbod.pharmasina.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rahbod.pharmasina.app.adapter.AdapterCategories;
import com.rahbod.pharmasina.app.database.Category;
import com.rahbod.pharmasina.app.helper.Components;
import com.rahbod.pharmasina.app.helper.DbHelper;
import com.rahbod.pharmasina.app.helper.SessionManager;
import com.rahbod.pharmasina.app.map.MapActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityCategories extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView text;
    private List<Category> list;
    private AdapterCategories adapter;
    private DbHelper dbHelper;
    int type;
    private DrawerLayout drawerLayout;
//    private ImageView btnListen;
//    private SpeechProgressView progress;
//    private ConnectivityManager connectivityManager;
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

        if (SessionManager.getExtrasPref(this).getString("idList") != null) {
            SessionManager.getExtrasPref(this).remove("idList");
        }

        drawerLayout = findViewById(R.id.DrawerLayout);
        ImageView imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
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

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        text = findViewById(R.id.editTextSearch);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        dbHelper = new DbHelper(this);
        list = dbHelper.getCategories(type, "parent_id = 0");
        recyclerView = findViewById(R.id.recCategoryList);
        adapter = new AdapterCategories(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        TextView txtTitle = findViewById(R.id.txtTitle);
        if (type == 0)
            txtTitle.setText("طبقه بندی درمانی");
        else txtTitle.setText("دسته بندی بر اساس بیماری");

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

        //        sort item
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
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
        ArrayList<Category> filterDrug = new ArrayList<>();
        for (Category category : list) {
            if (category.getName().toLowerCase().contains(str.toLowerCase()))
                filterDrug.add(category);

        }
        adapter.filterList(filterDrug);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else if (SessionManager.getExtrasPref(this).getString("idList").isEmpty())
            super.onBackPressed();
        String strList = SessionManager.getExtrasPref(this).getString("idList");
        try {
            JSONArray parentListId = new JSONArray(strList);
            if (parentListId.length() > 0) {
                int parentID = parentListId.getInt(parentListId.length() - 1);
                parentListId = Components.jsonArrayRemove(parentListId, parentListId.length() - 1);
                SessionManager.getExtrasPref(this).putExtra("idList", parentListId.toString());
                list = dbHelper.getCategories(type, "parent_id=" + parentID);
                adapter = new AdapterCategories(this, list);
                recyclerView.setAdapter(adapter);
            } else {
                super.onBackPressed();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // sort list
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public void getDataToList() {
        String strList = SessionManager.getExtrasPref(this).getString("idList");
        try {
            JSONArray jsonArray = new JSONArray(strList);
            int id = jsonArray.getInt(jsonArray.length() - 1);
            list = dbHelper.getCategories(type, "parent_id=" + id);
            jsonArray = Components.jsonArrayRemove(jsonArray, jsonArray.length() - 1);
            SessionManager.getExtrasPref(this).putExtra("idList", jsonArray.toString());
            adapter = new AdapterCategories(this, list);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // sort list
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    @Override
    protected void onResume() {
//        speechInstance = Speech.init(this, getPackageName());
        getDataToList();
        super.onResume();
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
                if (type == 0) {
                    Intent intentPharma = new Intent(this, ActivityCategories.class);
                    intentPharma.putExtra("type", 1);
                    startActivity(intentPharma);
                    closeNv();
                } else drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.item12:
                if (type == 1) {
                    Intent intentHealing = new Intent(this, ActivityCategories.class);
                    intentHealing.putExtra("type", 0);
                    startActivity(intentHealing);
                    closeNv();
                } else drawerLayout.closeDrawer(Gravity.RIGHT);
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
}