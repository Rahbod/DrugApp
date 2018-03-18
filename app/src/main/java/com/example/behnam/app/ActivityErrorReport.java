package com.example.behnam.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;

import com.example.behnam.app.helper.DbHelper;

import static com.example.behnam.app.ActivityReminderStep2.isAppAvailable;

public class ActivityErrorReport extends AppCompatActivity {
    DbHelper dbHelper ;
    String appName = "org.telegram.messenger";
    Button telegramBtn, favoriteBtn ;
    ImageView btnBack;

    private ImageView btnBack;
    private Button btnSave;
    private EditText etReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_report);

        telegramBtn= findViewById(R.id.telegram);
        favoriteBtn= findViewById(R.id.favorite);
        etReport = findViewById(R.id.etReport);

        btnBack = findViewById(R.id.btnBack);
        dbHelper = new DbHelper(getApplicationContext());

        telegramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isAppInstalled = isAppAvailable(ActivityErrorReport.this, appName);
                if (isAppInstalled)
                {
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    myIntent.setPackage(appName);
                    myIntent.putExtra(Intent.EXTRA_TEXT, "متن تلگرام");//
                    startActivity(Intent.createChooser(myIntent, "Share with"));
                }
                else
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=org.telegram.messenger"));
                    startActivity(i);
                }
            }
        });
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dbHelper.updateDrug();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave =findViewById(R.id.btnSave);
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (etReport.getText().toString().isEmpty()){
////                    Toast.makeText(ActivityErrorReport.this, "متن نمیتواند خالی باشد", Toast.LENGTH_LONG).show();
////                    etReport.setBackgroundResource(R.drawable.shape_background_error);
////                    etReport.addTextChangedListener(new TextWatcher() {
////                        @Override
////                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////
////                        }
////
////                        @Override
////                        public void onTextChanged(CharSequence s, int start, int before, int count) {
////
////                        }
////
////                        @Override
////                        public void afterTextChanged(Editable s) {
////                            etReport.setBackgroundResource(R.drawable.edit_text_border);
////                        }
////                    });
////                }
////                else {
//                    JSONObject params = new JSONObject();
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("subject", "0");
//                        jsonObject.put("text", etReport.getText().toString());
//                        jsonObject.put("drug_id", 0);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        params.put("Report", jsonObject);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    AppController.getInstance().sendRequest("android/api/report", params, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                Log.e("response", response.toString());
//                                if (response.getBoolean("status")){
//                                    String massage = response.getString("message");
//                                    Toast.makeText(ActivityErrorReport.this,  massage, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
////                }
//            }
//        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etReport.getText().toString();
                JSONObject params;
                if (etReport.getText().toString().isEmpty()){
                    Toast.makeText(ActivityErrorReport.this, "متن نمیتواند خالی باشد", Toast.LENGTH_LONG).show();
                }
                else {
                    params = new JSONObject();
                    JSONObject object = new JSONObject();
                    try {
                        object.put("subject", "0");
                        object.put("text", str);
                        object.put("drug_id", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        params.put("Report", object);
                        Log.e("asd", params.toString());
                        AppController.getInstance().sendRequest("android/api/report?cache="+System.currentTimeMillis(), params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("res", String.valueOf(response));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
