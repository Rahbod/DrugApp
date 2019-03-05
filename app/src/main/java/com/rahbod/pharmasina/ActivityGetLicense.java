package com.rahbod.pharmasina;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.rahbod.pharmasina.app.ActivitySplashScreen;
import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.controller.AppController;
import com.rahbod.pharmasina.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityGetLicense extends AppCompatActivity {
    private String idNumber, imei;
    private EditText etCodeLicense;
    public static Activity activityGetLicense = null;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_license);

        activityGetLicense = this;

        etCodeLicense = findViewById(R.id.etCodeLicense);
        idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        imei = "";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert telephonyManager != null;
                imei = telephonyManager.getImei();
            } else {
                assert telephonyManager != null;
                imei = telephonyManager.getDeviceId();
            }
        }

        final Button btnSave = findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    btnSave.setEnabled(false);
                    btnSave.setText("در حال ارسال اطلاعات...");
                    btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue_disable));
                    btnSave.setTextColor(getResources().getColor(R.color.Gray2));
                    JSONObject params = new JSONObject();
                    try {
                        params.put("id", idNumber);
                        params.put("imei", imei);
                        params.put("code", etCodeLicense.getText().toString());
                        AppController.getInstance(ActivityGetLicense.this).sendRequest("android/api/checkLicense", params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("status")) {
                                        // activeAccount
                                        SessionManager.getExtrasPref(ActivityGetLicense.this).putExtra("activated", 1);

                                        //download Data
                                        Intent intent = new Intent(ActivityGetLicense.this, ActivitySplashScreen.class);
                                        intent.putExtra("action", "fullDownload");
                                        startActivity(intent);
                                    } else {
                                        btnSave.setEnabled(true);
                                        btnSave.setText("ثبت");
                                        btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue));
                                        btnSave.setTextColor(getResources().getColor(R.color.white));
                                        String massage = response.getString("message");
                                        Toast.makeText(ActivityGetLicense.this, massage, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                    Toast.makeText(ActivityGetLicense.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
            return false;
        } else
            return true;
    }
}