package com.example.behnam.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

public class ViewDrug extends AppCompatActivity {
    List<Category> categoryList = new ArrayList<>();
    private ImageView btnBack;
    private DbHelper dbHelper;
    private HtmlTextView brand_value, healling_value, pharma_value, sickness_value, pregnancy_value, loctation_value, kids_value, senior_value,
            how_use_value, product_value, pharmacodynamic_value, usage_value, prohibition_value, caution_value, dose_value,
            complication_value, interferences_value, effect_value, over_dose_value, description_value, relation_food_value;
    private TextView name_drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drug);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        name_drug = findViewById(R.id.name_drug);
        brand_value = findViewById(R.id.brand_value);
        healling_value = findViewById(R.id.healling_value);
        pharma_value = findViewById(R.id.pharma_value);
        sickness_value = findViewById(R.id.sickness_value);
        pregnancy_value = findViewById(R.id.pregnancy_value);
        loctation_value = findViewById(R.id.loctation_value);
        kids_value = findViewById(R.id.kids_value);
        senior_value = findViewById(R.id.senior_value);
        how_use_value = findViewById(R.id.how_use_value);
        product_value = findViewById(R.id.product_value);
        pharmacodynamic_value = findViewById(R.id.pharmacodynamic_value);
        usage_value = findViewById(R.id.usage_value);
        prohibition_value = findViewById(R.id.prohibition_value);
        caution_value = findViewById(R.id.caution_value);
        dose_value = findViewById(R.id.dose_value);
        complication_value = findViewById(R.id.complication_value);
        interferences_value = findViewById(R.id.interferences_value);
        effect_value = findViewById(R.id.effect_value);
        over_dose_value = findViewById(R.id.over_dose_value);
        description_value = findViewById(R.id.description_value);
        relation_food_value = findViewById(R.id.relation_food_value);

        Bundle bundle = getIntent().getExtras();
        int ID = bundle.getInt("id");

        dbHelper = new DbHelper(this);
        Drug drug = dbHelper.getDrug(ID);


        categoryList = dbHelper.getCategories(ID);
        Log.e("TAG", "onCreate: "+categoryList.size());


        name_drug.setText(drug.getName());
        brand_value.setHtml(drug.getName());

        healling_value.setHtml(drug.getName());
        pharma_value.setHtml(drug.getName());
        sickness_value.setHtml(drug.getName());

        pregnancy_value.setHtml(drug.getPregnancy());
        loctation_value.setHtml(drug.getLactation());

        if (!drug.getKids().isEmpty()){
            kids_value.setEnabled(true);
            kids_value.setHtml(drug.getKids());
        }

        senior_value.setHtml(drug.getSeniors());
        how_use_value.setHtml(drug.getHow_to_use());
        product_value.setHtml(drug.getProduct());
        pharmacodynamic_value.setHtml(drug.getPharmacodynamic());
        usage_value.setHtml(drug.getUsage());
        prohibition_value.setHtml(drug.getProhibition());
        caution_value.setHtml(drug.getCaution());

        dose_value.setHtml(drug.getCaution());

        complication_value.setHtml(drug.getComplication());
        interferences_value.setHtml(drug.getInterference());

        effect_value.setHtml(drug.getInterference());

        over_dose_value.setHtml(drug.getOver_dose());
        description_value.setHtml(drug.getDescription());
        relation_food_value.setHtml(drug.getRelation_with_food());
    }
}
