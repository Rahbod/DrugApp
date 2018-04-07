package com.example.behnam.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.behnam.app.R;
import com.example.behnam.app.ViewDrug;
import com.example.behnam.app.database.Drug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Behnam on 3/7/2018.
 */

public class AdapterAlphabetIndexFastScroll extends RecyclerView.Adapter<AdapterAlphabetIndexFastScroll.ListViewHollder>
        implements SectionIndexer {

    private ArrayList<Integer> mSectionPositions;
    private List<Drug> drugList;
    private Context context;

    public AdapterAlphabetIndexFastScroll(List<Drug> dataset){
        drugList = dataset;
    }

    public AdapterAlphabetIndexFastScroll(List<Drug> drugList, Context context) {
        this.drugList = drugList;
        this.context = context;
    }

    @Override
    public ListViewHollder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_search, parent, false);
        return new ListViewHollder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHollder holder, final int position) {
        holder.txt.setText(drugList.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent(context.getApplicationContext(),ViewDrug.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id",drugList.get(position).getId());
                        context.startActivity(intent);
                    }
        });
    }

    @Override
    public int getItemCount() {
        if (drugList == null)
            return 0;
        return drugList.size();
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = drugList.size(); i < size; i++) {
            String section = String.valueOf(drugList.get(i).getName().substring(0, 1)).toUpperCase();
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

    public class ListViewHollder extends RecyclerView.ViewHolder {

        TextView txt;
        RelativeLayout rel;
        public ListViewHollder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            rel = itemView.findViewById(R.id.relativeLayoutRecHome);
        }
    }
    public void filterList(ArrayList<Drug> filterdNames) {
        this.drugList = filterdNames;
        notifyDataSetChanged();
    }
}
