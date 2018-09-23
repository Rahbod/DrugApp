package ir.rahbod.sinadrug.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.rahbod.sinadrug.app.R;
import ir.rahbod.sinadrug.app.database.DropList;

import java.util.List;

import ir.rahbod.sinadrug.app.database.DropList;


public class AdapterDropList extends RecyclerView.Adapter<AdapterDropList.drugListViewHolder> {

    private Context context;
    private List<DropList> list;
    private Dialog dialog;
    private TextView name;
    private List<Integer> id;

    public AdapterDropList(Context context, List<DropList> drugList, Dialog dialog, TextView name, List<Integer> presenterId) {
        this.context = context;
        this.list = drugList;
        this.id = presenterId;
        this.dialog = dialog;
        this.name = name;
    }

    @Override
    public AdapterDropList.drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ir.rahbod.sinadrug.app.R.layout.rec_category, parent, false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterDropList.drugListViewHolder holder, int position) {
        final int pos = position;
        holder.drugName.setText(list.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id.clear();
                name.setText(list.get(pos).getName());
                id.add(list.get(pos).getId());
                dialog.dismiss();
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
            drugName = itemView.findViewById(ir.rahbod.sinadrug.app.R.id.txt);
            rel = itemView.findViewById(ir.rahbod.sinadrug.app.R.id.relCategory);
        }
    }
}