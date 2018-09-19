package com.example.behnam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.behnam.app.ActivitySplashScreen;
import com.example.behnam.app.R;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCheckTransaction extends AppCompatActivity {


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_transaction);

        Button btnDownload = findViewById(R.id.download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // activeAccount
                SessionManager.getExtrasPref(ActivityCheckTransaction.this).putExtra("activated", 1);
                Intent intent = new Intent(ActivityCheckTransaction.this, ActivitySplashScreen.class);
                startActivity(intent);
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
        ConstraintLayout layout = findViewById(R.id.layout);
        layout.setVisibility(View.VISIBLE);
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
            TextView txtMassage = findViewById(R.id.txtMessage);
            txtMassage.setVisibility(View.VISIBLE);
            String massage = response.getString("message");
            txtMassage.setText(massage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
