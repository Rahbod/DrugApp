package com.rahbod.pharmasina.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.adapter.AdapterInterferenceDrug;
import com.rahbod.pharmasina.app.database.Index;
import com.rahbod.pharmasina.app.helper.DbHelper;

import java.util.List;

public class FragmentDrug extends Fragment {


    public FragmentDrug() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_drug, container, false);

        int ID = getActivity().getIntent().getIntExtra("id", 0);
        RecyclerView recyclerView = view.findViewById(R.id.recInterference);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DbHelper dbHelper = new DbHelper(getActivity());
        List<Index> list = dbHelper.getAllDrugInterference(ID);
        AdapterInterferenceDrug adapter = new AdapterInterferenceDrug(getActivity(), list);
        recyclerView.setAdapter(adapter);

        if (list.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            TextView txt = view.findViewById(R.id.txt);
            txt.setVisibility(View.VISIBLE);
        }
        return view;
    }

}
