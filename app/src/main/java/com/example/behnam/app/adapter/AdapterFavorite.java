package com.example.behnam.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;

import java.util.List;

/**
 * Created by Behnam on 3/19/2018.
 */

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.listViewHolder> {

    Context context;
    List<Drug> list;

    public AdapterFavorite(Context context, List<Drug> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rec_search, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, int position) {
        holder.txt.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder{

        TextView txt;
        public listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
        }
    }
}
