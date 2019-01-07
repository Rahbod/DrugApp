package ir.rahbod.sinadrug.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerSection;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import ir.rahbod.sinadrug.app.adapter.AdapterVegetalDrug;
import ir.rahbod.sinadrug.app.database.Index;
import ir.rahbod.sinadrug.app.fastscroll.AlphabetItem;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.app.map.MapActivity;

public class ActivityVegetalDrug extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView imgOpenNvDraw;
    private TextView text;
    private IndexFastScrollRecyclerView recyclerView;
    private AdapterVegetalDrug adapter;
    private DbHelper dbHelper;
    private List<Index> list;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetal_drug);
        bind();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterVegetalDrug(list, this);
        recyclerView.setAdapter(adapter);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final ImageView searchIcon = findViewById(R.id.searchHome);
        final ImageView closeIcon = findViewById(R.id.closeIcon);
        searchIcon.setVisibility(View.VISIBLE);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strText = text.getText().toString().trim();
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
                filter(text.getText().toString().trim());
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

    private void bind() {
        drawerLayout = findViewById(R.id.DrawerLayout);
        imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
        text = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.fast_scroller_recycler);
        dbHelper = new DbHelper(this);
        list = dbHelper.getVegetalDrug();
        btnBack = findViewById(R.id.btnBack);
        //        sort item
        Collections.sort(list, new Comparator<Index>() {
            @Override
            public int compare(Index o1, Index o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        //Alphabet fast scroller data
        List<AlphabetItem> mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Index name = list.get(i);
            if (name == null || name.getName().isEmpty())
                continue;
            String word = name.getName().substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
        //change color statusBar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.greenVegetalStatus));
        }
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            super.onBackPressed();
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
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.rahbod.ir/PharmaSina.apk");
        }

        startActivity(Intent.createChooser(intent, "Share app via"));
    }
}
