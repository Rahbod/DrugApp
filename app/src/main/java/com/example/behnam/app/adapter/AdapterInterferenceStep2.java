package com.example.behnam.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class AdapterInterferenceStep2 extends RecyclerView.Adapter<AdapterInterferenceStep2.drugListViewHolder> {
    List<String> idsList = new ArrayList<>();
    private Context context;
    private List<Drug> drugList;

    public AdapterInterferenceStep2(Context context, List<Drug> drugList) {
        this.context = context;
        this.drugList = drugList;
    }
    @Override
    public drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_search_interference, parent, false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final drugListViewHolder holder, final int position) {
        holder.drugName.setText(drugList.get(position).getName());
        holder.strName = drugList.get(position).getName();
        holder.chName = holder.strName.charAt(0);
        holder.txtChar.setText(String.valueOf(holder.chName));

        if (drugList.get(position).getChecked()) {
            holder.txtChar.setBackgroundResource(R.drawable.checked);
            holder.txtChar.setText("");
        } else {
            holder.txtChar.setBackgroundResource(R.drawable.shape_circle);
            holder.txtChar.setText(String.valueOf(holder.chName));
        }

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!idsList.contains(String.valueOf(drugList.get(position).getId()))) {
                    idsList.add(String.valueOf(drugList.get(position).getId()));
                    drugList.get(position).setChecked(true);
                } else {
                    idsList.remove(String.valueOf(drugList.get(position).getId()));
                    drugList.get(position).setChecked(false);
                }
                SessionManager.getExtrasPref(context).putExtra("selectedIDs", idsList.toString());
                notifyDataSetChanged();
            }
        });

//        holder.rel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<String> ids = new ArrayList<>();
//                SessionManager.getExtrasPref(context).putExtra(drugList.get(po));
//
//                holder.txtChar.setBackgroundResource(R.drawable.checked);
//                holder.txtChar.setText("");
////
//                JSONArray ids = new JSONArray();
//                String idsJson = SessionManager.getExtrasPref(context).getString("selectedIDs");
//                if (!idsJson.isEmpty()) {
//                    try {
//                        ids = new JSONArray(idsJson);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Boolean idInArray = false;
//                int idInArrayPos = 0;
//                for (int i = 0; i < ids.length(); i++) {
//                    try {
//                        if (ids.getInt(i) == drugList.get(position).getId()) {
//                            idInArray = true;
//                            idInArrayPos = i;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (!idInArray)
//                    ids.put(drugList.get(position).getId());
//                else
//                    ids.remove(idInArrayPos);
//                SessionManager.getExtrasPref(context).putExtra("selectedIDs", ids.toString());
//
//                // put selected drug position to json object if not exists
//                String selectedPositions = SessionManager.getExtrasPref(context).getString("selectedPositions");
//                if (!selectedPositions.isEmpty()) {
//                    try {
//                        selectedPositionsObj = new JSONObject(selectedPositions);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                try {
//                    selectedPositionsObj.put(drugList.get(position).getName(), position);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                DbHelper dbHelper = new DbHelper(context);
//                Drug selectedDrug = dbHelper.getDrug(drugList.get(position).getId());
//                Boolean existsName = false;
//                int duplicateIndex = 0;
//                for (int i = 0; i < drugListTag.size(); i++) {
//                    try {
//                        if (Objects.equals(drugListTag.get(i).getName(), selectedDrug.getName())) {
//                            existsName = true;
//                            duplicateIndex = i;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if (existsName) {
//                    drugListTag.remove(duplicateIndex);
//                    selectedPositionsObj.remove(drugList.get(position).getName());
//                    holder.txtChar.setBackgroundResource(R.drawable.shape_circle);
//                    holder.txtChar.setText(String.valueOf(holder.chName));
//                } else {
//                    drugListTag.add(selectedDrug);
//                    SessionManager.getExtrasPref(context).putExtra("tag-drug-" + (drugListTag.size() - 1), "");
//                }
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }

    class drugListViewHolder extends RecyclerView.ViewHolder {
        TextView drugName, txtChar;
        LinearLayout rel;
        String strName;
        char chName;

        drugListViewHolder(View itemView) {
            super(itemView);
            drugName = itemView.findViewById(R.id.txt);
            txtChar = itemView.findViewById(R.id.txtChar);
            rel = itemView.findViewById(R.id.relativeLayoutRecHome);
        }

    }

    public void filterList(ArrayList<Drug> filterdNames) {
        this.drugList = filterdNames;
        notifyDataSetChanged();
    }
}
