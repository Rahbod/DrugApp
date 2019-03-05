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

import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.database.Index;
import com.rahbod.pharmasina.app.viewDrug;

import java.util.ArrayList;
import java.util.List;

public class AdapterInterferenceDrug extends RecyclerView.Adapter<AdapterInterferenceDrug.listViewHolder> {

    Context context;
    List<Index> list;

    public AdapterInterferenceDrug(Context context, List<Index> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.rahbod.pharmasina.app.R.layout.rec_search, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, int position) {
        final int pos = position;
        holder.txt.setText(list.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, viewDrug.class);
                intent.putExtra("id", list.get(pos).getId());
                intent.putExtra("vegetal", list.get(pos).getVegetal());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {

        TextView txt;
        RelativeLayout rel;

        public listViewHolder(View itemView) {
            super(itemView);

            txt = itemView.findViewById(com.rahbod.pharmasina.app.R.id.txtTitle);
            rel = itemView.findViewById(com.rahbod.pharmasina.app.R.id.relativeLayoutRecHome);
        }
    }
    public void filterList(ArrayList<Index> filterdNames) {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}
