package com.rahbod.pharmasina.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.adapter.AdapterCategoryInterference;
import com.rahbod.pharmasina.app.database.Category;
import com.rahbod.pharmasina.app.helper.Components;
import com.rahbod.pharmasina.app.helper.DbHelper;
import com.rahbod.pharmasina.app.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentCategory extends Fragment {
    private static RecyclerView recyclerView;
    private static DbHelper dbHelper;
    private static List<Category> list;
    private static AdapterCategoryInterference adapter;
    private static int type;

    public FragmentCategory() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_drug, container, false);

        type = getActivity().getIntent().getIntExtra("type", 0);
        int ID = getActivity().getIntent().getIntExtra("id", 0);
        recyclerView = view.findViewById(R.id.recInterference);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbHelper = new DbHelper(getActivity());
        list = dbHelper.getCategoryInterference(ID);
        adapter = new AdapterCategoryInterference(getActivity(), list);
        recyclerView.setAdapter(adapter);

        if (list.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            TextView txt = view.findViewById(R.id.txt);
            txt.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public static boolean goBack(Context context) {
        if (SessionManager.getExtrasPref(context).getString("interferenceIdList").isEmpty())
            return false;
        String strList = SessionManager.getExtrasPref(context).getString("interferenceIdList");
        try {
            JSONArray parentListId = new JSONArray(strList);
            if (parentListId.length() > 0) {
                int parentID = parentListId.getInt(parentListId.length() - 1);
                parentListId = Components.jsonArrayRemove(parentListId, parentListId.length() - 1);
                SessionManager.getExtrasPref(context).putExtra("interferenceIdList", parentListId.toString());
                list = dbHelper.getCategories(type, "parent_id=" + parentID);
                adapter = new AdapterCategoryInterference(context, list);
                recyclerView.setAdapter(adapter);
            } else {
               return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // sort list
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return true;
    }

    public void getDataToList() {
        String strList = SessionManager.getExtrasPref(getActivity()).getString("interferenceIdList");
        try {
            JSONArray jsonArray = new JSONArray(strList);
            int id = jsonArray.getInt(jsonArray.length() - 1);
            list = dbHelper.getCategories(type, "parent_id=" + id);
            jsonArray = Components.jsonArrayRemove(jsonArray, jsonArray.length() - 1);
            SessionManager.getExtrasPref(getActivity()).putExtra("interferenceIdList", jsonArray.toString());
            adapter = new AdapterCategoryInterference(getActivity(), list);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // sort list
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    @Override
    public void onResume() {
        getDataToList();
        super.onResume();
    }
}
