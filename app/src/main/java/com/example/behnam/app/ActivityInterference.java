package com.example.behnam.app;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.behnam.app.adapter.AdapterTabBar;
import com.example.behnam.fonts.FontTextView;
import com.example.behnam.fragment.FragmentCategory;
import com.example.behnam.fragment.FragmentDrug;

public class ActivityInterference extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interference);

        //back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout = findViewById(R.id.tabBar);
        viewPager =findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager(), this);
        for (int i = 0; i< tabLayout.getTabCount(); i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager());

        adapter.addFragment(new FragmentDrug(), "تداخل با دارو");
        adapter.addFragment(new FragmentCategory(), "تداخل با طبقه بندی");
        viewPager.setAdapter(adapter);
    }
}
