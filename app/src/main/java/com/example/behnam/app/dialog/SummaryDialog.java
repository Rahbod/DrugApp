package com.example.behnam.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.adapter.AdapterSummaryDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SummaryDialog extends Dialog {

    public Context context;
    private String A;
    private RecyclerView recyclerView;
    public List<String> list = new ArrayList<>();
    private List<String> listChar = new ArrayList<>();

    public SummaryDialog(Context context, String a) {
        super(context);
        this.context = context;
        A = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.summary_dialog);

        ImageView imgCloseDialog = findViewById(R.id.imgCloseDialog);
        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView txtTitle = findViewById(R.id.txtTitle);

        if (A.equals("http://localhost/#pregnancy")) {
            txtTitle.setText("گروه های بارداری");
            pregnancyGroupsText();
        }

    }



    private void pregnancyGroupsText() {
        list.addAll(Arrays.asList(context.getResources().getStringArray(R.array.strPregnancyGroupsText)));

        listChar.addAll(Arrays.asList(context.getResources().getStringArray(R.array.str)));

        recyclerView = findViewById(R.id.recSummaryDialog);
        AdapterSummaryDialog adapter = new AdapterSummaryDialog(context, list, listChar);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
