package ir.rahbod.sinadrug.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
        Typeface face = null;
        if(list.get(position).getRead() == 0){
            face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans(FaNum)_Bold.ttf");
            holder.relCategory.setBackgroundColor(context.getResources().getColor(R.color.unreadNotification));
        }else {
            face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans(FaNum)_Light.ttf");
            holder.relCategory.setBackground(context.getResources().getDrawable(R.drawable.select_recycler_view_item));
        }
        holder.txt.setTypeface(face);
        holder.txtDate.setTypeface(face);
        holder.txt.setText(list.get(position).getTitle());
        holder.txtDate.setText(list.get(position).getJalaliDate());
        holder.relCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityNotifications.class);
                intent.putExtra("text", list.get(position).getMessage());
                context.startActivity(intent);

                dbHelper.readNotification(list.get(position).getId());

                Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans(FaNum)_Light.ttf");
                holder.txt.setTypeface(face);
                holder.txtDate.setTypeface(face);
                holder.relCategory.setBackground(context.getResources().getDrawable(R.drawable.select_recycler_view_item));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder {
        TextView txt, txtDate;
        RelativeLayout relCategory;
        public listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            txtDate = itemView.findViewById(R.id.txtDate);
            relCategory = itemView.findViewById(R.id.relCategory);

            txtDate.setVisibility(View.VISIBLE);
        }
    }
}
