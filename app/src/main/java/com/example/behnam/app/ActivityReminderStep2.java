package com.example.behnam.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.service.ReminderService;
import com.example.behnam.fonts.ButtonFont;
import com.example.behnam.fonts.EditTextFont;
import com.example.behnam.fonts.FontTextView;
import com.example.behnam.fonts.FontTextViewBold;
import com.example.behnam.reminder.ReminderModel;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;
import java.util.List;

import saman.zamani.persiandate.PersianDate;

public class ActivityReminderStep2 extends AppCompatActivity {
    PersianDate persianDate;
    EditText txtPeriod, txtCount;
    ButtonFont buttonRegister;
    DbHelper dbHelper;
    ImageView btnBack;
    Drug drug;
    long startTime = 0;
    TimePickerDialog tpd;
    int y = 0, m = 0, d = 0, h = 0, mm = 0;
    Context context;
    FontTextViewBold nameDrug;

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
        dbHelper = new DbHelper(this);
        txtPeriod = findViewById(R.id.et_period);
        txtCount = findViewById(R.id.et_count);
        nameDrug = findViewById(R.id.txtName);

        Intent intent = getIntent();
        final int drugId = intent.getIntExtra("id", 0);
        drug = dbHelper.getDrug(drugId);
        nameDrug.setText(drug.getName());
        final FontTextView date = findViewById(R.id.date_picker);
        final TextView time = findViewById(R.id.time_picker);
        time.setEnabled(true);
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
                                                                                             date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                                                                             time.setEnabled(true);
                                                                                         } else
                                                                                             Toast.makeText(ActivityReminderStep2.this, "تاریخ وارد شده اشتباه است.", Toast.LENGTH_SHORT).show();
                                                                                     }
                                                                                 }, now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay());
                now.getTimeInMillis();
                datePickerDialog.setThemeDark(false);
                datePickerDialog.show(getFragmentManager(), "tpd");
            }
        });

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
                                                           if (timeCalender.getTimeInMillis() < persianDate.getTime()) {
                                                               time.setText(String.valueOf(hourOfDay + ":" + minute));
                                                               time.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                                               startTime = persianDate.getTime();
                                                           } else
                                                               Toast.makeText(ActivityReminderStep2.this, "زمان وارد شده اشتباه است.", Toast.LENGTH_SHORT).show();
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

                txtPeriod.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!txtPeriod.getText().toString().equals(""))
                            txtPeriod.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        else
                            txtPeriod.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_atten), null, null, null);
                    }
                });
                txtCount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!txtCount.getText().toString().equals(""))
                            txtCount.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        else
                            txtCount.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_atten), null, null, null);
                    }
                });

                if (!txtCount.getText().toString().equals(""))
                    txtCount.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                if (!txtCount.getText().toString().equals("") && !txtPeriod.getText().toString().equals("") && !date.getText().equals("") && !time.getText().equals("")) {
                    int count = Integer.parseInt(txtCount.getText().toString()),
                            period = Integer.parseInt(txtPeriod.getText().toString());
                    dbHelper.addReminder(new ReminderModel(drugId, startTime, count, period, 0));

                    //max id
                    int id = dbHelper.getMaxID();

                    // Start service
                    Intent serviceIntent = new Intent(ActivityReminderStep2.this, ReminderService.class);
                    serviceIntent.putExtra("reminderID", id);
                    startService(serviceIntent);
                    ActivityReminderStep1.activityFinish.finish();
                    ActivityReminderList.activityFinish.finish();
                    startActivity(new Intent(ActivityReminderStep2.this, ActivityReminderList.class));
                    Toast.makeText(ActivityReminderStep2.this, "یادآور مورد نظر ثبت گردید.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (date.getText().equals("")) {
                        date.requestFocus();
                        date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_atten, 0);
                        Toast.makeText(ActivityReminderStep2.this, "تاریخ نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                    } else if (time.getText().equals("")) {
                        time.requestFocus();
                        Toast.makeText(ActivityReminderStep2.this, "زمان نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                        time.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_atten, 0, 0, 0);
                    } else if (txtPeriod.getText().toString().equals("")) {
                        txtPeriod.requestFocus();
                        Toast.makeText(ActivityReminderStep2.this, "دوره مصرف نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                        txtPeriod.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_atten), null, null, null);
                    } else if (txtCount.getText().toString().equals("")) {
                        txtCount.requestFocus();
                        Toast.makeText(ActivityReminderStep2.this, "تعداد مصرف نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                        txtCount.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_atten), null, null, null);
                    }
                }
            }
        });
    }
}