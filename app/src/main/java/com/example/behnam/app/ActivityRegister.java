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
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityRegister extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);

        final EditText etNumberMobile = findViewById(R.id.numberMobile);
        Button btnSave = findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                if (etNumberMobile.getText().toString().isEmpty())
                    Toast.makeText(ActivityRegister.this, "لطفا شماره تلفن خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (!checkMobileNumber(etNumberMobile.getText().toString()))
                    Toast.makeText(ActivityRegister.this, "شماره تلفن وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
                else {
                    try {
                        JSONObject user = new JSONObject();
                        user.put("mobile", etNumberMobile.getText().toString());
                        user.put("id", idNumber);
                        JSONObject params = new JSONObject();
                        params.put("User", user);
                        AppController.getInstance().sendRequest("android/api/setup", params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("status")) {
                                        String key = response.getString("key");
                                        String iv = response.getString("iv");
                                        String id = response.getString("id");
                                        int activated = response.getInt("activated");
                                        SessionManager.getExtrasPref(ActivityRegister.this).putExtra("key", key);
                                        SessionManager.getExtrasPref(ActivityRegister.this).putExtra("iv", iv);
                                        SessionManager.getExtrasPref(ActivityRegister.this).putExtra("activated", activated);
                                        Intent intent = new Intent(ActivityRegister.this, ActivitySplashScreen.class);
                                        intent.putExtra("userId", id);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean checkMobileNumber(String number) {
        if (number.matches("^[0][9][0-9]{9}$"))
            return true;
        else
            return false;
    }
}
