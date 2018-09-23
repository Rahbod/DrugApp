package ir.rahbod.sinadrug.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.rahbod.sinadrug.app.R;
import ir.rahbod.sinadrug.app.adapter.AdapterInterferenceDrug;
import ir.rahbod.sinadrug.app.database.Index;
import ir.rahbod.sinadrug.app.helper.DbHelper;

import java.util.List;

public class FragmentInterferenceDrug extends Fragment {


    public FragmentInterferenceDrug() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_fragment_drug, null);

        int ID = getActivity().getIntent().getIntExtra("id", 0);

        //set recyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recInterference);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DbHelper dbHelper = new DbHelper(getActivity());
        List<Index> list = dbHelper.getAllInterferenceDrug(ID);
        AdapterInterferenceDrug adapter = new AdapterInterferenceDrug(getActivity(), list);
        recyclerView.setAdapter(adapter);

        if (list.isEmpty()){
            TextView txt = view.findViewById(R.id.txt);
            recyclerView.setVisibility(View.INVISIBLE);
            txt.setVisibility(View.VISIBLE);
        }


        return view;
    }

}
