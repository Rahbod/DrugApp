package com.example.behnam.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.fonts.ButtonFont;
import com.example.behnam.fonts.FontTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.ClickableTableSpan;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewDrug extends AppCompatActivity {
    private String pregnancyGroup[] = new String[6];
    String s = "";
    WebView webView;
    String strPregnancy = "";
    private String descriptionGroup[] = new String[28];
    private String heallingStr = "", pharmaStr = "", sicknessStr = "";
    private ImageView btnBack;
    List<Category> categoryList = new ArrayList<>();
    private HtmlTextView brandValue, heallingValue, pharmaValue, sicknessValue, pregnancyValue,
            lactationValue, kidsValue, seniorValue, howUseValue, productValue,
            pharmacodynamicValue, usageValue, prohibitionValue, cautionValue, doseValue, complicationValue,
            interferencesValue, effectValue, overDoseValue, descriptionValue, relationFoodValue;
    private FontTextViewBold brandTitle, heallingTitle, pharmaTitle, sicknessTitle, pregnancyTitle, lactationTitle,
            kidsTitle, seniorTitle, howUseTitle, productTitle, pharmacodynamicTitle, usageTitle, prohibitionTitle,
            doseTitle, complicationTitle, interferencesTitle, descriptionTitle, cautionTitle, effectTitle,
            overDoseTitle, relationFoodTitle;
    private TextView nameDrug;
    private ButtonFont btnFavorite;

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
            viewFragment.show(getFragmentManager(), "Fragment");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drug);

        webView = findViewById(R.id.web_view_drug);

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

        Bundle bundle = getIntent().getExtras();
        final int ID = bundle.getInt("id");

        final DbHelper dbHelper = new DbHelper(this);
        Drug drug = dbHelper.getDrug(ID);

        categoryList = dbHelper.getCategories(ID);
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getType() == 0 && !categoryList.get(i).getName().isEmpty()) {
                heallingStr += "," + categoryList.get(i).getName();
                heallingStr = heallingStr.startsWith(",") ? heallingStr.substring(1) : heallingStr;
            }
            if (categoryList.get(i).getType() == 1) {
                pharmaStr += "," + categoryList.get(i).getName();
                pharmaStr = pharmaStr.startsWith(",") ? pharmaStr.substring(1) : pharmaStr;
            }
            if (categoryList.get(i).getType() == 2) {
                sicknessStr += "," + categoryList.get(i).getName();
                sicknessStr = sicknessStr.startsWith(",") ? sicknessStr.substring(1) : sicknessStr;
            }
        }

        try {
            JSONObject jsonPregnancy = new JSONObject(drug.getPregnancy());
            String groupPregnancy = jsonPregnancy.getString("group");
            String textPregnancy = jsonPregnancy.getString("text");
            groupPregnancy = groupPregnancy.trim();
            if (!groupPregnancy.isEmpty()) {
                strPregnancy = "";
                if (groupPregnancy.contains("A"))
                    strPregnancy += pregnancyGroup[0] + " <br> ";

                if (groupPregnancy.contains("B"))
                    strPregnancy += pregnancyGroup[1] + " <br> ";

                if (groupPregnancy.contains("C"))
                    strPregnancy += pregnancyGroup[2] + " <br> ";

                if (groupPregnancy.contains("D"))
                    strPregnancy += pregnancyGroup[3] + " <br> ";

                if (groupPregnancy.contains("X"))
                    strPregnancy += pregnancyGroup[4] + " <br> ";

                if (groupPregnancy.contains("NR"))
                    strPregnancy += pregnancyGroup[5] + " <br> ";
                strPregnancy += textPregnancy;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String webViewHtml = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />";
        if (!drug.getBrand().isEmpty())
            webViewHtml += "<h5>نام تجاری</h5><br>" + drug.getBrand() + "<br>";
        if (!heallingStr.isEmpty())
            webViewHtml += "<h5>گروه درمانی</h5><br>" + heallingStr + "<br>";
        if (!pharmaStr.isEmpty())
            webViewHtml += "<h5>گروه فارماکولوژیک</h5><br>" + pharmaStr + "<br>";
        if (!sicknessStr.isEmpty())
            webViewHtml += "<h5>گروه بیماری</h5><br>" + sicknessStr + "<br>";
        if (!strPregnancy.isEmpty())
            webViewHtml += "<h5>مصرف در دوران بارداری</h5><br>" + strPregnancy + "<br>";
        if (!drug.getLactation().isEmpty())
            webViewHtml += "<h5>مصرف در دوران شیردهی</h5><br>" + drug.getLactation() + "<br>";
        if (!drug.getKids().isEmpty())
            webViewHtml += "<h5>راهنمای مصرف در کودکان</h5><br>" + drug.getKids() + "<br>";
        if (!drug.getSeniors().isEmpty())
            webViewHtml += "<h5>راهنمای مصرف در سالمندان</h5><br>" + drug.getSeniors() + "<br>";
        if (!drug.getHowToUse().isEmpty())
            webViewHtml += "<h5>راه مصرف</h5><br>" + drug.getHowToUse() + "<br>";
        if (!drug.getProduct().isEmpty())
            webViewHtml += "<h5>فراورده های دارویی</h5><br>" + drug.getProduct() + "<br>";
        if (!drug.getPharmacodynamic().isEmpty())
            webViewHtml += "<h5>فارماکودینامیک و فارماکوکینتیک</h5><br>" + drug.getPharmacodynamic() + "<br>";
        if (!drug.getUsage().isEmpty())
            webViewHtml += "<h5> موارد مصرف و دوزاژ </h5><br>" + drug.getUsage() + "<br>";
        if (!drug.getDoseAdjustment().isEmpty())
            webViewHtml += "<h5>تعدیل دوزاژ</h5><br>" + drug.getDoseAdjustment() + "<br>";
        if (!drug.getProhibition().isEmpty())
            webViewHtml += "<h5>موارد منع مصرف</h5><br>" + drug.getProhibition() + "<br>";
        if (!drug.getCaution().isEmpty())
            webViewHtml += "<h5>موارد احتیاط</h5><br>" + drug.getCaution() + "<br>";
        if (!drug.getComplication().isEmpty())
            webViewHtml += "<h5>عوارض جانبی</h5><br>" + drug.getComplication() + "<br>";
        if (!drug.getEffectOnTest().isEmpty())
            webViewHtml += "<h5>اثر بر تست های آزمایشگاهی</h5><br>" + drug.getEffectOnTest() + "<br>";
        if (!drug.getOverDose().isEmpty())
            webViewHtml += "<h5>اوردوز و درمان</h5><br>" + drug.getOverDose() + "<br>";
        if (!drug.getDescription().isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(drug.getDescription());
                JSONArray jsonArray = jsonObject.getJSONArray("code");
                String codeDescription = jsonObject.getString("code");
                String textDescription = jsonObject.getString("text");
                if (!codeDescription.isEmpty() || !textDescription.isEmpty()) {
                    if (codeDescription.isEmpty()) {
                        webViewHtml += "<h5>اوردوز و درمان/h5><br>" + textDescription + "<br>";
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String codes = jsonArray.getString(i);
                            s += descriptionGroup[Integer.parseInt(codes) - 1] + "<br>";
                        }
                        webViewHtml += "<h5>اوردوز و درمان/h5><br>" + s + "<br>" + textDescription + "<br>";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!drug.getRelationWithFood().isEmpty())
            webViewHtml += "<h5>رابطه با غذا/h5><br>" + drug.getRelationWithFood() + "<br>";
        webViewHtml += "</body></html>";
        Log.e("TAG", "oooo" + webViewHtml);
        webView.loadDataWithBaseURL("file:///android_asset/", webViewHtml, "text/html", "UTF-8", null);
//        btnBack = findViewById(R.id.btnBack);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//
//
//
//
//        btnFavorite = findViewById(R.id.send);
//        nameDrug = findViewById(R.id.name_drug);
//        brandValue = findViewById(R.id.brand_value);
//        heallingValue = findViewById(R.id.healling_value);
//        pharmaValue = findViewById(R.id.pharma_value);
//        sicknessValue = findViewById(R.id.sickness_value);
//        pregnancyValue = findViewById(R.id.pregnancy_value);
//        lactationValue = findViewById(R.id.loctation_value);
//        kidsValue = findViewById(R.id.kids_value);
//        seniorValue = findViewById(R.id.senior_value);
//        howUseValue = findViewById(R.id.how_use_value);
//        productValue = findViewById(R.id.product_value);
//        pharmacodynamicValue = findViewById(R.id.pharmacodynamic_value);
//        usageValue = findViewById(R.id.usage_value);
//        prohibitionValue = findViewById(R.id.prohibition_value);
//        cautionValue = findViewById(R.id.caution_value);
//        doseValue = findViewById(R.id.dose_value);
//        complicationValue = findViewById(R.id.complication_value);
//        interferencesValue = findViewById(R.id.interferences_value);
//        effectValue = findViewById(R.id.effect_value);
//        overDoseValue = findViewById(R.id.over_dose_value);
//        descriptionValue = findViewById(R.id.description_value);
//        relationFoodValue = findViewById(R.id.relation_food_value);
//        brandTitle = findViewById(R.id.brand_name);
//        heallingTitle = findViewById(R.id.healling);
//        pharmaTitle = findViewById(R.id.pharma);
//        sicknessTitle = findViewById(R.id.sickness);
//        pregnancyTitle = findViewById(R.id.pregnancy);
//        lactationTitle = findViewById(R.id.loctation);
//        kidsTitle = findViewById(R.id.kids);
//        seniorTitle = findViewById(R.id.senior);
//        howUseTitle = findViewById(R.id.how_use);
//        productTitle = findViewById(R.id.product);
//        pharmacodynamicTitle = findViewById(R.id.pharmacodynamic);
//        usageTitle = findViewById(R.id.usage);
//        prohibitionTitle = findViewById(R.id.prohibition);
//        cautionTitle = findViewById(R.id.caution);
//        doseTitle = findViewById(R.id.dose);
//        complicationTitle = findViewById(R.id.complication);
//        interferencesTitle = findViewById(R.id.interferences);
//        effectTitle = findViewById(R.id.effect);
//        overDoseTitle = findViewById(R.id.over_dose);
//        descriptionTitle = findViewById(R.id.description);
//        relationFoodTitle = findViewById(R.id.relation_food);
//
//
//
//        //check exists to favorite and set text button
//        final Button btnAdd = findViewById(R.id.add);
//        if (dbHelper.checkFavorite(ID)) {
//            btnAdd.setBackgroundResource(R.drawable.background_button_selected);
//            btnAdd.setText("حذف از سبد دارو");
//            btnAdd.setTextColor(getResources().getColor(R.color.white));
//        } else{
//            btnAdd.setBackgroundResource(R.drawable.background_button);
//            btnAdd.setText("اضافه به سبد دارو");
//            btnAdd.setTextColor(getResources().getColor(R.color.blue2));
//        }
//
//
//        //add to favorite
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!dbHelper.checkFavorite(ID)) {
//                    dbHelper.bookMark(ID);
//                    btnAdd.setText("حذف از سبد دارو");
//                    btnAdd.setBackgroundResource(R.drawable.background_button_selected);
//                    btnAdd.setTextColor(getResources().getColor(R.color.white));
//                } else {
//                    dbHelper.bookMark(ID);
//                    btnAdd.setText("اضافه به سبد دارو");
//                    btnAdd.setBackgroundResource(R.drawable.background_button);
//                    btnAdd.setTextColor(getResources().getColor(R.color.blue2));
//                }
//
//            }
//
//        });
//
//        nameDrug.setText(drug.getName());
//
//        if (!drug.getBrand().isEmpty()) {
//            brandTitle.setVisibility(View.VISIBLE);
//            brandValue.setVisibility(View.VISIBLE);
//            brandValue.setHtml(drug.getBrand());
//        }
//
//        categoryList = dbHelper.getCategories(ID);
//        for (int i = 0; i < categoryList.size(); i++) {
//            if (categoryList.get(i).getType() == 0 && !categoryList.get(i).getName().isEmpty()) {
//                heallingTitle.setVisibility(View.VISIBLE);
//                heallingValue.setVisibility(View.VISIBLE);
//                heallingStr += "," + categoryList.get(i).getName();
//                heallingStr = heallingStr.startsWith(",") ? heallingStr.substring(1) : heallingStr;
//                heallingValue.setHtml(heallingStr);
//            }
//            if (categoryList.get(i).getType() == 1) {
//                pharmaTitle.setVisibility(View.VISIBLE);
//                pharmaValue.setVisibility(View.VISIBLE);
//                pharmaStr += "," + categoryList.get(i).getName();
//                pharmaStr = pharmaStr.startsWith(",") ? pharmaStr.substring(1) : pharmaStr;
//                pharmaValue.setHtml(pharmaStr);
//            }
//            if (categoryList.get(i).getType() == 2) {
//                sicknessTitle.setVisibility(View.VISIBLE);
//                sicknessValue.setVisibility(View.VISIBLE);
//                sicknessStr += "," + categoryList.get(i).getName();
//                sicknessStr = sicknessStr.startsWith(",") ? sicknessStr.substring(1) : sicknessStr;
//                sicknessValue.setHtml(sicknessStr);
//            }
//        }
//
//        try {
//            JSONObject jsonPregnancy = new JSONObject(drug.getPregnancy());
//            String groupPregnancy = jsonPregnancy.getString("group");
//            String textPregnancy = jsonPregnancy.getString("text");
//            groupPregnancy = groupPregnancy.trim();
//            pregnancyValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan pregTableLinkSpan = new DrawTableLinkSpan();
//            pregTableLinkSpan.setTableLinkText("مشاهده جدول");
//            pregnancyValue.setDrawTableLinkSpan(pregTableLinkSpan);
//            pregTableLinkSpan.setTextColor(R.color.table_link);
//            pregTableLinkSpan.setTextSize(20);
//
//            pregnancyValue.setDrawTableLinkSpan(pregTableLinkSpan);
//            if (!groupPregnancy.isEmpty()) {
//                pregnancyTitle.setVisibility(View.VISIBLE);
//                pregnancyValue.setVisibility(View.VISIBLE);
//                String strPregnancy = "";
//                if (groupPregnancy.contains("A"))
//                    strPregnancy += pregnancyGroup[0] + " <br> ";
//
//                if (groupPregnancy.contains("B"))
//                    strPregnancy += pregnancyGroup[1] + " <br> ";
//
//                if (groupPregnancy.contains("C"))
//                    strPregnancy += pregnancyGroup[2] + " <br> ";
//
//                if (groupPregnancy.contains("D"))
//                    strPregnancy += pregnancyGroup[3] + " <br> ";
//
//                if (groupPregnancy.contains("X"))
//                    strPregnancy += pregnancyGroup[4] + " <br> ";
//
//                if (groupPregnancy.contains("NR"))
//                    strPregnancy += pregnancyGroup[5] + " <br> ";
//                pregnancyValue.setHtml(strPregnancy + textPregnancy);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (!drug.getKids().isEmpty()) {
//            kidsValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan kidsTableLinkSpan = new DrawTableLinkSpan();
//            kidsTableLinkSpan.setTableLinkText("مشاهده جدول");
//            kidsValue.setDrawTableLinkSpan(kidsTableLinkSpan);
//            kidsTableLinkSpan.setTextColor(R.color.table_link);
//            kidsTableLinkSpan.setTextSize(20);
//            kidsTitle.setVisibility(View.VISIBLE);
//            kidsValue.setVisibility(View.VISIBLE);
//            kidsValue.setHtml(drug.getKids());
//        }
//        if (!drug.getSeniors().isEmpty()) {
//            seniorValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan seniorTableLinkSpan = new DrawTableLinkSpan();
//            seniorTableLinkSpan.setTableLinkText("مشاهده جدول");
//            seniorValue.setDrawTableLinkSpan(seniorTableLinkSpan);
//            seniorTableLinkSpan.setTextColor(R.color.table_link);
//            seniorTableLinkSpan.setTextSize(20);
//            seniorTitle.setVisibility(View.VISIBLE);
//            seniorValue.setVisibility(View.VISIBLE);
//            seniorValue.setHtml(drug.getSeniors());
//        }
//        if (!drug.getHowToUse().isEmpty()) {
//            howUseValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan how_useTableLinkSpan = new DrawTableLinkSpan();
//            how_useTableLinkSpan.setTableLinkText("مشاهده جدول");
//            howUseValue.setDrawTableLinkSpan(how_useTableLinkSpan);
//            how_useTableLinkSpan.setTextColor(R.color.table_link);
//            how_useTableLinkSpan.setTextSize(20);
//            howUseTitle.setVisibility(View.VISIBLE);
//            howUseValue.setVisibility(View.VISIBLE);
//            howUseValue.setHtml(drug.getHowToUse());
//        }
//        if (!drug.getProduct().isEmpty()) {
//            productValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan productTableLinkSpan = new DrawTableLinkSpan();
//            productTableLinkSpan.setTableLinkText("مشاهده جدول");
//            productValue.setDrawTableLinkSpan(productTableLinkSpan);
//            productTableLinkSpan.setTextColor(R.color.table_link);
//            productTableLinkSpan.setTextSize(20);
//            productTitle.setVisibility(View.VISIBLE);
//            productValue.setVisibility(View.VISIBLE);
//            productValue.setHtml(drug.getProduct());
//        }
//        if (!drug.getPharmacodynamic().isEmpty()) {
//            pharmacodynamicValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan pharmacodynamicTableLinkSpan = new DrawTableLinkSpan();
//            pharmacodynamicTableLinkSpan.setTableLinkText("مشاهده جدول");
//            pharmacodynamicValue.setDrawTableLinkSpan(pharmacodynamicTableLinkSpan);
//            pharmacodynamicTableLinkSpan.setTextColor(R.color.table_link);
//            pharmacodynamicTableLinkSpan.setTextSize(20);
//            pharmacodynamicTitle.setVisibility(View.VISIBLE);
//            pharmacodynamicValue.setVisibility(View.VISIBLE);
//            pharmacodynamicValue.setHtml(drug.getPharmacodynamic());
//        }
//        if (!drug.getUsage().isEmpty()) {
//            usageValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan usageTableLinkSpan = new DrawTableLinkSpan();
//            usageTableLinkSpan.setTableLinkText("مشاهده جدول");
//            usageValue.setDrawTableLinkSpan(usageTableLinkSpan);
//            usageTableLinkSpan.setTextColor(R.color.table_link);
//            usageTableLinkSpan.setTextSize(20);
//            usageTitle.setVisibility(View.VISIBLE);
//            usageValue.setVisibility(View.VISIBLE);
//            usageValue.setHtml(drug.getUsage());
//        }
//        if (!drug.getProhibition().isEmpty()) {
//            prohibitionValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan prohibitionTableLinkSpan = new DrawTableLinkSpan();
//            prohibitionTableLinkSpan.setTableLinkText("مشاهده جدول");
//            prohibitionValue.setDrawTableLinkSpan(prohibitionTableLinkSpan);
//            prohibitionTableLinkSpan.setTextColor(R.color.table_link);
//            prohibitionTableLinkSpan.setTextSize(20);
//            prohibitionTitle.setVisibility(View.VISIBLE);
//            prohibitionValue.setVisibility(View.VISIBLE);
//            prohibitionValue.setHtml(drug.getProhibition());
//        }
//        if (!drug.getLactation().isEmpty()) {
//            lactationValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan lactationTableLinkSpan = new DrawTableLinkSpan();
//            lactationTableLinkSpan.setTableLinkText("مشاهده جدول");
//            lactationValue.setDrawTableLinkSpan(lactationTableLinkSpan);
//            lactationTableLinkSpan.setTextColor(R.color.table_link);
//            lactationTableLinkSpan.setTextSize(20);
//            lactationTitle.setVisibility(View.VISIBLE);
//            lactationValue.setVisibility(View.VISIBLE);
//            lactationValue.setHtml(drug.getLactation());
//        }
//        if (!drug.getCaution().isEmpty()) {
//            cautionValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan cautionTableLinkSpan = new DrawTableLinkSpan();
//            cautionTableLinkSpan.setTableLinkText("مشاهده جدول");
//            cautionValue.setDrawTableLinkSpan(cautionTableLinkSpan);
//            cautionTableLinkSpan.setTextColor(R.color.table_link);
//            cautionTableLinkSpan.setTextSize(20);
//            cautionTitle.setVisibility(View.VISIBLE);
//            cautionValue.setVisibility(View.VISIBLE);
//            cautionValue.setHtml(drug.getCaution());
//        }
//        if (!drug.getDoseAdjustment().isEmpty()) {
//            doseValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan doseTableLinkSpan = new DrawTableLinkSpan();
//            doseTableLinkSpan.setTableLinkText("مشاهده جدول");
//            doseValue.setDrawTableLinkSpan(doseTableLinkSpan);
//            doseTableLinkSpan.setTextColor(R.color.table_link);
//            doseTableLinkSpan.setTextSize(20);
//            doseTitle.setVisibility(View.VISIBLE);
//            doseValue.setVisibility(View.VISIBLE);
//            doseValue.setHtml(drug.getDoseAdjustment());
//        }
//        if (!drug.getComplication().isEmpty()) {
//            complicationValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan complicationTableLinkSpan = new DrawTableLinkSpan();
//            complicationTableLinkSpan.setTableLinkText("مشاهده جدول");
//            complicationValue.setDrawTableLinkSpan(complicationTableLinkSpan);
//            complicationTableLinkSpan.setTextColor(R.color.table_link);
//            complicationTableLinkSpan.setTextSize(20);
//            complicationTitle.setVisibility(View.VISIBLE);
//            complicationValue.setVisibility(View.VISIBLE);
//            complicationValue.setHtml(drug.getComplication());
//        }
//        if (!drug.getInterference().isEmpty()) {
//            interferencesValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan interferencesTableLinkSpan = new DrawTableLinkSpan();
//            interferencesTableLinkSpan.setTableLinkText("مشاهده جدول");
//            interferencesValue.setDrawTableLinkSpan(interferencesTableLinkSpan);
//            interferencesTableLinkSpan.setTextColor(R.color.table_link);
//            interferencesTableLinkSpan.setTextSize(20);
//            interferencesTitle.setVisibility(View.VISIBLE);
//            interferencesValue.setVisibility(View.VISIBLE);
//            interferencesValue.setHtml(drug.getInterference());
//        }
//        if (!drug.getEffectOnTest().isEmpty()) {
//            effectValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan effectTableLinkSpan = new DrawTableLinkSpan();
//            effectTableLinkSpan.setTableLinkText("مشاهده جدول");
//            effectValue.setDrawTableLinkSpan(effectTableLinkSpan);
//            effectTableLinkSpan.setTextColor(R.color.table_link);
//            effectTableLinkSpan.setTextSize(20);
//            effectTitle.setVisibility(View.VISIBLE);
//            effectValue.setVisibility(View.VISIBLE);
//            effectValue.setHtml(drug.getEffectOnTest());
//        }
//        if (!drug.getOverDose().isEmpty()) {
//            overDoseValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan over_doseTableLinkSpan = new DrawTableLinkSpan();
//            over_doseTableLinkSpan.setTableLinkText("مشاهده جدول");
//            overDoseValue.setDrawTableLinkSpan(over_doseTableLinkSpan);
//            over_doseTableLinkSpan.setTextColor(R.color.table_link);
//            over_doseTableLinkSpan.setTextSize(20);
//            overDoseTitle.setVisibility(View.VISIBLE);
//            overDoseValue.setVisibility(View.VISIBLE);
//            overDoseValue.setHtml(drug.getOverDose());
//        }
//
//        try {
//            JSONObject jsonObject = new JSONObject(drug.getDescription());
//            JSONArray jsonArray = jsonObject.getJSONArray("code");
//            String codeDescription = jsonObject.getString("code");
//            String textDescription = jsonObject.getString("text");
//            if (!codeDescription.isEmpty() || !textDescription.isEmpty()) {
//                descriptionValue.setClickableTableSpan(new ClickableTableSpanImpl());
//                DrawTableLinkSpan descriptionTableLinkSpan = new DrawTableLinkSpan();
//                descriptionTableLinkSpan.setTableLinkText("مشاهده جدول");
//                descriptionValue.setDrawTableLinkSpan(descriptionTableLinkSpan);
//                descriptionTableLinkSpan.setTextColor(R.color.table_link);
//                descriptionTableLinkSpan.setTextSize(20);
//                descriptionTitle.setVisibility(View.VISIBLE);
//                descriptionValue.setVisibility(View.VISIBLE);
//                if (codeDescription.isEmpty()) {
//                    descriptionValue.setHtml(textDescription);
//                } else {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        String codes = jsonArray.getString(i);
//                        s += descriptionGroup[Integer.parseInt(codes) - 1] + "<br>";
//                    }
//                    descriptionValue.setHtml(s + "<br>" + textDescription);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (!drug.getRelationWithFood().isEmpty()) {
//            relationFoodValue.setClickableTableSpan(new ClickableTableSpanImpl());
//            DrawTableLinkSpan relation_foodTableLinkSpan = new DrawTableLinkSpan();
//            relation_foodTableLinkSpan.setTableLinkText("مشاهده جدول");
//            relationFoodValue.setDrawTableLinkSpan(relation_foodTableLinkSpan);
//            relation_foodTableLinkSpan.setTextColor(R.color.table_link);
//            relation_foodTableLinkSpan.setTextSize(20);
//            relationFoodTitle.setVisibility(View.VISIBLE);
//            relationFoodValue.setVisibility(View.VISIBLE);
//            relationFoodValue.setHtml(drug.getRelationWithFood());
//        }
    }
}