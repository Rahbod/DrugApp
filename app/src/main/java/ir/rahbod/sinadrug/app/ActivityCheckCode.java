package ir.rahbod.sinadrug.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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
import ir.rahbod.sinadrug.ActivitySelectVersion;
import ir.rahbod.sinadrug.app.controller.AppController;
import ir.rahbod.sinadrug.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import ir.rahbod.sinadrug.ActivitySelectVersion;
import ir.rahbod.sinadrug.app.controller.AppController;
import ir.rahbod.sinadrug.app.helper.SessionManager;

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
                    if (isConnected()) {
                        btnSave.setEnabled(false);
                        btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue_disable));
                        btnSave.setTextColor(getResources().getColor(R.color.Gray2));
                        btnSave.setText("در حال ارسال اطلاعات...");
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
//                                                Intent intent = new Intent(ActivityCheckCode.this, ActivitySplashScreen.class);
//                                                intent.putExtra("action", "sampleDownload");
                                                Intent intent = new Intent(ActivityCheckCode.this, ActivitySelectVersion.class);
                                                startActivity(intent);
                                                btnSave.setTextColor(getResources().getColor(R.color.white));
                                                btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue));
                                                btnSave.setEnabled(true);
                                                btnSave.setText("ثبت");
                                            }
                                        } else {
                                            Toast.makeText(ActivityCheckCode.this, "کد وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
                                            btnSave.setEnabled(true);
                                            btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue));
                                            btnSave.setTextColor(getResources().getColor(R.color.white));
                                            btnSave.setText("ثبت");
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
                        Toast.makeText(ActivityCheckCode.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(ActivityCheckCode.this, "کد وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
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