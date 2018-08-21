package com.example.behnam.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.behnam.app.adapter.AdapterDropList;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.database.DropList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityActivation extends AppCompatActivity {
    private TextView txtPresenter, txtCrossSection, txtDepartment;
    private List<DropList> presenterList, crossSectionList, departmentList;
    private List<Integer> crossSectionID, presenterID, departmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        crossSectionID = new ArrayList<>();
        presenterID = new ArrayList<>();
        departmentID = new ArrayList<>();

        txtCrossSection = findViewById(R.id.crossSection);
        txtCrossSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCrossSection(txtCrossSection);
            }
        });

        txtPresenter = findViewById(R.id.presenter);
        txtPresenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getPresenter(txtPresenter);
            }
        });

        txtDepartment = findViewById(R.id.department);
        txtDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getDepartment(txtDepartment);
            }
        });

        Button activ = findViewById(R.id.activ);
        activ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = findViewById(R.id.name);
                EditText etStudy = findViewById(R.id.study);
                EditText etEmail = findViewById(R.id.email);
                if (etName.getText().toString().isEmpty())
                    Toast.makeText(ActivityActivation.this, "لطفا نام خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (etStudy.getText().toString().isEmpty())
                    Toast.makeText(ActivityActivation.this, "لطفا رشته تحصیلی خود را وارد کنید", Toast.LENGTH_SHORT).show();
                else if (crossSectionID.isEmpty())
                    Toast.makeText(ActivityActivation.this, "لطفا مقطع تحصیلی خود را انتخاب کنید", Toast.LENGTH_SHORT).show();
                else if (departmentID.isEmpty())
                    Toast.makeText(ActivityActivation.this, "لطفا گروه تحصیلی خود را وارد کنید", Toast.LENGTH_SHORT).show();
                else {
                    @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    JSONObject user = new JSONObject();
                    JSONObject params = new JSONObject();
                    try {
                        user.put("name", etName.getText().toString());
                        user.put("department", departmentID.get(0));
                        user.put("grade", crossSectionID.get(0));
                        user.put("field", etStudy.getText().toString());
                        if (!etEmail.getText().toString().isEmpty())
                            user.put("email", etEmail.getText().toString());
                        if (presenterID.size() > 0)
                            user.put("marketer_id", presenterID.get(0));
                        params.put("id", idNumber);
                        params.put("User", user);
                        Log.e("qqqq", "params: " + params );
                        AppController.getInstance(ActivityActivation.this).sendRequest("android/api/activate", params, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getBoolean("status")){
                                        String url = response.getString("url");
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(url));
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
        final Dialog dialog = new Dialog(ActivityActivation.this);
        View view = LayoutInflater.from(ActivityActivation.this).inflate(R.layout.summary_dialog, null);
        dialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recSummaryDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityActivation.this));
        AdapterDropList adapter = new AdapterDropList(ActivityActivation.this, departmentList, dialog, txtDepartment, departmentID);
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

    private void getPresenter(final TextView txtPresenter) {
        AppController.getInstance(ActivityActivation.this).sendRequest("android/api/getMarketers", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        presenterList = new ArrayList<>();
                        JSONObject object;
                        JSONArray marketers = response.getJSONArray("marketers");
                        for (int i = 0; i < marketers.length(); i++) {
                            DropList presenter = new DropList();
                            object = marketers.getJSONObject(i);
                            presenter.setName(object.getString("name"));
                            presenter.setId(object.getInt("id"));
                            ActivityActivation.this.presenterList.add(presenter);
                        }
                        final Dialog dialog = new Dialog(ActivityActivation.this);
                        View view = LayoutInflater.from(ActivityActivation.this).inflate(R.layout.summary_dialog, null);
                        dialog.setContentView(view);
                        RecyclerView recyclerView = view.findViewById(R.id.recSummaryDialog);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityActivation.this));
                        AdapterDropList adapter = new AdapterDropList(ActivityActivation.this, presenterList, dialog, txtPresenter, presenterID);
                        recyclerView.setAdapter(adapter);
                        TextView txtTitle = view.findViewById(R.id.txtTitle);
                        txtTitle.setText("لیست معرف ها");
                        ImageView imgClose = view.findViewById(R.id.imgCloseDialog);
                        imgClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCrossSection(TextView txtName) {
        crossSectionList = new ArrayList<>();
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
                crossSectionList.add(drop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(ActivityActivation.this);
        View view = LayoutInflater.from(ActivityActivation.this).inflate(R.layout.summary_dialog, null);
        dialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recSummaryDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityActivation.this));
        AdapterDropList adapter = new AdapterDropList(ActivityActivation.this, crossSectionList, dialog, txtName, crossSectionID);
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