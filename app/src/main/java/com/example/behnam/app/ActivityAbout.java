package com.example.behnam.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ActivityAbout extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
//        setContentView(R.layout.navigation_view);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        drawerLayout = findViewById(R.id.DrawerLayout);
//        ImageView imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
//        imgOpenNvDraw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        drawerLayout.openDrawer(Gravity.RIGHT);
//                    }
//                }, 100);
//            }
//        });
    }

//    public void openNv(View view) {
//        switch (findViewById(view.getId()).getId()) {
//            case R.id.item1:
//                Intent goToListDrugInteractions = new Intent(this, ActivityListDrugInterference.class);
//                startActivity(goToListDrugInteractions);
//                closeNv();
//                break;
//            case R.id.item2:
//                startActivity(new Intent(this, MapActivity.class));
//                closeNv();
//                break;
//            case R.id.item3:
//                Intent goToReminder = new Intent(this, ActivityReminderList.class);
//                startActivity(goToReminder);
//                closeNv();
//                break;
//            case R.id.item4:
//                Intent goToFavorite = new Intent(this, ActivityFavorite.class);
//                startActivity(goToFavorite);
//                closeNv();
//                break;
//            case R.id.item5:
//                shareApplication();
//                closeNv();
//                break;
//            case R.id.item6:
//                Intent goToErrorReport = new Intent(this, ActivityErrorReport.class);
//                startActivity(goToErrorReport);
//                closeNv();
//                break;
//            case R.id.item7:
//                closeNv();
//                break;
//            case R.id.item8:
//                Intent intentVegetalDrug = new Intent(this, ActivityDrug.class);
//                startActivity(intentVegetalDrug);
//                closeNv();
//                break;
//            case R.id.item9:
//                Intent intentDrug = new Intent(this, ActivityHome.class);
//                startActivity(intentDrug);
//                closeNv();
//                break;
//            case R.id.item10:
//                Intent intentSearch = new Intent(this, ActivityDrug.class);
//                intentSearch.putExtra("search", "search");
//                startActivity(intentSearch);
//                closeNv();
//                break;
//            case R.id.item11:
//                Intent intentPharma = new Intent(this, ActivityCategories.class);
//                intentPharma.putExtra("type", 1);
//                startActivity(intentPharma);
//                closeNv();
//                break;
//            case R.id.item12:
//                Intent intentHealing = new Intent(this, ActivityCategories.class);
//                intentHealing.putExtra("type", 0);
//                startActivity(intentHealing);
//                closeNv();
//                break;
//        }
//    }

//    private void closeNv() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                drawerLayout.closeDrawer(Gravity.RIGHT);
//            }
//        }, 250);
//    }

//    private void shareApplication() {
//        ApplicationInfo app = getApplicationContext().getApplicationInfo();
//        String filePath = app.sourceDir;
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//
//        intent.setType("*/*");
//
//        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
//        startActivity(Intent.createChooser(intent, "Share app via"));
//    }
}
