package com.rahbod.pharmasina;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.rahbod.pharmasina.app.ActivityIndex;
import com.rahbod.pharmasina.app.PaymentActivity;
import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.controller.AppController;
import com.rahbod.pharmasina.app.helper.SessionManager;
import com.rahbod.pharmasina.fonts.ButtonFont;
import com.rahbod.pharmasina.fonts.FontTextView;

public class ActivityTrialMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_message);

        checkActivated();

        FontTextView trialMessageText = findViewById(R.id.trialMessageText);
        if (getIntent().getBooleanExtra("fromIndex", false))
            trialMessageText.setVisibility(View.GONE);
        else
            trialMessageText.setVisibility(View.VISIBLE);

        ButtonFont btnPayment = findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    String imei = "";
                    if (ActivityCompat.checkSelfPermission(ActivityTrialMessage.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(ActivityTrialMessage.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
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

                    JSONObject params = new JSONObject();
                    try {
                        params.put("id", idNumber);
                        params.put("imei", imei);
                        AppController.getInstance(ActivityTrialMessage.this).sendRequest("android/api/activate", params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String url = response.getString("url");
                                    int id = response.getInt("id");
                                    Intent intent = new Intent(ActivityTrialMessage.this, PaymentActivity.class);
                                    intent.putExtra("url", url);
                                    intent.putExtra("id", id);
                                    intent.putExtra("action", "download");
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(ActivityTrialMessage.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public void checkActivated() {
        JSONObject params = new JSONObject();
        String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert telephonyManager != null;
            imei = telephonyManager.getImei();
        } else {
            assert telephonyManager != null;
            imei = telephonyManager.getDeviceId();
        }
        try {
            params.put("id", idNumber);
            params.put("imei", imei);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isConnected())
            AppController.getInstance().sendRequest("android/api/checkActivated", params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        SessionManager.getExtrasPref(ActivityTrialMessage.this).putExtra("activated", response.getInt("activated"));
                        if (response.getInt("activated") == 1){
                            Intent intent = new Intent(ActivityTrialMessage.this, ActivityIndex.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
