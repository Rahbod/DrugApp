package com.example.behnam.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.service.ReminderService;
import com.example.behnam.fonts.ButtonFont;
import com.example.behnam.fonts.EditTextFont;
import com.example.behnam.fonts.FontTextView;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import saman.zamani.persiandate.PersianDate;

public class ActivityReminderStep2 extends AppCompatActivity {
    PersianDate persianDate;
    EditTextFont txtPeriod, txtCount;
    ButtonFont buttonRegister;
    DbHelper dbHelper;
    long timeStamps = 0;
    TimePickerDialog tpd;
    int y = 0, m = 0, d = 0, h = 0, mm = 0;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_step_2);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;
        dbHelper = new DbHelper(getApplicationContext());
        txtPeriod = findViewById(R.id.et_period);
        txtCount = findViewById(R.id.et_count);
        Intent intent = getIntent();
        final int drugId = intent.getIntExtra("id", 0);
        final FontTextView date = findViewById(R.id.date_picker);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianCalendar now = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                                     @Override
                                                                                     public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                                         persianDate = new PersianDate();
                                                                                         persianDate.setShYear(year);
                                                                                         persianDate.setShMonth(monthOfYear + 1);
                                                                                         persianDate.setShDay(dayOfMonth);
                                                                                         Calendar dateCalendar = Calendar.getInstance();
                                                                                         dateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                                                                         dateCalendar.set(Calendar.MINUTE, 0);
                                                                                         dateCalendar.set(Calendar.SECOND, 0);
                                                                                         dateCalendar.set(Calendar.MILLISECOND, 0);
                                                                                         if (dateCalendar.getTimeInMillis() < persianDate.getTime()) {
                                                                                             y = year;
                                                                                             m = monthOfYear + 1;
                                                                                             d = dayOfMonth;
                                                                                             date.setText(String.valueOf(y + "/" + m + "/" + d));
                                                                                         } else
                                                                                             Toast.makeText(ActivityReminderStep2.this, "تاریخ یا زمان وارد شده اشتباه است.", Toast.LENGTH_SHORT).show();
                                                                                     }
                                                                                 }, now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay());
                now.getTimeInMillis();
                datePickerDialog.setThemeDark(true);
                datePickerDialog.show(getFragmentManager(), "tpd");
            }
        });
        final TextView time = findViewById(R.id.time_picker);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianCalendar now = new PersianCalendar();
                tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                                       @Override
                                                       public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                                           tpd.setThemeDark(false);
                                                           h = hourOfDay;
                                                           mm = minute;
                                                           persianDate = new PersianDate();
                                                           persianDate.setShYear(y);
                                                           persianDate.setShMonth(m);
                                                           persianDate.setHour(h);
                                                           persianDate.setMinute(mm);
                                                           persianDate.setSecond(0);
                                                           Calendar timeCalender = Calendar.getInstance();
                                                           timeCalender.set(Calendar.SECOND, 0);
                                                           timeCalender.set(Calendar.MILLISECOND, 0);
                                                           Log.e("TAG", persianDate.getTime().toString() + "/////" + timeCalender.getTimeInMillis());
                                                           if (timeCalender.getTimeInMillis() < persianDate.getTime()) {
                                                               time.setText(String.valueOf(hourOfDay + ":" + minute));
                                                               timeStamps = persianDate.getTime();
                                                               Log.e("timestapm", "tttiii"+timeStamps );
                                                           } else
                                                               Toast.makeText(ActivityReminderStep2.this, "تاریخ یا زمان وارد شده اشتباه است.", Toast.LENGTH_SHORT).show();
                                                       }
                                                   },
                        now.get(PersianCalendar.HOUR_OF_DAY),
                        now.get(PersianCalendar.MINUTE)
                        , true);
                tpd.show(getFragmentManager(), "tpd");
            }
        });
        buttonRegister = findViewById(R.id.button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(txtCount.getText().toString()),
                        period = Integer.parseInt(txtPeriod.getText().toString());
                dbHelper.addReminder(new Reminder(drugId, timeStamps , count, period));

                //last id
                List<Reminder> reminderList =  dbHelper.getAllReminder();
                int id = reminderList.get(reminderList.size() - 1).getId();

                // Start service
                Intent serviceIntent = new Intent(ActivityReminderStep2.this, ReminderService.class);
                serviceIntent.putExtra("reminderID", id);
                Log.e("TAG", " drimdrim"+id );
                startService(serviceIntent);
            }
        });
    }

    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}