package ir.rahbod.sinadrug.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import ir.rahbod.sinadrug.app.dialog.SummaryDialog;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.app.helper.SessionManager;
import ir.rahbod.sinadrug.app.map.MapActivity;

public class ActivityNotifications extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Set user name and mobile
        NavigationView navigationView = findViewById(R.id.notificationNavView);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.txtNameNav);
        navUserName.setText(SessionManager.getExtrasPref(this).getString("name"));
        TextView navUserMobile = headerView.findViewById(R.id.txtMobileNav);
        navUserMobile.setText(SessionManager.getExtrasPref(this).getString("mobile"));

        DbHelper dbHelper = new DbHelper(this);
        int notificationCount = dbHelper.getCountNotification();
        LinearLayout linCountNotification = findViewById(R.id.linCountNotification);
        TextView txtCountNotification = findViewById(R.id.txtCountNotification);
        if (notificationCount > 0) {
            linCountNotification.setVisibility(View.VISIBLE);
            txtCountNotification.setText(notificationCount + "");
        }else
            linCountNotification.setVisibility(View.GONE);

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

        WebView webView = findViewById(R.id.webView);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return true;
            }

        });

        String html = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /><link rel=\"stylesheet\" type=\"text/css\" href=\"fontiran.css\" /><div class=\"container\">"+  getIntent().getStringExtra("text") +"</div>";

        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
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