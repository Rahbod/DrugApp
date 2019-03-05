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

import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.database.Index;
import com.rahbod.pharmasina.app.viewDrug;

import java.util.ArrayList;
import java.util.List;

public class AdapterDrugByCategory extends RecyclerView.Adapter<AdapterDrugByCategory.listViewHolder> {

    Context context;
    private List<Index> listDrug;

    public AdapterDrugByCategory(Context context, List<Index> listDrug) {
        this.context = context;
        this.listDrug = listDrug;
    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.rahbod.pharmasina.app.R.layout.rec_search, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txt.setText(listDrug.get(position).getName());
        holder.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, viewDrug.class);
                intent.putExtra("id", listDrug.get(position).getId());
                intent.putExtra("vegetal", listDrug.get(position).getVegetal());
                context.startActivity(intent);
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
            txt = itemView.findViewById(com.rahbod.pharmasina.app.R.id.txtTitle);
            relItem = itemView.findViewById(com.rahbod.pharmasina.app.R.id.relativeLayoutRecHome);
        }
    }

    public void filterList(ArrayList<Index> filterNames) {
        this.listDrug = filterNames;
        notifyDataSetChanged();
    }
}
