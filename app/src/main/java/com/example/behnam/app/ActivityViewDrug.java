package com.example.behnam.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Drug2;
import com.example.behnam.app.dialog.SummaryDialog;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.fonts.FontTextView;
import com.example.behnam.fonts.FontTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewDrug extends AppCompatActivity {
    private JSONObject pregnancyGroupsText = new JSONObject();
    String s = "";

    WebView webView;
    String strPregnancy = "";
    String groupPregnancyStr = "";
    String descriptionGroup[] = new String[28];
    String healingStr = "", pharmaStr = "", sicknessStr = "";
    ImageView btnBack;
    List<Category> categoryList = new ArrayList<>();
    FontTextViewBold nameDrug;
    FontTextView persianName;
    String strDescription;

    Animation animationToDown;
    Animation animationToUp;

    LinearLayout relativebottom;
    LinearLayout linearBasket;
    FontTextView addToBasketText;
    ImageView addToBasketImage;

    LinearLayout linearfriend;

    //@SuppressLint("WrongViewCast")
    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drug);

        webView = findViewById(R.id.web_view_drug);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        nameDrug = findViewById(R.id.name_drug);
        persianName = findViewById(R.id.persian_name);

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                SummaryDialog summaryDialog = new SummaryDialog(ActivityViewDrug.this, url);
                summaryDialog.show();
                return true;
            }

        });

        relativebottom = findViewById(R.id.relative_bottom);
        addToBasketText = findViewById(R.id.add_basket_item);
        addToBasketImage = findViewById(R.id.add_basket_ico);
        linearBasket = findViewById(R.id.layout_basket);
        linearfriend = findViewById(R.id.layout_friend);

        animationToUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationToUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                relativebottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        animationToDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animationToDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relativebottom.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        final int ID = getIntent().getIntExtra("id", 0);
        int VEGETAL = Integer.parseInt(getIntent().getStringExtra("vegetal"));

        if (VEGETAL == 1) {
            RelativeLayout rel = findViewById(R.id.header);
            rel.setBackgroundColor(getResources().getColor(R.color.greenVegetal));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.greenVegetalStatus));
            }
        }

        final DbHelper dbHelper = new DbHelper(this);
        final Drug list = dbHelper.getDrug(ID);

        nameDrug.setText(list.getName());
        persianName.setText(list.getFaName());

        //btnBack
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Get healing category
        List<Category> categoryList = dbHelper.getHealingCategory(ID);
        for (int i = 0; i < categoryList.size(); i++) {
            if (healingStr.isEmpty())
                healingStr = categoryList.get(i).getName();
            else
                healingStr += ", " + categoryList.get(i).getName();
        }

        //Get pharmacologic category
        categoryList = dbHelper.getPharmaCategory(ID);
        for (int i = 0; i < categoryList.size(); i++) {
            if (pharmaStr.isEmpty())
                pharmaStr = categoryList.get(i).getName();
            else
                pharmaStr += ", " + categoryList.get(i).getName();
        }

        //Get sickness category
        categoryList = dbHelper.getSicknessCategory(ID);
        for (int i = 0; i < categoryList.size(); i++) {
            if (sicknessStr.isEmpty())
                sicknessStr = categoryList.get(i).getName();
            else
                sicknessStr += ", " + categoryList.get(i).getName();
        }

        String webViewHtml = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /><link rel=\"stylesheet\" type=\"text/css\" href=\"fontiran.css\" />";
        if (VEGETAL == 1)
            webViewHtml += "<div class=\"container vegetal\">";
        else
            webViewHtml += "<div class=\"container\">";

        if (!list.getBrand().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">نام تجاری:</h4><div class=\"text direction-ltr\">" + list.getBrand() + "</div></div></div>";

        if (!healingStr.isEmpty() || !pharmaStr.isEmpty() || !sicknessStr.isEmpty()) {
            webViewHtml += "<div class=\"section\">";
            if (!healingStr.isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">طبقه بندی درمانی:</h4><div class=\"text\">" + healingStr + "</div></div>";
            if (!pharmaStr.isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">طبقه بندی فارماکولوژیک:</h4><div class=\"text\">" + pharmaStr + "</div></div>";
            if (!sicknessStr.isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">طبقه بندی بیماری:</h4><div class=\"text\">" + sicknessStr + "</div></div>";
            webViewHtml += "</div>";
        }

        JSONObject jsonPregnancy = null;
        JSONArray groupPregnancy = new JSONArray();
        String textPregnancy = "";
        try {
            jsonPregnancy = new JSONObject(list.getPregnancy());
            groupPregnancy = jsonPregnancy.getJSONArray("group");
            textPregnancy = jsonPregnancy.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ((groupPregnancy.length() != 0 || !textPregnancy.equals("") || !list.getLactation().isEmpty())) {
            webViewHtml += "<div class=\"section\">";
            if (groupPregnancy.length() != 0 || !textPregnancy.equals("")) {
                try {
                    if (groupPregnancy.length() != 0) {
                        strPregnancy += "<span>گروه ";
                        for (int i = 0; i < groupPregnancy.length(); i++) {
                            if (i != 0)
                                strPregnancy += " و ";

//                            strPregnancy += "<span style=\"font-weight:bold;color:#0334b0;\">گروه " + groupPregnancy.getString(i) + "</span> " + pregnancyGroupsText.get(groupPregnancy.getString(i));
                            strPregnancy += groupPregnancy.getString(i);
                        }
                        strPregnancy += "</span><a href=\"http://localhost/#pregnancy\" class=\"pregnancy-modal-trigger\"></a>";
                    }
                    if (!textPregnancy.equals(""))
                        strPregnancy += textPregnancy;
                    if (VEGETAL == 1)
                        webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">مصرف در دوران بارداری و شیردهی:</h4><div class=\"text\">" + groupPregnancyStr + strPregnancy + "</div></div>";
                    else
                        webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">مصرف در دوران بارداری:</h4><div class=\"text\">" + groupPregnancyStr + strPregnancy + "</div></div>";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (!list.getLactation().isEmpty() && VEGETAL == 0)
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">مصرف در دوران شیردهی:</h4><div class=\"text\">" + list.getLactation() + "</div></div>";
            webViewHtml += "</div>";
        }

        if ((!list.getKids().isEmpty() || !list.getSeniors().isEmpty() || !list.getHowToUse().isEmpty()) && VEGETAL == 0) {
            webViewHtml += "<div class=\"section\">";
            if (!list.getKids().isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">مصرف در کودکان:</h4><div class=\"text\">" + list.getKids() + "</div></div>";

            if (!list.getSeniors().isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">مصرف در سالمندان:</h4><div class=\"text\">" + list.getSeniors() + "</div></div>";

            if (!list.getHowToUse().isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">راه مصرف:</h4><div class=\"text\">" + list.getHowToUse() + "</div></div>";
            webViewHtml += "</div>";
        }

        if (VEGETAL == 1) {
            webViewHtml += "<div class=\"section\">";
            if (!list.getHowToUse().isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">راه مصرف:</h4><div class=\"text\">" + list.getHowToUse() + "</div></div>";
            webViewHtml += "</div>";
        }

        if (!list.getProduct().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>فرآورده های دارویی:</h4><div class=\"text direction-ltr\">" + list.getProduct() + "</div></div></div>";

        if (VEGETAL == 1) {
            if (!list.getCompounds().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>ترکیبات موجود:</h4><div class=\"text direction-ltr\">" + list.getCompounds() + "</div></div></div>";

            if (!list.getEffectiveIngredients().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>مواد موثره:</h4><div class=\"text direction-ltr\">" + list.getEffectiveIngredients() + "</div></div></div>";

            if (!list.getStandardized().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>استاندارد شده:</h4><div class=\"text direction-ltr\">" + list.getStandardized() + "</div></div></div>";
        }

        if (!list.getPharmacodynamic().isEmpty())
            if (VEGETAL == 1)
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-pharmacy\"></i>مکانیسم احتمالی اثر:</h4><div class=\"text\">" + list.getPharmacodynamic() + "</div></div></div>";
            else
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-pharmacy\"></i>فارماکودینامیک و فارماکوکینتیک:</h4><div class=\"text\">" + list.getPharmacodynamic() + "</div></div></div>";

        if (!list.getUsage().isEmpty())
            if (VEGETAL == 1)
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-usage\"></i>موارد مصرف:</h4><div class=\"text\">" + list.getUsage() + "</div></div></div>";
            else
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-usage\"></i>موارد مصرف و دوزاژ:</h4><div class=\"text\">" + list.getUsage() + "</div></div></div>";

        if (!list.getDoseAdjustment().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-dose-adjustment\"></i>تعدیل دوزاژ:</h4><div class=\"text\">" + list.getDoseAdjustment() + "</div></div></div>";

        if (!list.getProhibition().isEmpty())
            if (VEGETAL == 1)
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-prohibition\"></i>موارد منع مصرف و احتیاط:</h4><div class=\"text\">" + list.getProhibition() + "</div></div></div>";
            else
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-prohibition\"></i>موارد منع مصرف:</h4><div class=\"text\">" + list.getProhibition() + "</div></div></div>";

        if (!list.getCaution().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-caution\"></i>موارد احتیاط:</h4><div class=\"text\">" + list.getCaution() + "</div></div></div>";

        if (!list.getComplication().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-complication\"></i>عوارض جانبی:</h4><div class=\"text\">" + list.getComplication() + "</div></div></div>";

        if (!list.getInterference().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-interference\"></i>تداخلات دارویی:</h4><div class=\"text\">" + list.getInterference() + "</div></div></div>";

        if (!list.getEffectOnTest().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-effect\"></i>اثر بر تست های آزمایشگاهی:</h4><div class=\"text\">" + list.getEffectOnTest() + "</div></div></div>";

        if (!list.getOverdose().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-overdose\"></i>اوردوز و درمان:</h4><div class=\"text\">" + list.getOverdose() + "</div></div></div>";

        JSONObject jsonDescription = null;
        JSONArray codeDescription = new JSONArray();
        String textDescription = "";
        try {
            jsonDescription = new JSONObject(list.getDescription());
            codeDescription = jsonDescription.getJSONArray("code");
            textDescription = jsonDescription.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ((codeDescription.length() != 0 || !textDescription.equals("")) && VEGETAL == 0) {
            try {
                strDescription = "";
                if (codeDescription.length() != 0) {
                    for (int i = 0; i < codeDescription.length(); i++) {
                        if (i != 0)
                            strDescription += "<br>";

                        strDescription += "<span style=\"color:#FF8C00;\"><strong>&gt;&gt; </strong></span>" + getResources().getStringArray(R.array.descriptionGroup)[codeDescription.getInt(i) - 1];
                    }
                }

                if (!textDescription.equals(""))
                    strDescription += textDescription;
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-description\"></i>اطلاعات کلی برای پزشک، پرستار و بیمار:</h4><div class=\"text\">" + strDescription + "</div></div></div>";

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!list.getRelationWithFood().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-relation-with-food\"></i>رابطه با غذا:</h4><div class=\"text\">" + list.getRelationWithFood() + "</div></div></div>";

        if (VEGETAL == 1) {
            if (!list.getMaintenance().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-relation-with-food\"></i>شرایط نگهداری:</h4><div class=\"text\">" + list.getMaintenance() + "</div></div></div>";

            if (!list.getCompany().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-relation-with-food\"></i>شرکت سازنده:</h4><div class=\"text\">" + list.getCompany() + "</div></div></div>";
        }
        webViewHtml += "</div>";
        webView.loadDataWithBaseURL("file:///android_asset/", webViewHtml, "text/html", "UTF-8", null);

//        check exist in favorite
        if (dbHelper.checkFavorite(ID)) {
            addToBasketText.setTextColor(getResources().getColor(R.color.table_link));
            addToBasketText.setText("حذف از سبد دارو");
            addToBasketImage.setColorFilter(getResources().getColor(R.color.table_link));
        } else {
            addToBasketText.setText("اضافه به سبد دارو");
            addToBasketText.setTextColor(getResources().getColor(R.color.bottom_layout_text));
            addToBasketImage.setImageResource(R.drawable.star_icon_view_drug2);
        }

        linearBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dbHelper.checkFavorite(ID)) {
                    dbHelper.bookMark(ID);
                    addToBasketText.setTextColor(getResources().getColor(R.color.table_link));
                    addToBasketText.setText("حذف از سبد دارو");
                    addToBasketImage.setColorFilter(getResources().getColor(R.color.table_link));
                    Toast.makeText(ActivityViewDrug.this, "داروی " + "\"" + list.getName() + "\"" + " به سبد دارو اضافه شد .", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.bookMark(ID);
                    addToBasketText.setText("اضافه به سبد دارو");
                    addToBasketText.setTextColor(getResources().getColor(R.color.bottom_layout_text));
                    addToBasketImage.setColorFilter(getResources().getColor(R.color.bottom_layout_text));
                    Toast.makeText(ActivityViewDrug.this, "داروی " + "\"" + list.getName() + "\"" + " از سبد دارو حذف شد .", Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText(list.getName(), list.getName() + "\n" + list.getFaName());
            }
        });
    }

    private void shareText(String subject, String text) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, subject);
        share.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(share, "Share link!"));
    }
}
