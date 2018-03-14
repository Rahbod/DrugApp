package com.example.behnam.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.behnam.app.ActivityErrorReport;
import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;

import java.util.ArrayList;
import java.util.List;


public class AdapterHome extends RecyclerView.Adapter<AdapterHome.drugListViewHolder> implements SectionIndexer {

    Context context;
    List<Drug> drugList ;
    private List<String> mDataArray;
    private ArrayList<Integer> mSectionPositions;

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
    public void onBindViewHolder(drugListViewHolder holder, int position) {
        final int pos = position;
        holder.drugName.setText(drugList.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ActivityErrorReport.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("brand",drugList.get(pos).getBrand());
                intent.putExtra("name",drugList.get(pos).getName());
                intent.putExtra("pregnancy",drugList.get(pos).getPregnancy());
                intent.putExtra("lactation",drugList.get(pos).getLactation());
                intent.putExtra("kids",drugList.get(pos).getKids());
                intent.putExtra("seniors",drugList.get(pos).getSeniors());
                intent.putExtra("how_to_use",drugList.get(pos).getHow_to_use());
                intent.putExtra("product",drugList.get(pos).getProduct());
                intent.putExtra("pharmacodynamic",drugList.get(pos).getPharmacodynamic());
                intent.putExtra("usage",drugList.get(pos).getUsage());
                intent.putExtra("prohibition",drugList.get(pos).getProhibition());
                intent.putExtra("caution",drugList.get(pos).getCaution());
                intent.putExtra("dose_adjustment",drugList.get(pos).getDose_adjustment());
                intent.putExtra("complication",drugList.get(pos).getComplication());
                intent.putExtra("interference",drugList.get(pos).getInterference());
                intent.putExtra("effect_on_test",drugList.get(pos).getEffect_on_test());
                intent.putExtra("overdose",drugList.get(pos).getOver_dose());
                intent.putExtra("description",drugList.get(pos).getDescription());
                intent.putExtra("relation_with_food",drugList.get(pos).getRelation_with_food());
                intent.putExtra("status",drugList.get(pos).getStatus());
                intent.putExtra("last_modified",drugList.get(pos).getLast_modified());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drugList.size() ;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = mDataArray.size(); i < size; i++) {
            String section = String.valueOf(mDataArray.get(i).charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
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
