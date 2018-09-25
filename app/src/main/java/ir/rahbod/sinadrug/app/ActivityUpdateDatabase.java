package ir.rahbod.sinadrug.app;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Response;
import ir.rahbod.sinadrug.app.controller.AppController;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityUpdateDatabase extends AppCompatActivity {
    private DownloadManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_update_database);

        DbHelper dbHelper = new DbHelper(this);
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);



    }
}
