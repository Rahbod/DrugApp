package com.example.behnam.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.behnam.app.R;
import com.example.behnam.app.adapter.AdapterCategoryInterference;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.helper.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment {


    public FragmentCategory() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_category, container, false);

        int ID = getActivity().getIntent().getIntExtra("id", 0);
        RecyclerView recyclerView = view.findViewById(R.id.recCategoryInterference);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DbHelper dbHelper= new DbHelper(getActivity());
        List<Category> list = dbHelper.getCategoryInterference(ID);
        AdapterCategoryInterference adapter = new AdapterCategoryInterference(getActivity(), list);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
