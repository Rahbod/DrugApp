package com.example.behnam.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.behnam.app.ActivityErrorReport;
import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;

import java.util.ArrayList;
import java.util.List;


public class AdapterHome extends RecyclerView.Adapter<AdapterHome.drugListViewHolder> {
    Context context;
    List<Drug> drugList ;

    public AdapterHome(Context context, List<Drug> drugList) {
        this.context = context;
        this.drugList = drugList;
    }

    @Override
    public drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_search,parent,false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(drugListViewHolder holder, final int position) {
        holder.drugName.setText(drugList.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "PPPPPPPPPPPPPPPPPPP "+drugList.get(position).getName() );
                Intent intent = new Intent(context,ActivityErrorReport.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",drugList.get(position).getId());
                intent.putExtra("brand",drugList.get(position).getBrand());
                intent.putExtra("name",drugList.get(position).getName());
                intent.putExtra("pregnancy",drugList.get(position).getPregnancy());
                intent.putExtra("lactation",drugList.get(position).getLactation());
                intent.putExtra("kids",drugList.get(position).getKids());
                intent.putExtra("seniors",drugList.get(position).getSeniors());
                intent.putExtra("how_to_use",drugList.get(position).getHow_to_use());
                intent.putExtra("product",drugList.get(position).getProduct());
                intent.putExtra("pharmacodynamic",drugList.get(position).getPharmacodynamic());
                intent.putExtra("usage",drugList.get(position).getUsage());
                intent.putExtra("prohibition",drugList.get(position).getProhibition());
                intent.putExtra("caution",drugList.get(position).getCaution());
                intent.putExtra("dose_adjustment",drugList.get(position).getDose_adjustment());
                intent.putExtra("complication",drugList.get(position).getComplication());
                intent.putExtra("interference",drugList.get(position).getInterference());
                intent.putExtra("effect_on_test",drugList.get(position).getEffect_on_test());
                intent.putExtra("overdose",drugList.get(position).getOver_dose());
                intent.putExtra("description",drugList.get(position).getDescription());
                intent.putExtra("relation_with_food",drugList.get(position).getRelation_with_food());
                intent.putExtra("status",drugList.get(position).getStatus());
                intent.putExtra("last_modified",drugList.get(position).getLast_modified());
//                context.startActivity(intent);
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
