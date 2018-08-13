package com.example.behnam.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.behnam.app.ActivityInterference;
import com.example.behnam.app.R;
import com.example.behnam.app.database.Index;

import java.util.ArrayList;
import java.util.List;


public class AdapterListInterference extends RecyclerView.Adapter<AdapterListInterference.drugListViewHolder> {

    Context context;
    List<Index> drugList;

    public AdapterListInterference(Context context, List<Index> drugList) {
        this.context = context;
        this.drugList = drugList;
    }

    @Override
    public AdapterListInterference.drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_search,parent,false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterListInterference.drugListViewHolder holder, int position) {
        final int pos = position;
        holder.drugName.setText(drugList.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ActivityInterference.class);
                intent.putExtra("id", drugList.get(pos).getId());
                intent.putExtra("name", drugList.get(pos).getName());
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
            drugName = itemView.findViewById(R.id.txtTitle);
            rel = itemView.findViewById(R.id.relativeLayoutRecHome);
        }
    }
    public void filterList(ArrayList<Index> filterdNames) {
        this.drugList = filterdNames;
        notifyDataSetChanged();
    }
}