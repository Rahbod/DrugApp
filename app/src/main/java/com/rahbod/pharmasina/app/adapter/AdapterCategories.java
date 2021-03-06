package com.rahbod.pharmasina.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rahbod.pharmasina.app.ActivityCategoryViewDrug;
import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.database.Category;
import com.rahbod.pharmasina.app.helper.DbHelper;
import com.rahbod.pharmasina.app.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.listViewHolder> {

    Context context;
    List<Category> listDrug;
    JSONArray parentIDList;

    public AdapterCategories(Context context, List<Category> listDrug) {
        this.context = context;
        this.listDrug = listDrug;
        String strList = SessionManager.getExtrasPref(context).getString("idList");

        if (!strList.isEmpty()) {
            try {
                parentIDList = new JSONArray(strList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            parentIDList = new JSONArray();

    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.rahbod.pharmasina.app.R.layout.rec_category, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txt.setText(listDrug.get(position).getName());
        holder.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = listDrug.get(position).getId();
                String title = listDrug.get(position).getName();
                int parentID = listDrug.get(position).getParentID();
                parentIDList.put(parentID);
                SessionManager.getExtrasPref(context).putExtra("idList", parentIDList.toString());
                String condition = "parent_id=" + listDrug.get(position).getId();
                DbHelper dbHelper = new DbHelper(context);
                listDrug = dbHelper.getCategories(0, condition);
                if (listDrug.isEmpty()) {
                    Intent intent = new Intent(context, ActivityCategoryViewDrug.class);
                    intent.putExtra("id", ID);
                    intent.putExtra("title", title);
                    context.startActivity(intent);
                    SessionManager.getExtrasPref(context).putExtra("idList", parentIDList.toString());
                } else {
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDrug.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {

        TextView txt;
        RelativeLayout relItem;

        public listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(com.rahbod.pharmasina.app.R.id.txt);
            relItem = itemView.findViewById(com.rahbod.pharmasina.app.R.id.relCategory);
        }
    }

    public void filterList(ArrayList<Category> filterdNames) {
        this.listDrug = filterdNames;
        notifyDataSetChanged();
    }
}
