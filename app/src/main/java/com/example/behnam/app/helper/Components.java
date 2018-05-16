package com.example.behnam.app.helper;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import com.android.volley.Response;
import com.example.behnam.app.ActivityHome;
import com.example.behnam.app.ActivitySplashScreen;
import com.example.behnam.app.R;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.CategoryDrug;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.service.BroadcastReceivers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

public class Components extends AppController {
    public static void getDrugs(final Context context) {
        final DbHelper dbHelper = new DbHelper(context);
        if (dbHelper.getCount("drugs") <= 0) {
            // get drugs from server
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

                            // get categories from server
                            AppController.getInstance().sendRequest("android/api/category", null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getBoolean("status")) {
                                            JSONArray jsonArray = response.getJSONArray("categories");
                                            JSONObject object;
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                object = jsonArray.getJSONObject(i);
                                                dbHelper.addCategory(new Category(object.getInt("id"), object.getString("name"), object.getInt("type")));
                                            }

                                            // get categoryDrugs from server
                                            AppController.getInstance().sendRequest("android/api/categoryDrug", null, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        if (response.getBoolean("status")) {
                                                            Log.e("categoryDrug", response.toString());
                                                            JSONArray jsonArray = response.getJSONArray("list");
                                                            JSONObject object;
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                object = jsonArray.getJSONObject(i);
                                                                dbHelper.addCategoryDrug(new CategoryDrug(object.getInt("drug_id"), object.getInt("category_id"), object.getInt("type")));
                                                            }
                                                            // goto home activity
                                                            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                                                            assert am != null;
                                                            ComponentName currentActivity = am.getRunningTasks(1).get(0).topActivity;

                                                            if (!currentActivity.getClassName().equals(ActivityHome.class.getSimpleName())) {
                                                                SessionManager.getExtrasPref(context).putExtra("firstDataIsComplete", true);

                                                                Intent intent = new Intent(context, ActivityHome.class);
                                                                context.startActivity(intent);
                                                                ActivitySplashScreen.activitySplashScreen.finish();
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            // goto home activity
            Intent intent = new Intent(context, ActivityHome.class);
            context.startActivity(intent);
        }
    }

    public static void downloadData(Context context, String type) {
        final DbHelper dbHelper = new DbHelper(context);
        if (type.equals("first")) {
            if (dbHelper.getCount("drugs") <= 0) {
                ConnectivityManager connectivityManager;
                final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
                connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
                if (networkInfo == null || !networkInfo.isConnected()) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                    context.getApplicationContext().registerReceiver(new BroadcastReceivers(), intentFilter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);

                    builder.setView(inflater.inflate(R.layout.wifi_dialog, null));
                    builder.setMessage(R.string.enable_wifi).setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (wifiManager != null)
                                        wifiManager.setWifiEnabled(true);
                                }
                            }).show();
                } else
                    getDrugs(context);
            } else getDrugs(context);
        } else if (type.equals("complete")) {
            // download complete data
        }
    }
}
