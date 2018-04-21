package com.example.behnam.app.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Response;
import com.example.behnam.app.R;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.CategoryDrug;
import com.example.behnam.app.database.Drug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Components extends AppController {
    public void getDrugs(final Context context) {
        ConnectivityManager connectivityManager;
        final DbHelper dbHelper = new DbHelper(context);
        final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.enable_wifi).setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (wifiManager != null)
                                wifiManager.setWifiEnabled(true);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        } else if (SessionManager.getExtrasPref(context).getBoolean("primitiveRecordsExists")) {
            AppController.getInstance().sendRequest("android/api/list", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("status")) {
                            JSONArray jsonArray = response.getJSONArray("drugs");
                            JSONObject object;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                object = jsonArray.getJSONObject(i);
                                dbHelper.addDrug(new Drug(object.getInt("id"), object.getString("name"), object.getString("brand"), object.getString("pregnancy"), object.getString("lactation"), object.getString("kids"), object.getString("seniors"), object.getString("how_to_use"), object.getString("product"), object.getString("pharmacodynamic"), object.getString("usage"), object.getString("prohibition"), object.getString("caution"), object.getString("dose_adjustment"), object.getString("complication"), object.getString("interference"), object.getString("effect_on_test"), object.getString("overdose"), object.getString("description"), object.getString("relation_with_food"), object.getInt("status"), object.getString("last_modified")));

                            }

                            SessionManager.getExtrasPref(context).putExtra("primitiveRecordsExists", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            AppController.getInstance().sendRequest("android/api/category", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("status")) {
                            Log.e("category=", response.toString());
                            JSONArray jsonArray = response.getJSONArray("categories");
                            JSONObject object;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                object = jsonArray.getJSONObject(i);
                                dbHelper.addCategory(new Category(object.getString("name"), object.getInt("type")));
                            }
                            SessionManager.getExtrasPref(context).putExtra("primitiveRecordsExists", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            AppController.getInstance().sendRequest("android/api/categoryDrug", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("status")) {
                            Log.e("categoryDrug=", response.toString());
                            JSONArray jsonArray = response.getJSONArray("list");
                            JSONObject object;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                object = jsonArray.getJSONObject(i);
                                dbHelper.addCategoryDrug(new CategoryDrug(object.getInt("drug_id"), object.getInt("category_id"), object.getInt("type")));
                            }
                            SessionManager.getExtrasPref(context).putExtra("primitiveRecordsExists", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
