package com.example.behnam;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.behnam.app.ActivityIndex;
import com.example.behnam.app.ActivitySplashScreen;
import com.example.behnam.app.R;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActivityGetLicense extends AppCompatActivity {
    private String idNumber;
    private EditText etCodeLicense;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_license);

        etCodeLicense = findViewById(R.id.etCodeLicense);
        idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Button btnSave = findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject params = new JSONObject();
                try {
                    params.put("id", idNumber);
                    params.put("code", etCodeLicense.getText().toString());
                    AppController.getInstance(ActivityGetLicense.this).sendRequest("android/api/checkLicense", params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")){
                                    // activeAccount
                                    SessionManager.getExtrasPref(ActivityGetLicense.this).putExtra("activated", 1);

                                    //download Data
                                    Intent intent = new Intent(ActivityGetLicense.this, ActivitySplashScreen.class);
                                    intent.putExtra("action", "fullDownload");
                                    startActivity(intent);
                                }
                                else
                                    showMassage(response);
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

    }

    private void showMassage(JSONObject response) {
        try {
            String massage = response.getString("message");
            TextView txtMassage = findViewById(R.id.txtMessage);
            txtMassage.setVisibility(View.VISIBLE);
            txtMassage.setText(massage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}