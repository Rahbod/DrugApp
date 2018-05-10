package com.example.behnam.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.behnam.app.helper.Components;

import at.grabner.circleprogress.CircleProgressView;

public class ActivitySplashScreen extends AppCompatActivity {

    private LottieAnimationView animSplashScreen;
    private TextView txtNameLogo;
    private CircleProgressView progressBarSplash;

    public static Activity activitySplashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        activitySplashScreen = this;

        //        Lottie anim
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
                //download database
                //Components.downloadData(ActivitySplashScreen.this, "first");
                startActivity(new Intent(ActivitySplashScreen.this,ActivityHome.class));

            }
        }, 3000);
    }
}
