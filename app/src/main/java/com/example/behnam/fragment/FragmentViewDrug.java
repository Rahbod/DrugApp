package com.example.behnam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.behnam.app.R;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Drug2;
import com.example.behnam.app.dialog.SummaryDialog;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.fonts.FontTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FragmentViewDrug extends Fragment {

    String s = "";

    WebView webView;
    String strPregnancy = "";
    String groupPregnancyStr = "";
    String healingStr = "", pharmaStr = "", sicknessStr = "";
    String strDescription;

    Animation animationToDown;
    Animation animationToUp;

    LinearLayout relativebottom;
    LinearLayout linearBasket;
    FontTextView addToBasketText;
    ImageView addToBasketImage;

    LinearLayout linearfriend;


    public FragmentViewDrug() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =LayoutInflater.from(getActivity()).inflate(R.layout.view_drug, null);

        webView = view.findViewById(R.id.web_view_drug);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                SummaryDialog summaryDialog = new SummaryDialog(getActivity(), url);
                summaryDialog.show();
                return true;
            }

        });

        relativebottom = view.findViewById(R.id.relative_bottom);
        addToBasketText = view.findViewById(R.id.add_basket_item);
        addToBasketImage = view.findViewById(R.id.add_basket_ico);
        linearBasket = view.findViewById(R.id.layout_basket);
        linearfriend = view.findViewById(R.id.layout_friend);

        animationToUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
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

        animationToDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
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


        final int ID = getActivity().getIntent().getIntExtra("id", 0);
        int VEGETAL = getActivity().getIntent().getIntExtra("vegetal", 0);

        final DbHelper dbHelper = new DbHelper(getActivity());
        final Drug drug = dbHelper.getDrug(ID);

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

        if (!drug.getBrand().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">نام تجاری:</h4><div class=\"text direction-ltr\">" + drug.getBrand() + "</div></div></div>";

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
            jsonPregnancy = new JSONObject(drug.getPregnancy());
            groupPregnancy = jsonPregnancy.getJSONArray("group");
            textPregnancy = jsonPregnancy.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ((groupPregnancy.length() != 0 || !textPregnancy.equals("") || !drug.getLactation().isEmpty())) {
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

            if (!drug.getLactation().isEmpty() && VEGETAL == 0)
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">مصرف در دوران شیردهی:</h4><div class=\"text\">" + drug.getLactation() + "</div></div>";
            webViewHtml += "</div>";
        }

        if ((!drug.getKids().isEmpty() || !drug.getSeniors().isEmpty() || !drug.getHowToUse().isEmpty()) && VEGETAL == 0) {
            webViewHtml += "<div class=\"section\">";
            if (!drug.getKids().isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">مصرف در کودکان:</h4><div class=\"text\">" + drug.getKids() + "</div></div>";

            if (!drug.getSeniors().isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">مصرف در سالمندان:</h4><div class=\"text\">" + drug.getSeniors() + "</div></div>";

            if (!drug.getHowToUse().isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">راه مصرف:</h4><div class=\"text\">" + drug.getHowToUse() + "</div></div>";
            webViewHtml += "</div>";
        }

        if (VEGETAL == 1) {
            webViewHtml += "<div class=\"section\">";
            if (!drug.getHowToUse().isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">راه مصرف:</h4><div class=\"text\">" + drug.getHowToUse() + "</div></div>";
            webViewHtml += "</div>";
        }

        if (!drug.getProduct().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>فرآورده های دارویی:</h4><div class=\"text direction-ltr\">" + drug.getProduct() + "</div></div></div>";

        if (VEGETAL == 1) {
            if (!drug.getCompounds().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>ترکیبات موجود:</h4><div class=\"text direction-ltr\">" + drug.getCompounds() + "</div></div></div>";

            if (!drug.getEffectiveIngredients().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>مواد موثره:</h4><div class=\"text direction-ltr\">" + drug.getEffectiveIngredients() + "</div></div></div>";

            if (!drug.getStandardized().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>استاندارد شده:</h4><div class=\"text direction-ltr\">" + drug.getStandardized() + "</div></div></div>";
        }

        if (!drug.getPharmacodynamic().isEmpty())
            if (VEGETAL == 1)
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-pharmacy\"></i>مکانیسم احتمالی اثر:</h4><div class=\"text\">" + drug.getPharmacodynamic() + "</div></div></div>";
            else
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-pharmacy\"></i>فارماکودینامیک و فارماکوکینتیک:</h4><div class=\"text\">" + drug.getPharmacodynamic() + "</div></div></div>";

        if (!drug.getUsage().isEmpty())
            if (VEGETAL == 1)
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-usage\"></i>موارد مصرف:</h4><div class=\"text\">" + drug.getUsage() + "</div></div></div>";
            else
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-usage\"></i>موارد مصرف و دوزاژ:</h4><div class=\"text\">" + drug.getUsage() + "</div></div></div>";

        if (!drug.getDoseAdjustment().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-dose-adjustment\"></i>تعدیل دوزاژ:</h4><div class=\"text\">" + drug.getDoseAdjustment() + "</div></div></div>";

        if (!drug.getProhibition().isEmpty())
            if (VEGETAL == 1)
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-prohibition\"></i>موارد منع مصرف و احتیاط:</h4><div class=\"text\">" + drug.getProhibition() + "</div></div></div>";
            else
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-prohibition\"></i>موارد منع مصرف:</h4><div class=\"text\">" + drug.getProhibition() + "</div></div></div>";

        if (!drug.getCaution().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-caution\"></i>موارد احتیاط:</h4><div class=\"text\">" + drug.getCaution() + "</div></div></div>";

        if (!drug.getComplication().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-complication\"></i>عوارض جانبی:</h4><div class=\"text\">" + drug.getComplication() + "</div></div></div>";

        if (!drug.getInterference().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-interference\"></i>تداخلات دارویی:</h4><div class=\"text\">" + drug.getInterference() + "</div></div></div>";

        if (!drug.getEffectOnTest().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-effect\"></i>اثر بر تست های آزمایشگاهی:</h4><div class=\"text\">" + drug.getEffectOnTest() + "</div></div></div>";

        if (!drug.getOverdose().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-overdose\"></i>اوردوز و درمان:</h4><div class=\"text\">" + drug.getOverdose() + "</div></div></div>";

        JSONObject jsonDescription = null;
        JSONArray codeDescription = new JSONArray();
        String textDescription = "";
        try {
            jsonDescription = new JSONObject(drug.getDescription());
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

        if (!drug.getRelationWithFood().isEmpty() && VEGETAL == 0)
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-relation-with-food\"></i>رابطه با غذا:</h4><div class=\"text\">" + drug.getRelationWithFood() + "</div></div></div>";

        if (VEGETAL == 1) {
            if (!drug.getMaintenance().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-relation-with-food\"></i>شرایط نگهداری:</h4><div class=\"text\">" + drug.getMaintenance() + "</div></div></div>";

            if (!drug.getCompany().isEmpty())
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-relation-with-food\"></i>شرکت سازنده:</h4><div class=\"text\">" + drug.getCompany() + "</div></div></div>";
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
                    Toast.makeText(getActivity(), "داروی " + "\"" + drug.getName() + "\"" + " به سبد دارو اضافه شد .", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.bookMark(ID);
                    addToBasketText.setText("اضافه به سبد دارو");
                    addToBasketText.setTextColor(getResources().getColor(R.color.bottom_layout_text));
                    addToBasketImage.setColorFilter(getResources().getColor(R.color.bottom_layout_text));
                    Toast.makeText(getActivity(), "داروی " + "\"" + drug.getName() + "\"" + " از سبد دارو حذف شد .", Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText(drug.getName(), drug.getName() + "\n" + drug.getFaName());
            }
        });

        return view;
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