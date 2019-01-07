package ir.rahbod.sinadrug.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ir.rahbod.sinadrug.app.ActivityNotifications;
import ir.rahbod.sinadrug.app.R;
import ir.rahbod.sinadrug.app.database.Notifications;
import ir.rahbod.sinadrug.app.helper.DbHelper;

public class AdapterListNotifications extends RecyclerView.Adapter<AdapterListNotifications.listViewHolder> {

    private Context context;
    private List<Notifications> list;
    private DbHelper dbHelper;

    public AdapterListNotifications(Context context, List<Notifications> list) {
        this.context = context;
        this.list = list;
        dbHelper = new DbHelper(context);
    }

    @Override
    public AdapterListNotifications.listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rec_category, parent, false);
        return new AdapterListNotifications.listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterListNotifications.listViewHolder holder, final int position) {
        holder.txt.setText(list.get(position).getTitle());
        holder.relCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityNotifications.class);
                intent.putExtra("text", list.get(position).getMessage());
                dbHelper.readNotification(list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        RelativeLayout relCategory;
        public listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            relCategory = itemView.findViewById(R.id.relCategory);
        }
    }
}
