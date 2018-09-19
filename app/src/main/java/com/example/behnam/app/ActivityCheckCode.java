package com.example.behnam.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.behnam.ActivitySelectVersion;
import com.example.behnam.app.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCheckCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);
        final String userId = getIntent().getStringExtra("userId");
        @SuppressLint("HardwareIds") final String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final EditText etCode = findViewById(R.id.code);
        Button btnSave = findViewById(R.id.save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    Intent intent = new Intent(ActivityCheckCode.this, ActivitySelectVersion.class);
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                }else
                                    Toast.makeText(ActivityCheckCode.this, "کد وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
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
}
