package com.example.behnam.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.behnam.app.ActivityViewDrug;
import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.viewDrug;

import java.util.ArrayList;
import java.util.List;

public class AdapterDrugByCategory extends RecyclerView.Adapter<AdapterDrugByCategory.listViewHolder> {

    Context context;
    private List<Drug> listDrug;

    public AdapterDrugByCategory(Context context, List<Drug> listDrug) {
        this.context = context;
        this.listDrug = listDrug;
    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rec_search, parent, false);
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
            txt = itemView.findViewById(R.id.txt);
            relItem = itemView.findViewById(R.id.relativeLayoutRecHome);
        }
    }

    public void filterList(ArrayList<Drug> filterNames) {
        this.listDrug = filterNames;
        notifyDataSetChanged();
    }
}