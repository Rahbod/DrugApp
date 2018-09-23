package com.example.behnam.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.helper.SessionManager;
import com.example.behnam.app.map.MapActivity;

import java.io.File;

public class ActivityAbout extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txt = findViewById(R.id.txt);
        // Set user name and mobile
        NavigationView navigationView = findViewById(R.id.aboutNavView);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.txtNameNav);
        navUserName.setText(SessionManager.getExtrasPref(this).getString("name"));
        TextView navUserMobile = headerView.findViewById(R.id.txtMobileNav);
        navUserMobile.setText(SessionManager.getExtrasPref(this).getString("mobile"));

        if (getIntent().getStringExtra("type").equals("rules")) {
            TextView txtTitle = findViewById(R.id.txtTitle);

            txtTitle.setText("قوانین و مقررات");
            String text = "قوانین و مقررات استفاده از اپلیکیشن سینادارو" + "\n" + "کلیه حقوق مادی و معنوی اپلیکیشن سینادارو، به انتشارات سیناطب و رودگون اختصاص داشته و تمامی محتویات آن" +
                    " شامل آیکون ها، نشان تجاری داخل اپلیکیشن طبق قانون مالکیت مادی و معنوی، هرگونه استفاده از نام، متون، " +
                    "مطالب، مستندات نرم افزار بدون اجازه کتبی از پدیدآورندگان مطابق با قوانین جرایم نرم افزاری و رایانه ای، ممنوع و " +
                    "غیرمجاز تلقی میگردد و قابل پیگرد قانونی است.\n" +
                    "اطلاعات دارویی ارائه شده در این نرم افزار صرفاً جهت اطلاع رسانی به کاربر است و برای هرگونه تجویز دارو به " +
                    "بیمار لازم است تا این کار توسط پزشک معالج بر مبنای معاینه دقیق و بررسی های بالینی و پاراکلینیکی صورت گیرد و " +
                    "این اطلاعات نمی تواند بعنوان مرجعی برای تجویز و یا مصرف خودسرانه هیچ دارویی محسوب گردد. \nبدینوسیله " +
                    "تأکید میشود تمامی مسئولیت استفاده از محتوای اپلیکیشن و مصرف خودسرانه داروها به طور کلی به عهده کاربر " +
                    "بوده و پدیدآورندگان این اپلیکیشن هیچ مسئولیتی در این زمینه نخواهند داشت.\n";
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 36, 44, 0);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 77, 85, 0);
            txt.setText(spannableString);
        }else{
            String text = "1-اپلیکیشن سینادارو، تنها برای استفاده کاربرانی که در رشته های داروسازی، پزشکی یا زیرگروه های پزشکی مشغول به تحصیل می باشد تهیه و به بازار ارائه شده است و برای استفاده سایر رشته ها از این نرم افزار توصیه نمی شود. هرگونه کپی برداری و سعی در هک کردن داده های نرم افزار طبق قوانین نرم افزاری حاکم بر جمهوری اسلامی ایران غیرمجاز و ممنوع تلقی شده و علاوه بر پیگرد قانونی، شرایط پشتیبانی و خدمات پس از فروش نرم افزار را لغو خواهد نمود.";
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 12, 20, 0);
            txt.setText(spannableString);
        }

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                if (getIntent().getStringExtra("type").equals("rules")) {
                    Intent intentSearch = new Intent(this, ActivityAbout.class);
                    intentSearch.putExtra("type", "about");
                    startActivity(intentSearch);
                    closeNv();
                } else {
                    closeNv();
                }
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
                if (getIntent().getStringExtra("type").equals("rules")) {
                    closeNv();
                } else {
                    Intent intentSearch = new Intent(this, ActivityAbout.class);
                    intentSearch.putExtra("type", "rules");
                    startActivity(intentSearch);
                    closeNv();
                }
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
        else
            super.onBackPressed();
    }
}
