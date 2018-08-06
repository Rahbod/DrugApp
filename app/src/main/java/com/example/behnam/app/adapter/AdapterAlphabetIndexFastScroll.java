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

import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug2;
import com.example.behnam.app.database.Index;
import com.example.behnam.app.viewDrug;

import java.util.ArrayList;
import java.util.List;

public class AdapterAlphabetIndexFastScroll extends RecyclerView.Adapter<AdapterAlphabetIndexFastScroll.ListViewHollder>
        implements SectionIndexer {

    private ArrayList<Integer> mSectionPositions;
    private List<Index> drug2List;
    private Context context;

    public AdapterAlphabetIndexFastScroll(List<Index> drugList, Context context) {
        this.drug2List = drugList;
        this.context = context;
    }

    @Override
    public ListViewHollder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_search_home, parent, false);
        return new ListViewHollder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHollder holder, final int position) {
        holder.txt.setText(drug2List.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, viewDrug.class);
                intent.putExtra("id", drug2List.get(position).getId());
                intent.putExtra("vegetal", drug2List.get(position).getVegetal());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (drug2List == null)
            return 0;
        return drug2List.size();
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = drug2List.size(); i < size; i++) {
            String section = String.valueOf(drug2List.get(i).getName().substring(0, 1)).toUpperCase();
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

    class ListViewHollder extends RecyclerView.ViewHolder {

        TextView txt;
        RelativeLayout rel;

        ListViewHollder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            rel = itemView.findViewById(R.id.relativeLayoutRecHome);
        }
    }

    public void filterList(ArrayList<Index> filterdNames) {
        this.drug2List = filterdNames;
        notifyDataSetChanged();
    }
}
