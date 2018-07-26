package com.example.behnam.app.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.behnam.app.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterTabBar extends FragmentPagerAdapter{

    Context context;

    public AdapterTabBar(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();

    public AdapterTabBar(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        titleList.add(title);
    }

    public View getTabView(int position){
        String tabTitles[] = new String[] { "تداخل با دارو", "تداخل با طبقه بندی" };
        View view = LayoutInflater.from(context).inflate(R.layout.tab, null);
        TextView txt = view.findViewById(R.id.txtTitle);
        txt.setText(tabTitles[position]);
        return view;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
