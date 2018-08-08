package com.example.behnam.app.controller;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.behnam.app.ActivityHome;
import com.example.behnam.app.ActivityIndex;
import com.example.behnam.app.ActivitySplashScreen;
import com.example.behnam.app.R;
import com.example.behnam.app.database.Index;
import com.example.behnam.app.helper.DbHelper;


import net.gotev.speech.Speech;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class AppController extends Application {
    ProgressDialog pd;
    boolean withProgress = false;

    private static final int socketTimeout = 30000;
    private static final int socketRetries = 2;
    private static final String BASE_URL = "http://rahbod.com/";
    //    // visit
    private static final String CLIENT_ID = "UiRgEQt91vL60-8pixMTkxplOB-jAplL24rdfgDFgS6RTSf6eBGuFZ8ckmLXFT0nBRrz_6C5rGbmmY2f";
    private static final String CLIENT_SECRET = "huHxZTH6EZ_3Y8b71wcFetW34aFGc2P1x2H_yHVh9-uxB5lbcF12ds81mM8SB5IDWGZGpasd211";

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    private boolean async = true;

    @Override
    public void onCreate() {
        super.onCreate();
        // change font
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static synchronized AppController getInstance(Context context) {
        mInstance.withProgress = true;
        mInstance.pd = new ProgressDialog(context);
        mInstance.pd.setMessage("لطفا صبر کنید ...");
        mInstance.pd.setCancelable(false);
        mInstance.pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, socketRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        getRequestQueue().add(req);
    }

    /**
     * Cancel pending request
     *
     * @param tag of request
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Send Restful request with volley library to server
     *
     * @param url         for request
     * @param params      is Json object for request
     * @param resListener response listener has run after get response
     */
    public void sendRequest(String url, final JSONObject params, Response.Listener<JSONObject> resListener) {
        sendRequest(url, params, resListener, TAG);
    }

    /**
     * Send Restful request with volley library to server
     *
     * @param url         for request
     * @param params      is Json object for request
     * @param resListener response listener has run after get response
     * @param tag         of request
     */
    public void sendRequest(String url, final JSONObject params, Response.Listener<JSONObject> resListener, final String tag) {
        if (!isNetworkConnected())
            Toast.makeText(getApplicationContext(), "No internet access. Please check it.", Toast.LENGTH_LONG).show();
        else {
            if (withProgress)
                pd.show();
            final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL + url, params, resListener, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorHandler(error);
                    if (withProgress)
                        pd.dismiss();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Cache-Control", "no-cache");
                    String token = Base64.encodeToString(
                            (CLIENT_ID + ":" + CLIENT_SECRET).getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", "Basic " + token);
                    return headers;
                }
            };
            req.setTag(tag);
            addToRequestQueue(req);
        }
    }

    /**
     * Send Authenticate Restful request with volley library to server
     *
     * @param url         for request
     * @param params      is Json object for request
     * @param resListener response listener has run after get response
     */
//    public void sendAuthRequest(String url, final JSONObject params, Response.Listener<JSONObject> resListener) {
//        sendAuthRequest(url, params, resListener, TAG);
//    }

    /**
     * Send Authenticate Restful request with volley library to server
     *
     * @param url         for request
     * @param params      is Json object for request
     * @param resListener response listener has run after get response
     * @param tag         of request
     */
//    public void sendAuthRequest(String url, JSONObject params, Response.Listener<JSONObject> resListener, final String tag) {
//        if (!isNetworkConnected())
//            Toast.makeText(getApplicationContext(), "No internet access. Please check it.", Toast.LENGTH_LONG).show();
//        else {
//            if(withProgress)
//                pd.show();
//            final String accessToken = AccessTokenHelper.getAccessToken(getApplicationContext());
//            if (accessToken != null) {
//                if (params == null)
//                    params = new JSONObject();
//                final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL + url, params, resListener, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        errorHandler(error);
//                        if(withProgress)
//                            pd.dismiss();
//                    }
//                }) {
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> headers = new HashMap<>();
//                        headers.put("Content-Type", "application/json");
//                        headers.put("Authorization", "Bearer " + accessToken);
//                        return headers;
//                    }
//                };
//                req.setTag(tag);
//                addToRequestQueue(req);
//            }
//        }
//    }

    /**
     * Check Network and internet accessible or not.
     *
     * @return bool
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() && cm.getActiveNetworkInfo().isAvailable();
    }

    /**
     * Volley Callback interface for run callback after get volley response
     */
    public interface VolleyCallback {
        void onSuccessResponse(String result);

        void onErrorResponse(String result);
    }

    private void errorHandler(VolleyError error) {
        if (error instanceof TimeoutError) {
            Log.e("ResponseError", "زمان انتظار برای پاسخ از سرور به اتمام رسید.");
        } else if (error instanceof NoConnectionError) {
            Log.e("ResponseError", "خطای اتصال به اینترنت رخ داده است.");
        } else if (error instanceof AuthFailureError) {
            Log.e("ResponseError", "خطای احراز هویت رخ داده است.");
        } else if (error instanceof ServerError) {
            Log.e("ResponseError", "خطای سرور رخ داده است.");
        } else if (error instanceof NetworkError) {
            Log.e("ResponseError", "خطای دسترسی به شبکه رخ داده است.");
        } else if (error instanceof ParseError) {
            Log.e("ResponseError", "خطای پردازش پاسخ سرور رخ داده است.");
        } else
            Log.e("ResponseError", error.getMessage());
    }

    public void getSQLiteDb(final Context context) {
        String url = "android/api/download?id=1";
        File fileExists = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/drug.sql");
        DbHelper dbHelper = new DbHelper(context);
        if (dbHelper.getCount("drugs") == 0) {
            if (!fileExists.exists()) {
                downloadFile(url);
//                    DownloadFile downloadFile = new DownloadFile();
//                    downloadFile.execute(url);
            } else {
                getDrug(context);
            }
        } else {
            Intent intent = new Intent(context, ActivityIndex.class);
            startActivity(intent);
        }
    }

    public void downloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(BASE_URL + url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(String.valueOf(Environment.DIRECTORY_DOWNLOADS), "drug.sql");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        BroadcastReceiver endDownload = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getDrug(context);
            }
        };
        registerReceiver(endDownload, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private static void getDrug(Context context) {
        try {
            FileInputStream drug = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "drug.sql"));
            InputStreamReader reader = new InputStreamReader(drug);
            BufferedReader buffer = new BufferedReader(reader);
            //StringBuilder strDrug = new StringBuilder();
            String line;
            DbHelper dbHelper = new DbHelper(context);
            while ((line = buffer.readLine()) != null) {
                dbHelper.execSQL(line);
            }
            Intent intent = new Intent(context, ActivityIndex.class);
            context.startActivity(intent);

            //delete file
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "drug.sql");
            if (file.exists())
                file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class DownloadFile extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            String path = params[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
                File folder = new File("internalstorege");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}