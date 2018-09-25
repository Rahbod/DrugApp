package ir.rahbod.sinadrug;

import android.annotation.SuppressLint;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import ir.rahbod.sinadrug.app.ActivitySplashScreen;
import ir.rahbod.sinadrug.app.PaymentActivity;
import ir.rahbod.sinadrug.app.R;
import ir.rahbod.sinadrug.app.controller.AppController;
import ir.rahbod.sinadrug.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySelectVersion extends AppCompatActivity {

    public static Activity activitySelectVersion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_version);

        activitySelectVersion = this;

        // TrialVersion
//        LinearLayout btnTrialVersion = findViewById(R.id.btnTrialVersion);
//        if (SessionManager.getExtrasPref(this).getBoolean("selectedVersion"))
//            btnTrialVersion.setVisibility(View.GONE);
//        btnTrialVersion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivitySelectVersion.this, ActivitySplashScreen.class);
//                intent.putExtra("action", "sampleDownload");
//                startActivity(intent);
//            }
//        });

        // FullVersion
        LinearLayout btnFullVersion = findViewById(R.id.btnFullVersion);
        btnFullVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
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
                                    intent.putExtra("action", "fullDownload");
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
                    Toast.makeText(ActivitySelectVersion.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
            }
        });

        // LicenseVersion
        LinearLayout btnLicenseVersion = findViewById(R.id.btnLicenseVersion);
        btnLicenseVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySelectVersion.this, ActivityGetLicense.class);
                startActivity(intent);
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
