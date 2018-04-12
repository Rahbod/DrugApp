package com.example.behnam.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.fonts.FontTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewDrug extends AppCompatActivity {
    private String pregnancyGroup[] = new String[6];
    private String descriptionGroup[] = new String[27];
    private String heallingStr = "", pharmaStr = "", sicknessStr = "";
    private ImageView btnBack;
    List<Category> categoryList = new ArrayList<>();
    private DbHelper dbHelper;
    private HtmlTextView brand_value, healling_value, pharma_value, sickness_value, pregnancy_value,
            loctation_value, kids_value, senior_value, how_use_value, product_value,
            pharmacodynamic_value, usage_value, prohibition_value, caution_value, dose_value, complication_value,
            interferences_value, effect_value, over_dose_value, description_value, relation_food_value;
    private FontTextViewBold brand_title, healling_title, pharma_title, sickness_title, pregnancy_title, loctation_title,
            kids_title, senior_title, how_use_title, product_title, pharmacodynamic_title, usage_title, prohibition_title,
            dose_title, complication_title, interferences_title, description_title, caution_title, effect_title,
            over_dose_title, relation_food_title;
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

        pregnancyGroup[0] = "توضیحات گروه a";
        pregnancyGroup[1] = "توضیحات گروه b";
        pregnancyGroup[2] = "توضیحات گروه c";
        pregnancyGroup[3] = "توضیحات گروه d";
        pregnancyGroup[4] = "توضیحات گروه x";
        pregnancyGroup[5] = "توضیحات گروه nr";

        descriptionGroup[0] = "توضیحات 1";
        descriptionGroup[1] = "توضیحات 2";
        descriptionGroup[2] = "توضیحات 3";
        descriptionGroup[3] = "توضیحات 4";
        descriptionGroup[4] = "توضیحات 5";
        descriptionGroup[5] = "توضیحات 6";
        descriptionGroup[6] = "توضیحات 7";
        descriptionGroup[7] = "توضیحات 8";
        descriptionGroup[8] = "توضیحات 9";
        descriptionGroup[9] = "توضیحات 10";
        descriptionGroup[10] = "توضیحات 11";
        descriptionGroup[11] = "توضیحات 12";
        descriptionGroup[12] = "توضیحات 13";
        descriptionGroup[13] = "توضیحات 14";
        descriptionGroup[14] = "توضیحات 15";
        descriptionGroup[15] = "توضیحات 16";
        descriptionGroup[16] = "توضیحات 17";
        descriptionGroup[17] = "توضیحات 18";
        descriptionGroup[18] = "توضیحات 19";
        descriptionGroup[19] = "توضیحات 20";
        descriptionGroup[20] = "توضیحات 21";
        descriptionGroup[21] = "توضیحات 22";
        descriptionGroup[22] = "توضیحات 23";
        descriptionGroup[23] = "توضیحات 24";
        descriptionGroup[24] = "توضیحات 25";
        descriptionGroup[25] = "توضیحات 26";
        descriptionGroup[26] = "توضیحات 27";
//        descriptionGroup[27] = "توضیحات 28";

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

        brand_title = findViewById(R.id.brand_name);
        healling_title = findViewById(R.id.healling);
        pharma_title = findViewById(R.id.pharma);
        sickness_title = findViewById(R.id.sickness);
        pregnancy_title = findViewById(R.id.pregnancy);
        loctation_title = findViewById(R.id.loctation);
        kids_title = findViewById(R.id.kids);
        senior_title = findViewById(R.id.senior);
        how_use_title = findViewById(R.id.how_use);
        product_title = findViewById(R.id.product);
        pharmacodynamic_title = findViewById(R.id.pharmacodynamic);
        usage_title = findViewById(R.id.usage);
        prohibition_title = findViewById(R.id.prohibition);
        caution_title = findViewById(R.id.caution);
        dose_title = findViewById(R.id.dose);
        complication_title = findViewById(R.id.complication);
        interferences_title = findViewById(R.id.interferences);
        effect_title = findViewById(R.id.effect);
        over_dose_title = findViewById(R.id.over_dose);
        description_title = findViewById(R.id.description);
        relation_food_title = findViewById(R.id.relation_food);

        Bundle bundle = getIntent().getExtras();
        int ID = bundle.getInt("id");

        dbHelper = new DbHelper(this);
        Drug drug = dbHelper.getDrug(ID);

        name_drug.setText(drug.getName());

        if (!drug.getBrand().isEmpty()) {
            brand_title.setVisibility(View.VISIBLE);
            brand_value.setVisibility(View.VISIBLE);
            brand_value.setHtml(drug.getBrand());
        }

        categoryList = dbHelper.getCategories(ID);
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getType() == 0 && !categoryList.get(i).getName().isEmpty()) {
                healling_title.setVisibility(View.VISIBLE);
                healling_value.setVisibility(View.VISIBLE);
                heallingStr += "," + categoryList.get(i).getName();
                heallingStr = heallingStr.startsWith(",") ? heallingStr.substring(1) : heallingStr;
                healling_value.setHtml(heallingStr);
            }
            if (categoryList.get(i).getType() == 1) {
                pharma_title.setVisibility(View.VISIBLE);
                pharma_value.setVisibility(View.VISIBLE);
                pharmaStr += "," + categoryList.get(i).getName();
                pharmaStr = pharmaStr.startsWith(",") ? pharmaStr.substring(1) : pharmaStr;
                pharma_value.setHtml(pharmaStr);
            }
            if (categoryList.get(i).getType() == 2) {
                sickness_title.setVisibility(View.VISIBLE);
                sickness_value.setVisibility(View.VISIBLE);
                sicknessStr += "," + categoryList.get(i).getName();
                sicknessStr = sicknessStr.startsWith(",") ? sicknessStr.substring(1) : sicknessStr;
                sickness_value.setHtml(sicknessStr);
            }
        }

        try {
            JSONObject jsonPregnancy = new JSONObject(drug.getPregnancy());
            String groupPregnancy = jsonPregnancy.getString("group");
            String textPregnancy = jsonPregnancy.getString("text");
            groupPregnancy=groupPregnancy.trim();
            if (!groupPregnancy.isEmpty()) {
                pregnancy_title.setVisibility(View.VISIBLE);
                pregnancy_value.setVisibility(View.VISIBLE);
                switch (groupPregnancy) {
                    case "A":
                        pregnancy_value.setHtml(pregnancyGroup[0] + " <br> " + textPregnancy);
                        break;
                    case "B":
                        pregnancy_value.setHtml(pregnancyGroup[1] + " <br> " + textPregnancy);
                        break;
                    case "C":
                        pregnancy_value.setHtml(pregnancyGroup[2] + " <br> " + textPregnancy);
                        break;
                    case "D":
                        pregnancy_value.setHtml(pregnancyGroup[3] + " <br> " + textPregnancy);
                        break;
                    case "X":
                        pregnancy_value.setHtml(pregnancyGroup[4] + " <br> " + textPregnancy);
                        break;
                    case "NR":
                        pregnancy_value.setHtml(pregnancyGroup[5] + " <br> " + textPregnancy);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!drug.getKids().isEmpty()) {
            kids_title.setVisibility(View.VISIBLE);
            kids_value.setVisibility(View.VISIBLE);
            kids_value.setHtml(drug.getKids());
        }
        if (!drug.getSeniors().isEmpty()) {
            senior_title.setVisibility(View.VISIBLE);
            senior_value.setVisibility(View.VISIBLE);
            senior_value.setHtml(drug.getSeniors());
        }
        if (!drug.getHow_to_use().isEmpty()) {
            how_use_title.setVisibility(View.VISIBLE);
            how_use_value.setVisibility(View.VISIBLE);
            how_use_value.setHtml(drug.getHow_to_use());
        }
        if (!drug.getProduct().isEmpty()) {
            product_title.setVisibility(View.VISIBLE);
            product_value.setVisibility(View.VISIBLE);
            product_value.setHtml(drug.getProduct());
        }
        if (!drug.getPharmacodynamic().isEmpty()) {
            pharmacodynamic_title.setVisibility(View.VISIBLE);
            pharmacodynamic_value.setVisibility(View.VISIBLE);
            pharmacodynamic_value.setHtml(drug.getPharmacodynamic());
        }
        if (!drug.getUsage().isEmpty()) {
            usage_title.setVisibility(View.VISIBLE);
            usage_value.setVisibility(View.VISIBLE);
            usage_value.setHtml(drug.getUsage());
        }
        if (!drug.getProhibition().isEmpty()) {
            prohibition_title.setVisibility(View.VISIBLE);
            prohibition_value.setVisibility(View.VISIBLE);
            prohibition_value.setHtml(drug.getProhibition());
        }
        if (!drug.getCaution().isEmpty()) {
            caution_title.setVisibility(View.VISIBLE);
            caution_value.setVisibility(View.VISIBLE);
            caution_value.setHtml(drug.getCaution());
        }
        if (!drug.getDose_adjustment().isEmpty()) {
            dose_title.setVisibility(View.VISIBLE);
            dose_value.setVisibility(View.VISIBLE);
            dose_value.setHtml(drug.getDose_adjustment());
        }
        if (!drug.getComplication().isEmpty()) {
            complication_title.setVisibility(View.VISIBLE);
            complication_value.setVisibility(View.VISIBLE);
            complication_value.setHtml(drug.getComplication());
        }
        if (!drug.getInterference().isEmpty()) {
            interferences_title.setVisibility(View.VISIBLE);
            interferences_value.setVisibility(View.VISIBLE);
            interferences_value.setHtml(drug.getInterference());
        }
        if (!drug.getEffect_on_test().isEmpty()) {
            effect_title.setVisibility(View.VISIBLE);
            effect_value.setVisibility(View.VISIBLE);
            effect_value.setHtml(drug.getEffect_on_test());
        }
        if (!drug.getOver_dose().isEmpty()) {
            over_dose_title.setVisibility(View.VISIBLE);
            over_dose_value.setVisibility(View.VISIBLE);
            over_dose_value.setHtml(drug.getOver_dose());
        }

        try {
            JSONObject jsonObject = new JSONObject(drug.getDescription());
            String codeDescription = jsonObject.getString("code");
            String textDescription = jsonObject.getString("text");
            if (!codeDescription.isEmpty()) {
                description_title.setVisibility(View.VISIBLE);
                description_value.setVisibility(View.VISIBLE);
                switch (codeDescription) {
                    case "1":
                        description_value.setHtml(descriptionGroup[0] + "<br>" + textDescription);
                        break;
                    case "2":
                        description_value.setHtml(descriptionGroup[1] + "<br>" + textDescription);
                        break;
                    case "3":
                        description_value.setHtml(descriptionGroup[2] + "<br>" + textDescription);
                        break;
                    case "4":
                        description_value.setHtml(descriptionGroup[3] + "<br>" + textDescription);
                        break;
                    case "5":
                        description_value.setHtml(descriptionGroup[4] + "<br>" + textDescription);
                        break;
                    case "6":
                        description_value.setHtml(descriptionGroup[5] + "<br>" + textDescription);
                        break;
                    case "7":
                        description_value.setHtml(descriptionGroup[6] + "<br>" + textDescription);
                        break;
                    case "8":
                        description_value.setHtml(descriptionGroup[7] + "<br>" + textDescription);
                        break;
                    case "9":
                        description_value.setHtml(descriptionGroup[8] + "<br>" + textDescription);
                        break;
                    case "10":
                        description_value.setHtml(descriptionGroup[9] + "<br>" + textDescription);
                        break;
                    case "11":
                        description_value.setHtml(descriptionGroup[10] + "<br>" + textDescription);
                        break;
                    case "12":
                        description_value.setHtml(descriptionGroup[11] + "<br>" + textDescription);
                        break;
                    case "13":
                        description_value.setHtml(descriptionGroup[12] + "<br>" + textDescription);
                        break;
                    case "14":
                        description_value.setHtml(descriptionGroup[13] + "<br>" + textDescription);
                        break;
                    case "15":
                        description_value.setHtml(descriptionGroup[14] + "<br>" + textDescription);
                        break;
                    case "16":
                        description_value.setHtml(descriptionGroup[15] + "<br>" + textDescription);
                        break;
                    case "17":
                        description_value.setHtml(descriptionGroup[16] + "<br>" + textDescription);
                        break;
                    case "18":
                        description_value.setHtml(descriptionGroup[17] + "<br>" + textDescription);
                        break;
                    case "19":
                        description_value.setHtml(descriptionGroup[18] + "<br>" + textDescription);
                        break;
                    case "20":
                        description_value.setHtml(descriptionGroup[19] + "<br>" + textDescription);
                        break;
                    case "21":
                        description_value.setHtml(descriptionGroup[20] + "<br>" + textDescription);
                        break;
                    case "22":
                        description_value.setHtml(descriptionGroup[21] + "<br>" + textDescription);
                        break;
                    case "23":
                        description_value.setHtml(descriptionGroup[22] + "<br>" + textDescription);
                        break;
                    case "24":
                        description_value.setHtml(descriptionGroup[23] + "<br>" + textDescription);
                        break;
                    case "25":
                        description_value.setHtml(descriptionGroup[24] + "<br>" + textDescription);
                        break;
                    case "26":
                        description_value.setHtml(descriptionGroup[25] + "<br>" + textDescription);
                        break;
                    case "27":
                        description_value.setHtml(descriptionGroup[26] + "<br>" + textDescription);
                        break;
//                        case "28":
//                            description_value.setHtml(descriptionGroup[27] + "<br>" + textDescription);
//                            break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!drug.getRelation_with_food().isEmpty()) {
            relation_food_title.setVisibility(View.VISIBLE);
            relation_food_value.setVisibility(View.VISIBLE);
            relation_food_value.setHtml(drug.getRelation_with_food());
        }
    }
}
