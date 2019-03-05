package com.rahbod.pharmasina.app.adapter;

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

import java.util.List;

public class AdapterCategoryInterference extends RecyclerView.Adapter<AdapterCategoryInterference.listViewHolder> {

    Context context;
    List<Category> list;
    JSONArray parentIDList;

    public AdapterCategoryInterference(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
        String strList = SessionManager.getExtrasPref(context).getString("interferenceIdList");
        if (!strList.isEmpty()) {
            try {
                parentIDList = new JSONArray(strList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            parentIDList = new JSONArray();
    }

    @NonNull
    @Override
    public AdapterCategoryInterference.listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.rahbod.pharmasina.app.R.layout.rec_category, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, final int position) {
        holder.txt.setText(list.get(position).getName());
        holder.relCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = list.get(position).getId();
                int parentID = list.get(position).getParentID();
                parentIDList.put(parentID);
                SessionManager.getExtrasPref(context).putExtra("interferenceIdList", parentIDList.toString());
                String condition = "parent_id=" + list.get(position).getId();
                DbHelper dbHelper = new DbHelper(context);
                list = dbHelper.getCategories(0, condition);
                if (list.isEmpty()) {
                    Intent intent = new Intent(context, ActivityCategoryViewDrug.class);
                    intent.putExtra("id", ID);
                    context.startActivity(intent);
                    SessionManager.getExtrasPref(context).putExtra("interferenceIdList", parentIDList.toString());
                } else {
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {

        TextView txt;
        RelativeLayout relCategory;

        public listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(com.rahbod.pharmasina.app.R.id.txt);
            relCategory = itemView.findViewById(com.rahbod.pharmasina.app.R.id.relCategory);
        }
    }
}
