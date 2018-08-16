package com.example.behnam.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Reminder;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.map.MapActivity;
import com.example.behnam.app.service.ReminderService;
import com.example.behnam.fonts.ButtonFont;
import com.example.behnam.fonts.FontTextView;
import com.example.behnam.fonts.FontTextViewBold;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.File;
import java.util.Calendar;

import saman.zamani.persiandate.PersianDate;

public class ActivityReminderStep2 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private PersianDate persianDate;
    private EditText txtPeriod, txtCount;
    private DbHelper dbHelper;
    private long startTime = 0;
    private TimePickerDialog tpd;
    private int y = 0, m = 0, d = 0, h = 0, mm = 0;
    private Context context;

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
        FontTextViewBold nameDrug = findViewById(R.id.txtName);

        final int drugId = getIntent().getIntExtra("id", 0);
        Drug drug = dbHelper.getDrug(drugId);
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
                if (!(date.getText().toString().equals(""))) {
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
                } else {
                    Toast.makeText(ActivityReminderStep2.this, "لطفا ابتدا تاریخ مورد نظر را انتخاب کنید", Toast.LENGTH_LONG).show();
                }
            }
        });
        ButtonFont buttonRegister = findViewById(R.id.button);

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
                    dbHelper.addReminder(new Reminder(drugId, startTime, count, period, 0));

                    //max id
                    int id = dbHelper.getMaxID();
                    dbHelper.getReminder(id);

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

        drawerLayout = findViewById(R.id.DrawerLayout);
        ImageView imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
        imgOpenNvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                Class<? extends View.OnClickListener> view = this.getClass();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(txtPeriod.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(txtCount.getWindowToken(), 0);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }, 100);
            }
        });
    }

    public void openNv(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.item1:
                Intent goToListDrugInteractions = new Intent(this, ActivityListDrugInterference.class);
                startActivity(goToListDrugInteractions);
                closeNv();
                break;
            case R.id.item2:
                startActivity(new Intent(this, MapActivity.class));
                closeNv();
                break;
            case R.id.item3:
                ActivityReminderStep1.activityFinish.finish();
                ActivityReminderList.activityFinish.finish();
                finish();
                Intent goToReminder = new Intent(this, ActivityReminderList.class);
                startActivity(goToReminder);
                closeNv();
                break;
            case R.id.item4:
                Intent goToFavorite = new Intent(this, ActivityFavorite.class);
                startActivity(goToFavorite);
                closeNv();
                break;
            case R.id.item5:
                shareApplication();
                closeNv();
                break;
            case R.id.item6:
                Intent goToErrorReport = new Intent(this, ActivityErrorReport.class);
                startActivity(goToErrorReport);
                closeNv();
                break;
            case R.id.item7:
                Intent intentAbout = new Intent(this, ActivityAbout.class);
                intentAbout.putExtra("type", "about");
                startActivity(intentAbout);
                closeNv();
                break;
            case R.id.item8:
                Intent intentVegetalDrug = new Intent(this, ActivityDrug.class);
                startActivity(intentVegetalDrug);
                closeNv();
                break;
            case R.id.item9:
                Intent intentHome = new Intent(this, ActivityHome.class);
                startActivity(intentHome);
                closeNv();
                break;
            case R.id.item10:
                Intent intentRules = new Intent(this, ActivityAbout.class);
                intentRules.putExtra("type", "rules");
                startActivity(intentRules);
                closeNv();
                break;
            case R.id.item11:
                Intent intentPharma = new Intent(this, ActivityCategories.class);
                intentPharma.putExtra("type", 1);
                startActivity(intentPharma);
                closeNv();
                break;
            case R.id.item12:
                Intent intentHealing = new Intent(this, ActivityCategories.class);
                intentHealing.putExtra("type", 0);
                startActivity(intentHealing);
                closeNv();
                break;
        }
    }

    private void closeNv() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        }, 250);
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "Share app via"));
    }
}