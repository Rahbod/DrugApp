package com.example.behnam.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.map.MapActivity;

public class ActivityIndex extends AppCompatActivity {

    private static long BackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }

    public void openActivity(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.interference:
                Intent intent = new Intent(ActivityIndex.this, ActivityListDrugInterference.class);
                startActivity(intent);
                break;
            case R.id.search:
                Intent intentSearch = new Intent(ActivityIndex.this, ActivityVegetalDrug.class);
                intentSearch.putExtra("search", "search");
                startActivity(intentSearch);
                break;
            case R.id.vegetalDrug:
                Intent intentVegetalDrug = new Intent(ActivityIndex.this, ActivityVegetalDrug.class);
                startActivity(intentVegetalDrug);
                break;
            case R.id.drug:
                Intent intentDrug = new Intent(ActivityIndex.this, ActivityHome.class);
                startActivity(intentDrug);
                break;
            case R.id.healing:
                Intent intentHealing = new Intent(ActivityIndex.this, ActivityCategories.class);
                intentHealing.putExtra("type", 0);
                startActivity(intentHealing);
                break;
            case R.id.pharma:
                Intent intentPharma = new Intent(ActivityIndex.this, ActivityCategories.class);
                intentPharma.putExtra("type", 1);
                startActivity(intentPharma);
                break;
            case R.id.location:
                Intent intentLocation = new Intent(ActivityIndex.this, MapActivity.class);
                startActivity(intentLocation);
                break;
            case R.id.reminder:
                Intent intentReminder = new Intent(ActivityIndex.this, ActivityReminderList.class);
                startActivity(intentReminder);
                break;
        }
    }

    @Override
    public void onBackPressed() {
         if (2000 + BackPressed > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "لطفا کلید برگشت را مجددا فشار دهید.", Toast.LENGTH_SHORT).show();
            BackPressed = System.currentTimeMillis();
        }
    }
}