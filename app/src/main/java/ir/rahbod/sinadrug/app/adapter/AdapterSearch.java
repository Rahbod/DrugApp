package ir.rahbod.sinadrug.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.rahbod.sinadrug.app.ActivityInterference;
import ir.rahbod.sinadrug.app.R;
import ir.rahbod.sinadrug.app.database.Index;
import ir.rahbod.sinadrug.app.viewDrug;

import java.util.ArrayList;
import java.util.List;

import ir.rahbod.sinadrug.app.database.Index;
import ir.rahbod.sinadrug.app.viewDrug;


public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.drugListViewHolder> {

    private Context context;
    private List<Index> list;

    public AdapterSearch(Context context, List<Index> drugList) {
        this.context = context;
        this.list = drugList;
    }

    @Override
    public AdapterSearch.drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ir.rahbod.sinadrug.app.R.layout.rec_search, parent, false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSearch.drugListViewHolder holder, int position) {
        final int pos = position;
        holder.drugName.setText(list.get(position).getName());
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

    class drugListViewHolder extends RecyclerView.ViewHolder {
        TextView drugName;
        RelativeLayout rel;

        drugListViewHolder(View itemView) {
            super(itemView);
            drugName = itemView.findViewById(ir.rahbod.sinadrug.app.R.id.txtTitle);
            rel = itemView.findViewById(ir.rahbod.sinadrug.app.R.id.relativeLayoutRecHome);
        }
    }

    public void filterList(ArrayList<Index> filterdNames) {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}