package com.rahbod.pharmasina.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.rahbod.pharmasina.app.database.Index;
import com.rahbod.pharmasina.app.viewDrug;

import java.util.ArrayList;
import java.util.List;


public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.drugListViewHolder>  implements SectionIndexer {

    private ArrayList<Integer> mSectionPositions;
    private Context context;
    private List<Index> list;

    public AdapterSearch(Context context, List<Index> drugList) {
        this.context = context;
        this.list = drugList;
    }

    @Override
    public AdapterSearch.drugListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.rahbod.pharmasina.app.R.layout.rec_search, parent, false);
        return new drugListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSearch.drugListViewHolder holder, int position) {
        final int pos = position;
        holder.drugName.setText(list.get(position).getShowFieldValue());
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

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = list.size(); i < size; i++) {
            String section = String.valueOf(list.get(i).getName().substring(0, 1)).toUpperCase();
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

    class drugListViewHolder extends RecyclerView.ViewHolder {
        TextView drugName;
        RelativeLayout rel;

        drugListViewHolder(View itemView) {
            super(itemView);
            drugName = itemView.findViewById(com.rahbod.pharmasina.app.R.id.txtTitle);
            rel = itemView.findViewById(com.rahbod.pharmasina.app.R.id.relativeLayoutRecHome);
        }
    }

    public void filterList(ArrayList<Index> filterdNames) {
        this.list = filterdNames;
        notifyDataSetChanged();
    }
}