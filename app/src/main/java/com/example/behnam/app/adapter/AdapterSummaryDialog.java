package com.example.behnam.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.behnam.app.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSummaryDialog extends RecyclerView.Adapter<AdapterSummaryDialog.listViewHolder> {

    Context context;
    List<String> list;
    List<String> listChar;

    public AdapterSummaryDialog(Context context, List<String> list, List<String> listChar) {
        this.context = context;
        this.list = list;
        this.listChar = listChar;
    }

    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rec_summary_dialog, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, int position) {
        holder.txt.setText(list.get(position));
        holder.txtChar.setText(listChar.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {

        TextView txt, txtChar;

        public listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            txtChar = itemView.findViewById(R.id.txtChar);
        }
    }
}
