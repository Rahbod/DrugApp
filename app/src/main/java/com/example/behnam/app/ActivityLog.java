package com.example.behnam.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityLog extends AppCompatActivity {

    TextView txtActiv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        txtActiv = findViewById(R.id.txtActiv);
        txtActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLog.this, ActivityActiv.class);
                startActivity(intent);
            }
        });
    }
}
