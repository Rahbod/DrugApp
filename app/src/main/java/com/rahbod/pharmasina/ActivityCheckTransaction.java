package com.rahbod.pharmasina;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.rahbod.pharmasina.app.ActivitySplashScreen;
import com.rahbod.pharmasina.app.PaymentActivity;
import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.controller.AppController;
import com.rahbod.pharmasina.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCheckTransaction extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private LinearLayout linText;
    private Button btnBack;
    public static Activity activityCheckTransaction = null;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_transaction);

        linText = findViewById(R.id.layout);

        if (PaymentActivity.activityPayment != null)
            PaymentActivity.activityPayment.finish();

        ImageView imgBack = findViewById(R.id.btnBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBack = findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnDownload = findViewById(R.id.download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // activeAccount
                SessionManager.getExtrasPref(ActivityCheckTransaction.this).putExtra("activated", 1);
                Intent intent = new Intent(ActivityCheckTransaction.this, ActivitySplashScreen.class);
                intent.putExtra("action", getIntent().getStringExtra("action"));
                startActivity(intent);
                finish();
            }
        });

        JSONObject params = new JSONObject();
        try {
            params.put("id", getIntent().getIntExtra("id", 0));
            AppController.getInstance(this).sendRequest("android/api/checkTransaction", params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("status"))
                            SuccessfulPayment(response);
                        else
                            UnsuccessfulPayment(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SuccessfulPayment(JSONObject response) {
        linText.setVisibility(View.VISIBLE);
        Button btnDownload = findViewById(R.id.download);
        btnDownload.setVisibility(View.VISIBLE);
        btnDownload.setBackground(getResources().getDrawable(R.drawable.shape_button_blue));
        btnDownload.setTextColor(getResources().getColor(R.color.white));
        TextView txtMessageOk = findViewById(R.id.txtMessageOk);
        TextView txtAmount = findViewById(R.id.amount);
        TextView txtOrderId = findViewById(R.id.orderId);
        TextView txtCode = findViewById(R.id.code);
        try {
            String massage = response.getString("message");
            int amount = response.getInt("amount");
            int order_id = response.getInt("orderID");
            String code = response.getString("code");

            // setText
            txtMessageOk.setText(massage);
            txtMessageOk.setTextColor(getResources().getColor(R.color.greenVegetal));
            txtAmount.setText(amount + "");
            txtOrderId.setText(order_id + "");
            txtCode.setText(code);

            // activeAccount
            SessionManager.getExtrasPref(this).putExtra("activated", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void UnsuccessfulPayment(JSONObject response) {
        try {
            btnBack.setVisibility(View.VISIBLE);
            TextView txtMassage = findViewById(R.id.txtMessageOk);
            txtMassage.setVisibility(View.VISIBLE);
            String massage = response.getString("message");
            txtMassage.setText(massage);
            txtMassage.setTextColor(getResources().getColor(R.color.red));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}