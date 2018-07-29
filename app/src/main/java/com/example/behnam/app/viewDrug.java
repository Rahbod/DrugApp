package com.example.behnam.app;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.behnam.app.adapter.AdapterTabBar;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.fonts.FontTextView;
import com.example.behnam.fonts.FontTextViewBold;
import com.example.behnam.fragment.FragmentCategory;
import com.example.behnam.fragment.FragmentDrug;
import com.example.behnam.fragment.FragmentInterferenceDrug;
import com.example.behnam.fragment.FragmentViewDrug;

public class viewDrug extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drug);

        //btnBack
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // create TabBra
        TabLayout tabLayout = findViewById(R.id.tabBar);
        ViewPager viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager(), this);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabViewDrug(i));
        }

        int VEGETAL = Integer.parseInt(getIntent().getStringExtra("vegetal"));

        if (VEGETAL == 1) {
            btnBack.setBackground(getResources().getDrawable(R.drawable.background_focus_vegetal));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.greenVegetal));
            RelativeLayout rel = findViewById(R.id.header);
            rel.setBackgroundColor(getResources().getColor(R.color.greenVegetal));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.greenVegetalStatus));
            }
        }

        //set TextTitle
        FontTextViewBold nameDrug = findViewById(R.id.name_drug);
        FontTextView persianName = findViewById(R.id.persian_name);
        final int ID = getIntent().getIntExtra("id", 0);
        DbHelper dbHelper = new DbHelper(this);
        final Drug drug = dbHelper.getDrug(ID);
        nameDrug.setText(drug.getName());
        persianName.setText(drug.getNamePersian());

    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterTabBar adapter = new AdapterTabBar(getSupportFragmentManager());

        adapter.addFragment(new FragmentViewDrug(), "توضیحات دارو");
        adapter.addFragment(new FragmentInterferenceDrug(), "تداخل با دارو");
        viewPager.setAdapter(adapter);
    }
}
