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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.ClickableTableSpan;
import org.sufficientlysecure.htmltextview.DrawTableLinkSpan;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewDrug extends AppCompatActivity {
    private String pregnancyGroup[] = new String[6];
    String s = "";
    private String descriptionGroup[] = new String[28];
    private String heallingStr = "", pharmaStr = "", sicknessStr = "";
    private ImageView btnBack;
    List<Category> categoryList = new ArrayList<>();
    private DbHelper dbHelper;
    private HtmlTextView brand_value, healling_value, pharma_value, sickness_value, pregnancy_value,
            lactation_value, kids_value, senior_value, how_use_value, product_value,
            pharmacodynamic_value, usage_value, prohibition_value, caution_value, dose_value, complication_value,
            interferences_value, effect_value, over_dose_value, description_value, relation_food_value;
    private FontTextViewBold brand_title, healling_title, pharma_title, sickness_title, pregnancy_title, loctation_title,
            kids_title, senior_title, how_use_title, product_title, pharmacodynamic_title, usage_title, prohibition_title,
            dose_title, complication_title, interferences_title, description_title, caution_title, effect_title,
            over_dose_title, relation_food_title;
    private TextView name_drug;

    class ClickableTableSpanImpl extends ClickableTableSpan {
        @Override
        public ClickableTableSpan newInstance() {
            return new ClickableTableSpanImpl();
        }
        @Override
        public void onClick(View widget) {
            Bundle bundle = new Bundle();
            bundle.putString("table", getTableHtml());

            TableFragment viewFragment = new TableFragment();
            viewFragment.setArguments(bundle);
            viewFragment.show(getFragmentManager(),"Fragment");
        }
    }

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
        descriptionGroup[27] = "توضیحات 28";

        name_drug = findViewById(R.id.name_drug);
        brand_value = findViewById(R.id.brand_value);
        healling_value = findViewById(R.id.healling_value);
        pharma_value = findViewById(R.id.pharma_value);
        sickness_value = findViewById(R.id.sickness_value);
        pregnancy_value = findViewById(R.id.pregnancy_value);
        lactation_value = findViewById(R.id.loctation_value);
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
            pregnancy_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan pregTableLinkSpan = new DrawTableLinkSpan();
            pregTableLinkSpan.setTableLinkText("مشاهده جدول");
            pregnancy_value.setDrawTableLinkSpan(pregTableLinkSpan);
            pregTableLinkSpan.setTextColor(R.color.table_link);
            pregTableLinkSpan.setTextSize(20);

            pregnancy_value.setDrawTableLinkSpan(pregTableLinkSpan);
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
            kids_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan kidsTableLinkSpan = new DrawTableLinkSpan();
            kidsTableLinkSpan.setTableLinkText("مشاهده جدول");
            kids_value.setDrawTableLinkSpan(kidsTableLinkSpan);
            kidsTableLinkSpan.setTextColor(R.color.table_link);
            kidsTableLinkSpan.setTextSize(20);
            kids_title.setVisibility(View.VISIBLE);
            kids_value.setVisibility(View.VISIBLE);
            kids_value.setHtml(drug.getKids());
        }
        if (!drug.getSeniors().isEmpty()) {
            senior_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan seniorTableLinkSpan = new DrawTableLinkSpan();
            seniorTableLinkSpan.setTableLinkText("مشاهده جدول");
            senior_value.setDrawTableLinkSpan(seniorTableLinkSpan);
            seniorTableLinkSpan.setTextColor(R.color.table_link);
            seniorTableLinkSpan.setTextSize(20);
            senior_title.setVisibility(View.VISIBLE);
            senior_value.setVisibility(View.VISIBLE);
            senior_value.setHtml(drug.getSeniors());
        }
        if (!drug.getHow_to_use().isEmpty()) {
            how_use_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan how_useTableLinkSpan = new DrawTableLinkSpan();
            how_useTableLinkSpan.setTableLinkText("مشاهده جدول");
            how_use_value.setDrawTableLinkSpan(how_useTableLinkSpan);
            how_useTableLinkSpan.setTextColor(R.color.table_link);
            how_useTableLinkSpan.setTextSize(20);
            how_use_title.setVisibility(View.VISIBLE);
            how_use_value.setVisibility(View.VISIBLE);
            how_use_value.setHtml(drug.getHow_to_use());
        }
        if (!drug.getProduct().isEmpty()) {
            product_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan productTableLinkSpan = new DrawTableLinkSpan();
            productTableLinkSpan.setTableLinkText("مشاهده جدول");
            product_value.setDrawTableLinkSpan(productTableLinkSpan);
            productTableLinkSpan.setTextColor(R.color.table_link);
            productTableLinkSpan.setTextSize(20);
            product_title.setVisibility(View.VISIBLE);
            product_value.setVisibility(View.VISIBLE);
            product_value.setHtml(drug.getProduct());
        }
        if (!drug.getPharmacodynamic().isEmpty()) {
            pharmacodynamic_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan pharmacodynamicTableLinkSpan = new DrawTableLinkSpan();
            pharmacodynamicTableLinkSpan.setTableLinkText("مشاهده جدول");
            pharmacodynamic_value.setDrawTableLinkSpan(pharmacodynamicTableLinkSpan);
            pharmacodynamicTableLinkSpan.setTextColor(R.color.table_link);
            pharmacodynamicTableLinkSpan.setTextSize(20);
            pharmacodynamic_title.setVisibility(View.VISIBLE);
            pharmacodynamic_value.setVisibility(View.VISIBLE);
            pharmacodynamic_value.setHtml(drug.getPharmacodynamic());
        }
        if (!drug.getUsage().isEmpty()) {
            usage_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan usageTableLinkSpan = new DrawTableLinkSpan();
            usageTableLinkSpan.setTableLinkText("مشاهده جدول");
            usage_value.setDrawTableLinkSpan(usageTableLinkSpan);
            usageTableLinkSpan.setTextColor(R.color.table_link);
            usageTableLinkSpan.setTextSize(20);
            usage_title.setVisibility(View.VISIBLE);
            usage_value.setVisibility(View.VISIBLE);
            usage_value.setHtml(drug.getUsage());
        }
        if (!drug.getProhibition().isEmpty()) {
            prohibition_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan prohibitionTableLinkSpan = new DrawTableLinkSpan();
            prohibitionTableLinkSpan.setTableLinkText("مشاهده جدول");
            prohibition_value.setDrawTableLinkSpan(prohibitionTableLinkSpan);
            prohibitionTableLinkSpan.setTextColor(R.color.table_link);
            prohibitionTableLinkSpan.setTextSize(20);
            prohibition_title.setVisibility(View.VISIBLE);
            prohibition_value.setVisibility(View.VISIBLE);
            prohibition_value.setHtml(drug.getProhibition());
        }
        if (!drug.getLactation().isEmpty()) {
            lactation_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan lactationTableLinkSpan = new DrawTableLinkSpan();
            lactationTableLinkSpan.setTableLinkText("مشاهده جدول");
            lactation_value.setDrawTableLinkSpan(lactationTableLinkSpan);
            lactationTableLinkSpan.setTextColor(R.color.table_link);
            lactationTableLinkSpan.setTextSize(20);
            loctation_title.setVisibility(View.VISIBLE);
            lactation_value.setVisibility(View.VISIBLE);
            lactation_value.setHtml(drug.getLactation());
        }
        if (!drug.getCaution().isEmpty()) {
            caution_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan cautionTableLinkSpan = new DrawTableLinkSpan();
            cautionTableLinkSpan.setTableLinkText("مشاهده جدول");
            caution_value.setDrawTableLinkSpan(cautionTableLinkSpan);
            cautionTableLinkSpan.setTextColor(R.color.table_link);
            cautionTableLinkSpan.setTextSize(20);
            caution_title.setVisibility(View.VISIBLE);
            caution_value.setVisibility(View.VISIBLE);
            caution_value.setHtml(drug.getCaution());
        }
        if (!drug.getDose_adjustment().isEmpty()) {
            dose_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan doseTableLinkSpan = new DrawTableLinkSpan();
            doseTableLinkSpan.setTableLinkText("مشاهده جدول");
            dose_value.setDrawTableLinkSpan(doseTableLinkSpan);
            doseTableLinkSpan.setTextColor(R.color.table_link);
            doseTableLinkSpan.setTextSize(20);
            dose_title.setVisibility(View.VISIBLE);
            dose_value.setVisibility(View.VISIBLE);
            dose_value.setHtml(drug.getDose_adjustment());
        }
        if (!drug.getComplication().isEmpty()) {
            complication_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan complicationTableLinkSpan = new DrawTableLinkSpan();
            complicationTableLinkSpan.setTableLinkText("مشاهده جدول");
            complication_value.setDrawTableLinkSpan(complicationTableLinkSpan);
            complicationTableLinkSpan.setTextColor(R.color.table_link);
            complicationTableLinkSpan.setTextSize(20);
            complication_title.setVisibility(View.VISIBLE);
            complication_value.setVisibility(View.VISIBLE);
            complication_value.setHtml(drug.getComplication());
        }
        if (!drug.getInterference().isEmpty()) {
            interferences_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan interferencesTableLinkSpan = new DrawTableLinkSpan();
            interferencesTableLinkSpan.setTableLinkText("مشاهده جدول");
            interferences_value.setDrawTableLinkSpan(interferencesTableLinkSpan);
            interferencesTableLinkSpan.setTextColor(R.color.table_link);
            interferencesTableLinkSpan.setTextSize(20);
            interferences_title.setVisibility(View.VISIBLE);
            interferences_value.setVisibility(View.VISIBLE);
            interferences_value.setHtml(drug.getInterference());
        }
        if (!drug.getEffect_on_test().isEmpty()) {
            effect_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan effectTableLinkSpan = new DrawTableLinkSpan();
            effectTableLinkSpan.setTableLinkText("مشاهده جدول");
            effect_value.setDrawTableLinkSpan(effectTableLinkSpan);
            effectTableLinkSpan.setTextColor(R.color.table_link);
            effectTableLinkSpan.setTextSize(20);
            effect_title.setVisibility(View.VISIBLE);
            effect_value.setVisibility(View.VISIBLE);
            effect_value.setHtml(drug.getEffect_on_test());
        }
        if (!drug.getOver_dose().isEmpty()) {
            over_dose_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan over_doseTableLinkSpan = new DrawTableLinkSpan();
            over_doseTableLinkSpan.setTableLinkText("مشاهده جدول");
            over_dose_value.setDrawTableLinkSpan(over_doseTableLinkSpan);
            over_doseTableLinkSpan.setTextColor(R.color.table_link);
            over_doseTableLinkSpan.setTextSize(20);
            over_dose_title.setVisibility(View.VISIBLE);
            over_dose_value.setVisibility(View.VISIBLE);
            over_dose_value.setHtml(drug.getOver_dose());
        }

        try {
            JSONObject jsonObject = new JSONObject(drug.getDescription());
            JSONArray jsonArray = jsonObject.getJSONArray("code");
            String codeDescription = jsonObject.getString("code");
            String textDescription = jsonObject.getString("text");
            if (!codeDescription.isEmpty() || !textDescription.isEmpty()) {
                description_value.setClickableTableSpan(new ClickableTableSpanImpl());
                DrawTableLinkSpan descriptionTableLinkSpan = new DrawTableLinkSpan();
                descriptionTableLinkSpan.setTableLinkText("مشاهده جدول");
                description_value.setDrawTableLinkSpan(descriptionTableLinkSpan);
                descriptionTableLinkSpan.setTextColor(R.color.table_link);
                descriptionTableLinkSpan.setTextSize(20);
                description_title.setVisibility(View.VISIBLE);
                description_value.setVisibility(View.VISIBLE);
                if(codeDescription.isEmpty())
                {
                    description_value.setHtml(textDescription);
                }
                else
                {
                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        String codes = jsonArray.getString(i);
                        s += descriptionGroup[Integer.parseInt(codes)-1]+ "<br>";
                    }
                    description_value.setHtml(s+"<br>"+textDescription);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!drug.getRelation_with_food().isEmpty()) {
            relation_food_value.setClickableTableSpan(new ClickableTableSpanImpl());
            DrawTableLinkSpan relation_foodTableLinkSpan = new DrawTableLinkSpan();
            relation_foodTableLinkSpan.setTableLinkText("مشاهده جدول");
            relation_food_value.setDrawTableLinkSpan(relation_foodTableLinkSpan);
            relation_foodTableLinkSpan.setTextColor(R.color.table_link);
            relation_foodTableLinkSpan.setTextSize(20);
            relation_food_title.setVisibility(View.VISIBLE);
            relation_food_value.setVisibility(View.VISIBLE);
            relation_food_value.setHtml(drug.getRelation_with_food());
        }
    }
}