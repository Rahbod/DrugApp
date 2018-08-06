package com.example.behnam.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.behnam.app.ActivityFavorite;
import com.example.behnam.app.ActivityViewDrug;
import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Drug2;
import com.example.behnam.app.helper.DbHelper;

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
        View view = LayoutInflater.from(context).inflate(R.layout.rec_favorite, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final listViewHolder holder, final int position) {
        holder.txtName.setText(list.get(position).getName());
        holder.LinearLayoutFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityViewDrug.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("vegetal", list.get(position).getVegetal());
                context.startActivity(intent);
            }
        });
        final View view = LayoutInflater.from(context).inflate(R.layout.massage_dialog, null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(view);
        final Button btnOk = view.findViewById(R.id.btnOk);
        final Button btnCancel = view.findViewById(R.id.btnCancel);
        final TextView txt = view.findViewById(R.id.txt);
        LinearLayout linDialogMassage = view.findViewById(R.id.linDialogMassage);
        linDialogMassage.setVisibility(View.VISIBLE);
        txt.setText("آیا میخواهید داروی "+ list.get(position).getName() +" را حذف کنید؟");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        holder.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof ActivityFavorite) {
                            DbHelper dbHelper = new DbHelper(context);
                            dbHelper.bookMark(list.get(position).getId());
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            dialog.dismiss();
                            if (list.isEmpty()) {
                                new android.os.Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ActivityFavorite) context).setBackGround();
                                    }
                                }, 250);
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        LinearLayout LinearLayoutFavorite;
        ImageView imgClose;

        public listViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            LinearLayoutFavorite = itemView.findViewById(R.id.LinearLayoutFavorite);
            imgClose = itemView.findViewById(R.id.deleteItem);
        }
    }
}
