package com.example.behnam.app.adapter;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.ActivityReminderList;
import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.service.BroadcastReceivers;
import com.example.behnam.app.service.ReminderService;
import com.google.android.gms.common.data.DataBuffer;

import java.util.List;

public class AdapterListReminder extends RecyclerView.Adapter<AdapterListReminder.ListReminderViewHolder> {
    private List<Reminder> reminderList;
    private Context context;
    private DbHelper dbHelper;

    public AdapterListReminder(List<Reminder> reminderList, Context context, DbHelper dbHelper) {
        this.reminderList = reminderList;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @Override
    public ListReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reminder_list_items, parent, false);
        return new ListReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListReminderViewHolder holder, final int position) {
        holder.drugName.setText(dbHelper.getDrug(reminderList.get(position).getDrugID()).getName());
        final View view = LayoutInflater.from(context).inflate(R.layout.massage_dialog, null);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(view);
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = view.findViewById(R.id.btnOk);
                Button btnCancel = view.findViewById(R.id.btnCancel);
                final TextView txt = view.findViewById(R.id.txt);
                LinearLayout linDialogMassage = view.findViewById(R.id.linDialogMassage);
                linDialogMassage.setVisibility(View.VISIBLE);
                txt.setText("آیا میخواهید یاد آور را حذف کنید؟");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        removeRemainder(position);
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class ListReminderViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteIcon;
        TextView drugName;

        ListReminderViewHolder(View itemView) {
            super(itemView);
            deleteIcon = itemView.findViewById(R.id.deleteItem);
            drugName = itemView.findViewById(R.id.txtName);
        }
    }

    private void removeRemainder(int position) {
        int ID = reminderList.get(position).getId();
        dbHelper.deleteReminder(ID);
        reminderList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, reminderList.size());
        notifyDataSetChanged();
        if (reminderList.isEmpty()) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ActivityReminderList) context).checkReminder();
                }
            }, 100);
        }
    }
}