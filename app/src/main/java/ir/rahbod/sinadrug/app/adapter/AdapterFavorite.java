package ir.rahbod.sinadrug.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.rahbod.sinadrug.app.ActivityFavorite;
import ir.rahbod.sinadrug.app.R;
import ir.rahbod.sinadrug.app.database.Drug;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.app.viewDrug;

import java.util.List;

import ir.rahbod.sinadrug.app.database.Drug;
import ir.rahbod.sinadrug.app.viewDrug;

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
        View view = LayoutInflater.from(context).inflate(ir.rahbod.sinadrug.app.R.layout.rec_favorite, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final listViewHolder holder, final int position) {
        holder.txtName.setText(list.get(position).getName());
        holder.LinearLayoutFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, viewDrug.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("vegetal", list.get(position).getVegetal());
                context.startActivity(intent);
            }
        });
        final View view = LayoutInflater.from(context).inflate(ir.rahbod.sinadrug.app.R.layout.massage_dialog, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        final Button btnOk = view.findViewById(ir.rahbod.sinadrug.app.R.id.btnOk);
        final Button btnCancel = view.findViewById(ir.rahbod.sinadrug.app.R.id.btnCancel);
        final TextView txt = view.findViewById(ir.rahbod.sinadrug.app.R.id.txt);
        LinearLayout linDialogMassage = view.findViewById(ir.rahbod.sinadrug.app.R.id.linDialogMassage);
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
            txtName = itemView.findViewById(ir.rahbod.sinadrug.app.R.id.txtName);
            LinearLayoutFavorite = itemView.findViewById(ir.rahbod.sinadrug.app.R.id.LinearLayoutFavorite);
            imgClose = itemView.findViewById(ir.rahbod.sinadrug.app.R.id.deleteItem);
        }
    }
}