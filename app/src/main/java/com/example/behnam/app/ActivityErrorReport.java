package com.example.behnam.app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.helper.DbHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityErrorReport extends AppCompatActivity {
    DbHelper dbHelper ;

    private String appName = "org.telegram.messenger";
    private ImageView btnBack;
    private Button btnSave;
    private EditText etReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_report);

        etReport = findViewById(R.id.etReport);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        btnBack = findViewById(R.id.btnBack);
        dbHelper = new DbHelper(getApplicationContext());


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etReport.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityErrorReport.this, "متن نمیتواند خالی باشد", Toast.LENGTH_LONG).show();
                    etReport.setBackgroundResource(R.drawable.shape_background_error);
                    etReport.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            etReport.setBackgroundResource(R.drawable.edit_text_border);
                        }
                    });
                } else {
                    //  hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etReport.getWindowToken(), 0);

                    JSONObject params = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("subject", "0");
                        jsonObject.put("text", etReport.getText().toString());
                        jsonObject.put("drug_id", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        params.put("Report", jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AppController.getInstance().sendRequest("android/api/report?cache=", params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    final String massage = response.getString("message");
                                    Toast.makeText(ActivityErrorReport.this, massage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    etReport.setText("");
                }
            }
        });
    }
}