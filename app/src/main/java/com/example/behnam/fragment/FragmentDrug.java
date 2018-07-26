package com.example.behnam.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.behnam.app.R;
import com.example.behnam.app.adapter.AdapterInterferenceDrug;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Interference;
import com.example.behnam.app.helper.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrug extends Fragment {


    public FragmentDrug() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_drug, container, false);

        int ID = getActivity().getIntent().getIntExtra("id", 0);
        RecyclerView recyclerView = view.findViewById(R.id.recInterferenceDrug);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DbHelper dbHelper = new DbHelper(getActivity());
        List<Drug> list = dbHelper.getDrugInterference(ID);
        AdapterInterferenceDrug adapter = new AdapterInterferenceDrug(getActivity(), list);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
