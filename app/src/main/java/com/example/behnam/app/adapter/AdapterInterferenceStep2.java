package com.example.behnam.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;


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
            holder.txtChar.setBackgroundResource(R.drawable.checked2);
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
