package com.example.behnam.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;

import java.util.ArrayList;
import java.util.List;


public class AdapterInterferenceStep2Tag extends RecyclerView.Adapter<AdapterInterferenceStep2Tag.drugListViewHolder> {

    Context context;
    List<Drug> drugList ;

    public AdapterInterferenceStep2Tag(Context context, List<Drug> drugList) {
        this.context = context;
        this.drugList = drugList;
    }

    @Override
    public drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_search,parent,false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(drugListViewHolder holder, int position) {

        holder.drugName.setText(drugList.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return drugList.size() ;
    }

    class drugListViewHolder extends RecyclerView.ViewHolder
    {
        TextView drugName;
        RelativeLayout rel;
        drugListViewHolder(View itemView) {
            super(itemView);
            drugName = itemView.findViewById(R.id.txt);
            rel = itemView.findViewById(R.id.relativeLayoutRecHome);
        }
    }
    public void filterList(ArrayList<Drug> filterdNames) {
        this.drugList = filterdNames;
        notifyDataSetChanged();
    }
}
