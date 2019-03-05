package com.rahbod.pharmasina.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rahbod.pharmasina.app.R;
import com.rahbod.pharmasina.app.ActivityReminderList;
import com.rahbod.pharmasina.app.database.Reminder;
import com.rahbod.pharmasina.app.helper.DbHelper;

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
        View view = LayoutInflater.from(context).inflate(com.rahbod.pharmasina.app.R.layout.reminder_list_items, parent, false);
        return new ListReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListReminderViewHolder holder, final int position) {
        holder.drugName.setText(dbHelper.getDrug(reminderList.get(position).getDrugID()).getName());
        final View view = LayoutInflater.from(context).inflate(com.rahbod.pharmasina.app.R.layout.massage_dialog, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = view.findViewById(com.rahbod.pharmasina.app.R.id.btnOk);
                Button btnCancel = view.findViewById(com.rahbod.pharmasina.app.R.id.btnCancel);
                final TextView txt = view.findViewById(com.rahbod.pharmasina.app.R.id.txt);
                LinearLayout linDialogMassage = view.findViewById(com.rahbod.pharmasina.app.R.id.linDialogMassage);
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
            deleteIcon = itemView.findViewById(com.rahbod.pharmasina.app.R.id.deleteItem);
            drugName = itemView.findViewById(com.rahbod.pharmasina.app.R.id.txtName);
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