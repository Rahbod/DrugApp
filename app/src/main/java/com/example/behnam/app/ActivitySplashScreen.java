package com.example.behnam.app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.helper.DbHelper;
import com.github.ybq.android.spinkit.style.ThreeBounce;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.grabner.circleprogress.CircleProgressView;

public class ActivitySplashScreen extends AppCompatActivity {
    private LottieAnimationView animSplashScreen;
    private TextView txtNameLogo;
    public static Activity activitySplashScreen = null;
    private DbHelper dbHelper;
    private Button btnDownload;
    private WifiManager wifi;
    private ProgressBar spin;
    private ProgressBar progressBar;
    private TextView txtDownload;
    private TextView txtPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dbHelper = new DbHelper(ActivitySplashScreen.this);
        activitySplashScreen = this;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        btnDownload = findViewById(R.id.btnDownload);
        wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        spin = findViewById(R.id.spin);
        progressBar = findViewById(R.id.progressBarDownload);
        txtDownload = findViewById(R.id.txtDownload);
        txtPercent = findViewById(R.id.txtPercent);

        //check network
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getInstance().isConnected()) {
                    downloadFile();
                } else {
                    Toast.makeText(ActivitySplashScreen.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Lottie anim
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animSplashScreen = findViewById(R.id.animSplashScreen);
                animSplashScreen.playAnimation();
            }
        }, 300);

        //        anim name
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtNameLogo = findViewById(R.id.txtNameSplash);
                txtNameLogo.setVisibility(View.VISIBLE);
                Animation fadeText = AnimationUtils.loadAnimation(ActivitySplashScreen.this, R.anim.fade_anim);
                fadeText.setStartOffset(200);
                fadeText.setDuration(700);
                txtNameLogo.setAnimation(fadeText);
            }
        }, 1050);

//        anim progressBar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fadeProgress = AnimationUtils.loadAnimation(ActivitySplashScreen.this, R.anim.fade_anim);
                fadeProgress.setStartOffset(200);
                fadeProgress.setDuration(700);
                spin.setVisibility(View.VISIBLE);
                spin.setAnimation(fadeProgress);
                ThreeBounce threeBounce = new ThreeBounce();
                spin.setIndeterminateDrawable(threeBounce);
            }
        }, 1750);

        //Intent to Home activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 3000);
    }

    public void getData() {
        if (dbHelper.getCount("drugs") == 0) {
            if (ActivityCompat.checkSelfPermission(ActivitySplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (AppController.getInstance().isConnected())
                    downloadFile();
                else if (wifi.isWifiEnabled()) {
                    Toast.makeText(ActivitySplashScreen.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
                    btnDownload.setVisibility(View.VISIBLE);
                    spin.setVisibility(View.INVISIBLE);
                } else showWifiDialog();
            } else ActivityCompat.requestPermissions(ActivitySplashScreen.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Intent intent = new Intent(ActivitySplashScreen.this, ActivityIndex.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (wifi.isWifiEnabled())
                downloadFile();
            else showWifiDialog();
        } else {
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void showWifiDialog() {
        assert wifi != null;
        View viewDialogMassage = LayoutInflater.from(this).inflate(R.layout.massage_dialog, null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(viewDialogMassage);
        LinearLayout linMassageDialog = viewDialogMassage.findViewById(R.id.linDialogMassage);
        linMassageDialog.setVisibility(View.VISIBLE);
        TextView txt = viewDialogMassage.findViewById(R.id.txt);
        txt.setText(R.string.enable_wifi);
        Button btnOk = viewDialogMassage.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                wifi.setWifiEnabled(true);
                btnDownload.setVisibility(View.VISIBLE);
                spin.setVisibility(View.INVISIBLE);
            }
        });
        Button btnCancel = viewDialogMassage.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ActivitySplashScreen.activitySplashScreen.finish();
            }
        });
        dialog.show();
    }

    private void downloadFile() {
        final String url = "android/api/download?id=1";
        new DownloadFile().execute("http://rahbod.com/" + url);
    }

    private void getDrug() {
        try {
            FileInputStream drug = new FileInputStream(new File(Environment.getExternalStorageDirectory() + File.separator + "sina/", "drug.sql"));
            InputStreamReader reader = new InputStreamReader(drug);
            BufferedReader buffer = new BufferedReader(reader);
            //StringBuilder strDrug = new StringBuilder();
            String line;
            DbHelper dbHelper = new DbHelper(ActivitySplashScreen.this);
            while ((line = buffer.readLine()) != null) {
                dbHelper.execSQL(line);
            }
            Intent intent = new Intent(ActivitySplashScreen.this, ActivityIndex.class);
            startActivity(intent);

            //delete file
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "sina/drug.sql");
            if (file.exists()) {
                file.delete();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class DownloadFile extends AsyncTask<String, String, String> {
        File directory;
        private String folder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtDownload.setVisibility(View.VISIBLE);
            spin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(100);
            txtPercent.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... f_url) {
            Log.e("qqqq", "doInBackground: ");
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "sina/";

                //Create androiddeft folder if it does not exist
                directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + "drug.sql");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + "drug.sql";

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressBar.setProgress(Integer.parseInt(progress[0]));
            txtPercent.setText(" %" + progress[0]);
        }


        @Override
        protected void onPostExecute(String message) {
            progressBar.setVisibility(View.INVISIBLE);
            txtPercent.setVisibility(View.INVISIBLE);
            txtDownload.setVisibility(View.VISIBLE);
            spin.setVisibility(View.VISIBLE);
            txtDownload.setText("در حال پردازش اطلاعات...");
            Thread thread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    getDrug();
                }
            };
            thread.start();
        }
    }
}