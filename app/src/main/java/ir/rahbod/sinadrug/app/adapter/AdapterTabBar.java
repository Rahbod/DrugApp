package ir.rahbod.sinadrug.app.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ir.rahbod.sinadrug.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterTabBar extends FragmentPagerAdapter {

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

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    public View getTabView(int position) {
        String tabTitles[];
        tabTitles = new String[]{"تداخل با دارو", "تداخل با طبقه بندی"};
        View view = LayoutInflater.from(context).inflate(ir.rahbod.sinadrug.app.R.layout.tab, null);
        TextView txt = view.findViewById(ir.rahbod.sinadrug.app.R.id.txtTitle);
        txt.setText(tabTitles[position]);
        return view;
    }

    public View getTabViewDrug(int position) {
        String tabTitles[] = new String[]{"توضیحات دارو", "تداخل با دارو"};
        View view = LayoutInflater.from(context).inflate(ir.rahbod.sinadrug.app.R.layout.tab, null);
        TextView txt = view.findViewById(ir.rahbod.sinadrug.app.R.id.txtTitle);
        txt.setText(tabTitles[position]);
        return view;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}