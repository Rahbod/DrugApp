package com.example.behnam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.example.behnam.app.ActivitySplashScreen;
import com.example.behnam.app.PaymentActivity;
import com.example.behnam.app.R;
import com.example.behnam.app.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySelectVersion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_version);

        // TrialVersion
        Button btnTrialVersion = findViewById(R.id.btnTrialVersion);
        btnTrialVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySelectVersion.this, ActivitySplashScreen.class);
                startActivity(intent);
            }
        });

        // FullVersion
        Button btnFullVersion = findViewById(R.id.btnFullVersion);
        btnFullVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                JSONObject params = new JSONObject();
                try {
                    params.put("id", idNumber);
                    AppController.getInstance(ActivitySelectVersion.this).sendRequest("android/api/activate", params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String url = response.getString("url");
                                int id = response.getInt("id");
                                Intent intent = new Intent(ActivitySelectVersion.this, PaymentActivity.class);
                                intent.putExtra("url", url);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // LicenseVersion
        Button btnLicenseVersion = findViewById(R.id.btnLicenseVersion);
        btnLicenseVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySelectVersion.this, ActivityGetLicense.class);
                startActivity(intent);
            }
        });
    }
}
