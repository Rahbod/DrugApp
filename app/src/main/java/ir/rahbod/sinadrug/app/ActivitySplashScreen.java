package ir.rahbod.sinadrug.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.crashlytics.android.Crashlytics;
import ir.rahbod.sinadrug.ActivityCheckTransaction;
import ir.rahbod.sinadrug.ActivityGetLicense;
import ir.rahbod.sinadrug.ActivitySelectVersion;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.app.helper.SessionManager;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import ir.rahbod.sinadrug.ActivityCheckTransaction;
import ir.rahbod.sinadrug.ActivityGetLicense;
import ir.rahbod.sinadrug.ActivitySelectVersion;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.app.helper.SessionManager;

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
    private DownloadManager manager;
    private String idNumber, action;
    private String fileName = "";

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dbHelper = new DbHelper(ActivitySplashScreen.this);
        activitySplashScreen = this;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //close activity
        if (ActivityIndex.activityIndex != null)
            ActivityIndex.activityIndex.finish();

        if (ActivityCheckCode.activityCheckCode != null)
            ActivityCheckCode.activityCheckCode.finish();

        if (ActivitySelectVersion.activitySelectVersion != null)
            ActivitySelectVersion.activitySelectVersion.finish();

        if (ActivityCheckTransaction.activityCheckTransaction != null)
            ActivityCheckTransaction.activityCheckTransaction.finish();

        if (ActivityGetLicense.activityGetLicense != null)
            ActivityGetLicense.activityGetLicense.finish();

        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (getIntent().getStringExtra("id") != null)
            idNumber = getIntent().getStringExtra("id");
        action = getIntent().getStringExtra("action");

        //cancel Download Previous
        if (SessionManager.getExtrasPref(this).getLong("downloadId") != 0)
            manager.remove(SessionManager.getExtrasPref(this).getLong("downloadId"));


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
                spin.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.INVISIBLE);
                if (isConnected()) {
                    download(idNumber, action);
                } else {
                    spin.setVisibility(View.INVISIBLE);
                    btnDownload.setVisibility(View.VISIBLE);
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
                final Animation fadeProgress = AnimationUtils.loadAnimation(ActivitySplashScreen.this, R.anim.fade_anim);
                fadeProgress.setStartOffset(200);
                fadeProgress.setDuration(700);
                spin.setAnimation(fadeProgress);
                ThreeBounce threeBounce = new ThreeBounce();
                spin.setIndeterminateDrawable(threeBounce);
                spin.setVisibility(View.VISIBLE);
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
        if (ActivityCompat.checkSelfPermission(ActivitySplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (isConnected())
                download(idNumber, action);
            else if (wifi.isWifiEnabled()) {
                Toast.makeText(ActivitySplashScreen.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
                btnDownload.setVisibility(View.VISIBLE);
                spin.setVisibility(View.INVISIBLE);
            } else
                showWifiDialog();
        } else ActivityCompat.requestPermissions(ActivitySplashScreen.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isConnected()) {
                download(idNumber, action);
            } else if (wifi.isWifiEnabled()) {
                Toast.makeText(ActivitySplashScreen.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
                btnDownload.setVisibility(View.VISIBLE);
                spin.setVisibility(View.INVISIBLE);
            } else showWifiDialog();
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                spin.setVisibility(View.INVISIBLE);
                btnDownload.setVisibility(View.VISIBLE);
            }
        });
        Button btnCancel = viewDialogMassage.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isConnected())
                    getData();
                else finish();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void insertDatabase() {
        try {
            FileInputStream drug = new FileInputStream(new File(String.valueOf(getExternalFilesDir("sina/" + fileName))));
            InputStreamReader reader = new InputStreamReader(drug);
            BufferedReader buffer = new BufferedReader(reader);
            //StringBuilder strDrug = new StringBuilder();
            String line;
            DbHelper dbHelper = new DbHelper(ActivitySplashScreen.this);
            while ((line = buffer.readLine()) != null) {
                dbHelper.execSQL(line);
            }

            long now = System.currentTimeMillis() / 1000;
            SessionManager.getExtrasPref(this).putExtra("updateCheck", now);
            SessionManager.getExtrasPref(this).putExtra("lastSync", now);
            SessionManager.getExtrasPref(this).putExtra("selectedVersion", true);

            Intent intent = new Intent(ActivitySplashScreen.this, ActivityIndex.class);
            startActivity(intent);

            //delete file
            manager.remove(SessionManager.getExtrasPref(this).getLong("downloadId"));
            SessionManager.getExtrasPref(this).remove("downloadId");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
            return false;
        } else
            return true;
    }

    private void download(String id, String action) {
        txtDownload.setVisibility(View.VISIBLE);
        btnDownload.setVisibility(View.INVISIBLE);
        spin.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        txtPercent.setVisibility(View.VISIBLE);
        String url = "http://rahbod.ir/android/api/" + action + "?id=" + id;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        fileName = "drug-"+System.currentTimeMillis()+".sql";
        request.setDestinationInExternalFilesDir(this, "sina", "/"+fileName);
        final long downloadId = manager.enqueue(request);
        SessionManager.getExtrasPref(this).putExtra("downloadId", downloadId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    final Cursor cursor = manager.query(query);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int dl_progress;
                    if (bytes_total == 0)
                        dl_progress = 0;
                    else
                        dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setProgress(dl_progress);
                            txtPercent.setText("% " + dl_progress);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusMessage(cursor);
                            cursor.close();
                        }
                    });

                }

            }
        }).start();
    }

    private void statusMessage(Cursor c) {
        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_PAUSED:
                txtDownload.setText("دستگاه شما به اینترنت دسترسی ندارد.");
                txtDownload.setTextColor(getResources().getColor(R.color.red));
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                progressBar.setVisibility(View.INVISIBLE);
                txtPercent.setVisibility(View.INVISIBLE);
                txtDownload.setVisibility(View.VISIBLE);
                spin.setVisibility(View.VISIBLE);
                txtDownload.setText("در حال پردازش اطلاعات...");
                txtDownload.setTextColor(getResources().getColor(R.color.sina));
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        insertDatabase();
                    }
                };
                thread.start();
                break;
            case DownloadManager.STATUS_RUNNING:
                if (getIntent().getStringExtra("referer") != null)
                    txtDownload.setText("درحال بروزرسانی اطلاعات، لطفا منتظر بمانید...");
                else
                    txtDownload.setText("در حال دریافت اطلاعات، لطفا منتظر بمانید...");
                txtDownload.setTextColor(getResources().getColor(R.color.sina));
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
