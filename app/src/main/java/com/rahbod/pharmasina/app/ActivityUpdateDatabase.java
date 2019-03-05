package com.rahbod.pharmasina.app;

import android.app.DownloadManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rahbod.pharmasina.app.helper.DbHelper;

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
