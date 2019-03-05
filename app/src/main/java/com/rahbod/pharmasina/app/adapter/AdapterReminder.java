package com.rahbod.pharmasina.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rahbod.pharmasina.app.ActivityReminderStep2;
import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.database.Index;

import java.util.ArrayList;
import java.util.List;


public class AdapterReminder extends RecyclerView.Adapter<AdapterReminder.drugListViewHolder> {

    Context context;
    List<Index> drugList;

    public AdapterReminder(Context context, List<Index> drugList) {
        this.context = context;
        this.drugList = drugList;
    }

    @Override
    public AdapterReminder.drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.rahbod.pharmasina.app.R.layout.rec_search,parent,false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterReminder.drugListViewHolder holder, int position) {
        final int pos = position;
        holder.drugName.setText(drugList.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ActivityReminderStep2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", drugList.get(pos).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }
    class drugListViewHolder extends RecyclerView.ViewHolder
    {
        TextView drugName;
        RelativeLayout rel;
        drugListViewHolder(View itemView) {
            super(itemView);
            drugName = itemView.findViewById(com.rahbod.pharmasina.app.R.id.txtTitle);
            rel = itemView.findViewById(com.rahbod.pharmasina.app.R.id.relativeLayoutRecHome);
        }
    }
    public void filterList(ArrayList<Index> filterdNames) {
        this.drugList = filterdNames;
        notifyDataSetChanged();
    }
}