package com.example.behnam.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityReminderStep2 extends AppCompatActivity {

    EditText etHour, etMinute;
    int Hour, Minute;
    TextView txtName, txtBrand;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_step_2);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String brand = bundle.getString("name");

        txtName = findViewById(R.id.txtName);
        txtBrand = findViewById(R.id.txtBrand);

        txtName.setText(name);
        txtBrand.setText(brand);

        etHour = findViewById(R.id.etHour);
        etMinute = findViewById(R.id.etMinute);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etMinute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etMinute.getText().toString().isEmpty()) {
                    Minute = Integer.parseInt(etMinute.getText().toString());
                    if (Minute > 59){
                        etMinute.setText("59");
                    }
                }
            }
        });

        etHour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!etHour.getText().toString().isEmpty()) {
                    Hour = Integer.parseInt(etHour.getText().toString());
                    if (Hour >= 24) {
                        etHour.setText("24");
                        etMinute.setText("00");
                        etMinute.setEnabled(false);
                    }
                }
            }
        });

        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etHour.getText().toString().isEmpty()) {
                    etMinute.setEnabled(true);
                    etMinute.setText("");
                }
            }
        });
    }
}