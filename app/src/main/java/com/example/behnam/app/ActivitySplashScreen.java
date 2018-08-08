package com.example.behnam.app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.behnam.app.controller.AppController;

import at.grabner.circleprogress.CircleProgressView;

public class ActivitySplashScreen extends AppCompatActivity {

    private LottieAnimationView animSplashScreen;
    private TextView txtNameLogo;
    private CircleProgressView progressBarSplash;
    private ConnectivityManager connectivityManager;
    public static Activity activitySplashScreen = null;
    public int MY_PERMISSIONS_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        activitySplashScreen = this;

        //Lottie anim
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animSplashScreen = findViewById(R.id.animSplashScreen);
                animSplashScreen.playAnimation();
            }
        }, 300);

        //        anim name
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtNameLogo = findViewById(R.id.txtNameSplash);
                txtNameLogo.setVisibility(View.VISIBLE);
                Animation fadeText = AnimationUtils.loadAnimation(ActivitySplashScreen.this, R.anim.fade_anim);
                fadeText.setStartOffset(200);
                fadeText.setDuration(700);
                txtNameLogo.setAnimation(fadeText);
            }
        }, 1050);

//        anim progressBar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarSplash = findViewById(R.id.progressBarSplash);
                progressBarSplash.setVisibility(View.VISIBLE);
                Animation fadeProgress = AnimationUtils.loadAnimation(ActivitySplashScreen.this, R.anim.fade_anim);
                fadeProgress.setStartOffset(200);
                fadeProgress.setDuration(700);
                progressBarSplash.setAnimation(fadeProgress);
                progressBarSplash.spin();
            }
        }, 1750);

        //Intent to Home activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View viewDialogMassage = LayoutInflater.from(ActivitySplashScreen.this).inflate(R.layout.massage_dialog, null);
                final Dialog dialog = new Dialog(ActivitySplashScreen.this);
                final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
                    dialog.setContentView(viewDialogMassage);
                    LinearLayout linMassageDialog = viewDialogMassage.findViewById(R.id.linDialogMassage);
                    linMassageDialog.setVisibility(View.VISIBLE);
                    TextView txt = viewDialogMassage.findViewById(R.id.txt);
                    txt.setText(R.string.enable_wifi);
                    Button btnOk = viewDialogMassage.findViewById(R.id.btnOk);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (wifiManager != null)
                                wifiManager.setWifiEnabled(true);
                        }
                    });
                    Button btnCancel = viewDialogMassage.findViewById(R.id.btnCancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            ActivitySplashScreen.activitySplashScreen.finish();
                        }
                    });
                    dialog.show();
                } else {
                    if (ActivityCompat.checkSelfPermission(ActivitySplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //download database
                        AppController.getInstance().getSQLiteDb(ActivitySplashScreen.this);
                    } else ActivityCompat.requestPermissions(ActivitySplashScreen.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }


            }
        }, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            AppController.getInstance().getSQLiteDb(ActivitySplashScreen.this);
        } else {
            finish();
        }
    }
}