package com.example.behnam.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.behnam.app.adapter.AdapterDropList;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.database.DropList;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityRegister extends AppCompatActivity {
    private EditText etName, etNumberMobile, etField, etEmail;
    private TextView txtGrade, txtDepartment;
    private List<Integer> gradeID, departmentID;
    private List<DropList> gradeList, departmentList;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);

        etNumberMobile = findViewById(R.id.numberMobile);
        etName = findViewById(R.id.name);
        etField = findViewById(R.id.field);
        etEmail = findViewById(R.id.email);
        txtGrade = findViewById(R.id.grade);
        txtDepartment = findViewById(R.id.department);
        CheckBox checkBox = findViewById(R.id.checkBox);
        btnSave = findViewById(R.id.save);
        gradeID = new ArrayList<>();

        if (checkBox.isChecked())
            btnSave.setEnabled(true);
        else
            btnSave.setEnabled(false);

        txtDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDepartment(txtDepartment);
            }
        });
        txtGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGrade(txtGrade);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                if (etNumberMobile.getText().toString().isEmpty())
                    Toast.makeText(ActivityRegister.this, "لطفا شماره تلفن خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (!checkMobileNumber(etNumberMobile.getText().toString()))
                    Toast.makeText(ActivityRegister.this, "شماره تلفن وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
                else {
                    JSONObject object = new JSONObject();
                    JSONObject params = new JSONObject();
                    try {
                        object.put("mobile", etNumberMobile.getText().toString());
                        object.put("id", idNumber);
                        object.put("name", etName.getText().toString());
                        object.put("field", etField.getText().toString());
                        object.put("grade", gradeID.get(0));
                        object.put("department", departmentID.get(0));
                        if (!etEmail.getText().toString().isEmpty())
                            object.put("email", etEmail.getText().toString());
                        params.put("User", object);
                        AppController.getInstance(ActivityRegister.this).sendRequest("android/api/register", params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("status")){
                                        SessionManager.getExtrasPref(ActivityRegister.this).putExtra("activated", response.getString("activated"));
                                        SessionManager.getExtrasPref(ActivityRegister.this).putExtra("key", response.getString("key"));
                                        SessionManager.getExtrasPref(ActivityRegister.this).putExtra("iv", response.getString("iv"));
                                        SessionManager.getExtrasPref(ActivityRegister.this).putExtra("name", response.getString("name"));
                                        SessionManager.getExtrasPref(ActivityRegister.this).putExtra("mobile", response.getString("mobile"));
                                        Intent intent = new Intent(ActivityRegister.this, ActivityCheckCode.class);
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

    private void getGrade(TextView txtName) {
        gradeList = new ArrayList<>();
        String[] strCrossSection = getResources().getStringArray(R.array.crossSection);
        JSONObject object = createJSONObject(strCrossSection, "txtCrossSection");
        try {
            JSONArray jsonArray = object.getJSONArray("txtCrossSection");
            JSONObject objectList;
            for (int i = 0; i < jsonArray.length(); i++) {
                DropList drop = new DropList();
                objectList = jsonArray.getJSONObject(i);
                drop.setName(objectList.getString("name"));
                drop.setId(objectList.getInt("id"));
                gradeList.add(drop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.summary_dialog, null);
        dialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recSummaryDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterDropList adapter = new AdapterDropList(this, gradeList, dialog, txtName, gradeID);
        recyclerView.setAdapter(adapter);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("مقطع");
        ImageView imgClose = view.findViewById(R.id.imgCloseDialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getDepartment(TextView txtDepartment) {
        departmentList = new ArrayList<>();
        departmentID = new ArrayList<>();
        String[] strCrossSection = getResources().getStringArray(R.array.txtDepartment);
        JSONObject object = createJSONObject(strCrossSection, "txtDepartment");
        try {
            JSONArray jsonArray = object.getJSONArray("txtDepartment");
            JSONObject objectList;
            for (int i = 0; i < jsonArray.length(); i++) {
                DropList drop = new DropList();
                objectList = jsonArray.getJSONObject(i);
                drop.setName(objectList.getString("name"));
                drop.setId(objectList.getInt("id"));
                departmentList.add(drop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.summary_dialog, null);
        dialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recSummaryDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterDropList adapter = new AdapterDropList(this, departmentList, dialog, txtDepartment, departmentID);
        recyclerView.setAdapter(adapter);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("گروه تحصیلی");
        ImageView imgClose = view.findViewById(R.id.imgCloseDialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private JSONObject createJSONObject(String[] strArray, String name) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < strArray.length; i++) {
            try {
                JSONObject object = new JSONObject();
                object.put("name", strArray[i]);
                object.put("id", i + 1);
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            jsonObject.put(name, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}