package com.example.behnam.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.behnam.app.ActivityInterferenceStep2;
import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Behnam on 3/4/2018.
 */

public class AdapterInterferenceStep1 extends RecyclerView.Adapter<AdapterInterferenceStep1.drugListViewHolder> {

    Context context;
    List<Drug> drugList;

    public AdapterInterferenceStep1(Context context, List<Drug> drugList) {
        this.context = context;
        this.drugList = drugList;
    }

    @Override
    public AdapterInterferenceStep1.drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_search, parent, false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterInterferenceStep1.drugListViewHolder holder, final int position) {

        holder.drugName.setText(String.valueOf(drugList.get(position).getName()));
        holder.relativeLayoutRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityInterferenceStep2.class);
                SessionManager.getExtrasPref(context).putExtra("mainName", drugList.get(position).getName());
                SessionManager.getExtrasPref(context).putExtra("mainID", drugList.get(position).getId());
                SessionManager.getExtrasPref(context).putExtra("ID", String.valueOf(drugList.get(position)));

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }

    class drugListViewHolder extends RecyclerView.ViewHolder {

        TextView drugName;
        RelativeLayout relativeLayoutRec;

        public drugListViewHolder(View itemView) {
            super(itemView);

            drugName = itemView.findViewById(R.id.txt);
            relativeLayoutRec = itemView.findViewById(R.id.relativeLayoutRecHome);
        }
    }

    public void filterList(ArrayList<Drug> filterdNames) {
        this.drugList = filterdNames;
        notifyDataSetChanged();
    }
}
