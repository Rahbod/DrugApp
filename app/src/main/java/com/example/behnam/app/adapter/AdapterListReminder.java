package com.example.behnam.app.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.behnam.app.R;
import com.example.behnam.app.ReminderListActivity;
import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.service.ReminderService;
import com.example.behnam.fonts.FontTextView;

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
        holder.drugName.setText(dbHelper.getDrug(reminderList.get(position).getDrugId()).getName());
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("آیا میخواهید یادآور داروی مورد نظر خود را حذف کنید؟ ").setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("TAG", "@$@$@$@$@$@$@$@$@$ " + reminderList.get(position).getId());
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent broadIntent = ((Activity) context).getIntent();
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderList.get(position).getId(), broadIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        Intent intent = new Intent(context, ReminderService.class);
                        intent.putExtra("close", true);
                        context.stopService(intent);
                        dbHelper.deleteReminder(reminderList.get(position).getId());
                        reminderList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, reminderList.size());
                        notifyDataSetChanged();

                        //context.stopService(new Intent(context,ReminderService.class));
                        //context.stopService(new Intent(context, ReminderService.class));
//                        Intent intent = new Intent(context, ReminderService.class);
//                        PendingIntent sender = PendingIntent.getBroadcast(context, reminderList.get(position).getId(), intent, 0);
//                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//                        alarmManager.cancel(sender);
                    }
                }).setNegativeButton("نه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class ListReminderViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteIcon;
        FontTextView drugName;

        ListReminderViewHolder(View itemView) {
            super(itemView);
            deleteIcon = itemView.findViewById(R.id.delete_reminder);
            drugName = itemView.findViewById(R.id.drug_name);
        }
    }
}