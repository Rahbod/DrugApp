package com.example.behnam.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.behnam.ActivitySelectVersion;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCheckCode extends AppCompatActivity {

    public static Activity activityCheckCode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);

        activityCheckCode = this;

        @SuppressLint("HardwareIds") final String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final EditText etCode = findViewById(R.id.code);
        final Button btnSave = findViewById(R.id.save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCode.getText().toString().length() == 4) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("id", idNumber);
                        object.put("code", etCode.getText().toString());
                        JSONObject params = new JSONObject();
                        params.put("User", object);
                        AppController.getInstance(ActivityCheckCode.this).sendRequest("android/api/checkCode", params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("status")) {
                                        if (SessionManager.getExtrasPref(ActivityCheckCode.this).getInt("activated") == 1) {
                                            Intent intent = new Intent(ActivityCheckCode.this, ActivitySplashScreen.class);
                                            intent.putExtra("action", "fullDownload");
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(ActivityCheckCode.this, ActivitySelectVersion.class);
                                            startActivity(intent);
                                        }
                                    } else
                                        Toast.makeText(ActivityCheckCode.this, "کد وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else
                    Toast.makeText(ActivityCheckCode.this, "کد وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
            }
        });
    }
}
