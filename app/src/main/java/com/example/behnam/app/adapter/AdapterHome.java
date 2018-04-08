package com.example.behnam.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.ActivityViewDrug;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;

import java.util.ArrayList;
import java.util.List;


public class AdapterHome extends RecyclerView.Adapter<AdapterHome.drugListViewHolder> implements SectionIndexer {
    DbHelper dbHelper ;
    List<Category>categoryList = new ArrayList<>();
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
    public void onBindViewHolder(drugListViewHolder holder, final int position) {
        holder.drugName.setText(drugList.get(position).getName());
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "PPPPPPPPPPPPPPPPPPP "+drugList.get(position).getName() );
                dbHelper.getCategoriesByDrug(drugList.get(position).getId());
                Log.e("TAG55555555555", String.valueOf(categoryList.size()));
                Intent intent = new Intent(context,ActivityViewDrug.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",drugList.get(position).getId());
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
