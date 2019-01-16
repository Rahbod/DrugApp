package ir.rahbod.sinadrug.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.rahbod.sinadrug.app.R;
import ir.rahbod.sinadrug.app.database.Category;
import ir.rahbod.sinadrug.app.database.Drug;
import ir.rahbod.sinadrug.app.dialog.SummaryDialog;
import ir.rahbod.sinadrug.app.helper.DbHelper;
import ir.rahbod.sinadrug.fonts.FontTextView;
import ir.rahbod.sinadrug.fonts.FontTextViewBold;

public class FragmentViewDrug2 extends Fragment {
    private String strPregnancy = "";
    private String healingStr = "", pharmaStr = "", sicknessStr = "";
    private LinearLayout relativebottom;
    private FontTextView addToBasketText;
    private ImageView addToBasketImage;

    public FragmentViewDrug2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_drug, null);
        WebView webView = view.findViewById(R.id.web_view_drug);
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
                if(url.equals("http://localhost/#pregnancy")) {
                    SummaryDialog summaryDialog = new SummaryDialog(getActivity(), url);
                    summaryDialog.show();
                }else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
                return true;
            }

        });

        relativebottom = view.findViewById(R.id.relative_bottom);
        addToBasketText = view.findViewById(R.id.add_basket_item);
        addToBasketImage = view.findViewById(R.id.add_basket_ico);
        LinearLayout linearBasket = view.findViewById(R.id.layout_basket);
        LinearLayout linearFriend = view.findViewById(R.id.layout_friend);

        Animation animationToUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
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
        Animation animationToDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
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
        FontTextViewBold nameDrug = getActivity().findViewById(R.id.name_drug);
        FontTextView persianName = getActivity().findViewById(R.id.persian_name);

        nameDrug.setText(drug.getName());
        persianName.setText(drug.getFaName());

        /* @TODO: show categories in view page
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
        if (!healingStr.isEmpty() || !pharmaStr.isEmpty() || !sicknessStr.isEmpty()) {
            webViewHtml += "<div class=\"section\">";
            if (!healingStr.isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">طبقه بندی درمانی:</h4><div class=\"text\">" + healingStr + "</div></div>";
            if (!pharmaStr.isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">طبقه بندی فارماکولوژیک:</h4><div class=\"text\">" + pharmaStr + "</div></div>";
            if (!sicknessStr.isEmpty())
                webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">دسته بندی بر اساس بیماری:</h4><div class=\"text\">" + sicknessStr + "</div></div>";
            webViewHtml += "</div>";
        }*/

        String brandHtml = "",
                pregnancyHtml = "",
                lactationHtml = "",
                kidsHtml = "",
                seniorsHtml = "",
                howToUseHtml = "",
                productHtml = "",
                pharmacodynamicHtml = "",
                usageHtml = "",
                prohibitionHtml = "",
                cautionHtml = "",
                doseAdjustmentHtml = "",
                complicationHtml = "",
                interferenceHtml = "",
                effectOnTestHtml = "",
                overdoseHtml = "",
                descriptionHtml = "",
                relationWithFoodHtml = "",
                compoundsHtml = "",
                effectiveIngredientsHtml = "",
                standardizedHtml = "",
                maintenanceHtml = "",
                companyHtml = "";

        // Make brand html
        if (!drug.getBrand().isEmpty() && VEGETAL == 0)
            brandHtml = "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-brand\"></i>نام تجاری:</h4><div class=\"text direction-ltr\">" + drug.getBrand() + "</div></div></div>";

        // Make pregnancy html
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
        if ((groupPregnancy.length() != 0 || (!textPregnancy.equals("") && !(textPregnancy.equals("null"))) || !drug.getLactation().isEmpty())) {
            pregnancyHtml = "<div class=\"section";
            if(VEGETAL == 0)
                pregnancyHtml += " iconic-field";
            pregnancyHtml += "\">";
            if (groupPregnancy.length() != 0 || (!textPregnancy.equals("") && !(textPregnancy.equals("null")))) {
                try {
                    if (groupPregnancy.length() != 0) {
                        strPregnancy += "<span class=\"pregnancy-group\">گروه ";
                        for (int i = 0; i < groupPregnancy.length(); i++) {
                            if (i != 0)
                                strPregnancy += " و ";

//                            strPregnancy += "<span style=\"font-weight:bold;color:#0334b0;\">گروه " + groupPregnancy.getString(i) + "</span> " + pregnancyGroupsText.get(groupPregnancy.getString(i));
                            strPregnancy += groupPregnancy.getString(i);
                        }
                        strPregnancy += "</span><a href=\"http://localhost/#pregnancy\" class=\"pregnancy-modal-trigger\"></a>";
                    }
                    if (!textPregnancy.equals("") && !(textPregnancy.equals("null")))
                        strPregnancy += textPregnancy;
                    if (VEGETAL == 1)
                        pregnancyHtml += "<div class=\"row\"><h4 class=\"title\">مصرف در دوران بارداری و شیردهی:</h4><div class=\"text\">" + strPregnancy + "</div></div>";
                    else
                        pregnancyHtml += "<div class=\"row linear\"><h4 class=\"title\"><i class=\"icon-lactation\"></i>مصرف در دوران بارداری:</h4><div class=\"text\">" + strPregnancy + "</div></div>";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pregnancyHtml += "</div>";
        }

        // Make lactation html
        if (!drug.getLactation().isEmpty() && VEGETAL == 0)
            lactationHtml = "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">مصرف در دوران شیردهی:</h4><div class=\"text\">" + drug.getLactation() + "</div></div></div>";

        // Make kids html
        if (!drug.getKids().isEmpty() && VEGETAL == 0)
            kidsHtml = "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-kids\"></i>مصرف در کودکان:</h4><div class=\"text\">" + drug.getKids() + "</div></div></div>";

        // Make seniors html
        if (!drug.getSeniors().isEmpty() && VEGETAL == 0)
            seniorsHtml = "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-seniors\"></i>مصرف در سالمندان:</h4><div class=\"text\">" + drug.getSeniors() + "</div></div></div>";

        // Make how to use html
        if (!drug.getHowToUse().isEmpty()) {
            howToUseHtml = "<div class=\"section\">";
            if (VEGETAL == 0)
                howToUseHtml += "<div class=\"row linear\"><h4 class=\"title\">راه مصرف:</h4><div class=\"text\">" + drug.getHowToUse() + "</div></div>";
            else
                howToUseHtml += "<div class=\"row\"><h4 class=\"title\">روش مصرف:</h4><div class=\"text\">" + drug.getHowToUse() + "</div></div>";
            howToUseHtml += "</div>";
        }

        // Make product html
        if (!drug.getProduct().isEmpty()) {
            if (VEGETAL == 1)
                productHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">فرآورده های دارویی:</h4><div class=\"text\">" + drug.getProduct() + "</div></div></div>";
            else
                productHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>فرآورده های دارویی:</h4><div class=\"text direction-ltr\">" + drug.getProduct() + "</div></div></div>";
        }

        // Make pharmacodynamic html
        if (!drug.getPharmacodynamic().isEmpty()) {
            if (VEGETAL == 1)
                pharmacodynamicHtml = "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">مکانیسم اثر:</h4><div class=\"text\">" + drug.getPharmacodynamic() + "</div></div></div>";
            else
                pharmacodynamicHtml = "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-pharmacy\"></i>فارماکودینامیک و فارماکوکینتیک:</h4><div class=\"text\">" + drug.getPharmacodynamic() + "</div></div></div>";
        }

        // Make usage html
        if (!drug.getUsage().isEmpty()) {
            if (VEGETAL == 1)
                usageHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">موارد مصرف:</h4><div class=\"text\">" + drug.getUsage() + "</div></div></div>";
            else
                usageHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-usage\"></i>موارد مصرف و دوزاژ:</h4><div class=\"text\">" + drug.getUsage() + "</div></div></div>";
        }

        // Make prohibition html
        if (!drug.getProhibition().isEmpty()) {
            if (VEGETAL == 1)
                prohibitionHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">موارد منع مصرف و احتیاط:</h4><div class=\"text\">" + drug.getProhibition() + "</div></div></div>";
            else
                prohibitionHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-prohibition\"></i>موارد منع مصرف:</h4><div class=\"text\">" + drug.getProhibition() + "</div></div></div>";
        }

        // Make caution html
        if (!drug.getCaution().isEmpty() && VEGETAL == 0)
            cautionHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-caution\"></i>موارد احتیاط:</h4><div class=\"text\">" + drug.getCaution() + "</div></div></div>";

        // Make dose adjustment html
        if (!drug.getDoseAdjustment().isEmpty() && VEGETAL == 0)
            doseAdjustmentHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-dose-adjustment\"></i>تعدیل دوزاژ:</h4><div class=\"text\">" + drug.getDoseAdjustment() + "</div></div></div>";

        // Make complication html
        if (!drug.getComplication().isEmpty()){
            if(VEGETAL == 0)
                complicationHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-complication\"></i>عوارض جانبی:</h4><div class=\"text\">" + drug.getComplication() + "</div></div></div>";
            else
                complicationHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">عوارض جانبی:</h4><div class=\"text\">" + drug.getComplication() + "</div></div></div>";
        }

        // Make interference html
        if (!drug.getInterference().isEmpty()){
            if(VEGETAL == 0)
                interferenceHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-interference\"></i>تداخلات دارویی:</h4><div class=\"text\">" + drug.getInterference() + "</div></div></div>";
            else
                interferenceHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">تداخلات دارویی:</h4><div class=\"text\">" + drug.getInterference() + "</div></div></div>";
        }

        // Make effect on test html
        if (!drug.getEffectOnTest().isEmpty() && VEGETAL == 0)
            effectOnTestHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-effect\"></i>اثر بر تست های آزمایشگاهی:</h4><div class=\"text\">" + drug.getEffectOnTest() + "</div></div></div>";

        // Make overdose html
        if (!drug.getOverdose().isEmpty() && VEGETAL == 0)
            overdoseHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-overdose\"></i>اوردوز و درمان:</h4><div class=\"text\">" + drug.getOverdose() + "</div></div></div>";

        // Make description html
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
                String strDescription = "";
                if (codeDescription.length() != 0) {
                    for (int i = 0; i < codeDescription.length(); i++) {
                        if (i != 0)
                            strDescription += "<br>";

                        strDescription += "<span style=\"color:#FF8C00;\"><strong>&gt;&gt; </strong></span>" + getResources().getStringArray(ir.rahbod.sinadrug.app.R.array.descriptionGroup)[codeDescription.getInt(i) - 1];
                    }
                }
                if (!textDescription.equals(""))
                    strDescription += textDescription;
                descriptionHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-description\"></i>اطلاعات کلی برای پزشک، پرستار و بیمار:</h4><div class=\"text\">" + strDescription + "</div></div></div>";

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Make relation with food html
        if (!drug.getRelationWithFood().isEmpty() && VEGETAL == 0)
            relationWithFoodHtml= "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-relation-with-food\"></i>رابطه با غذا:</h4><div class=\"text\">" + drug.getRelationWithFood() + "</div></div></div>";

        // Make compounds html
        if (!drug.getCompounds().isEmpty() && VEGETAL == 1)
            compoundsHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">ترکیبات موجود:</h4><div class=\"text\">" + drug.getCompounds() + "</div></div></div>";

        // Make effective ingredients html
        if (!drug.getEffectiveIngredients().isEmpty() && VEGETAL == 1)
            effectiveIngredientsHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">مواد موثره:</h4><div class=\"text\">" + drug.getEffectiveIngredients() + "</div></div></div>";

        // Make standardized html
        if (!drug.getStandardized().isEmpty() && VEGETAL == 1)
            standardizedHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">استاندارد شده:</h4><div class=\"text\">" + drug.getStandardized() + "</div></div></div>";

        // Make maintenance html
        if (!drug.getMaintenance().isEmpty() && VEGETAL == 1)
            maintenanceHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">شرایط نگهداری:</h4><div class=\"text\">" + drug.getMaintenance() + "</div></div></div>";

        // Make company html
        if (!drug.getCompany().isEmpty() && VEGETAL == 1)
            companyHtml= "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">شرکت سازنده یا وارد کننده:</h4><div class=\"text\">" + drug.getCompany() + "</div></div></div>";

        // Make main (page) html
        String webViewHtml = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /><link rel=\"stylesheet\" type=\"text/css\" href=\"fontiran.css\" />";
        webViewHtml += "<div class=\"container"+(VEGETAL == 1 ? " vegetal" : "")+"\">";

        if (VEGETAL == 0){
            webViewHtml += brandHtml;
            webViewHtml += pregnancyHtml;
            webViewHtml += lactationHtml;
            webViewHtml += howToUseHtml;
            webViewHtml += kidsHtml;
            webViewHtml += seniorsHtml;
            webViewHtml += productHtml;
            webViewHtml += pharmacodynamicHtml;
            webViewHtml += usageHtml;
            webViewHtml += doseAdjustmentHtml;
            webViewHtml += prohibitionHtml;
            webViewHtml += cautionHtml;
            webViewHtml += complicationHtml;
            webViewHtml += interferenceHtml;
            webViewHtml += effectOnTestHtml;
            webViewHtml += overdoseHtml;
            webViewHtml += descriptionHtml;
            webViewHtml += relationWithFoodHtml;
        }else{
            webViewHtml += productHtml;
            webViewHtml += usageHtml;
            webViewHtml += pregnancyHtml;
            webViewHtml += howToUseHtml;
            webViewHtml += compoundsHtml;
            webViewHtml += effectiveIngredientsHtml;
            webViewHtml += standardizedHtml;
            webViewHtml += pharmacodynamicHtml;
            webViewHtml += prohibitionHtml;
            webViewHtml += complicationHtml;
            webViewHtml += interferenceHtml;
            webViewHtml += maintenanceHtml;
            webViewHtml += companyHtml;
        }

        webViewHtml += "</div>";
        webView.loadDataWithBaseURL("file:///android_asset/", webViewHtml, "text/html", "UTF-8", null);

        // Check exist in favorite
        if (dbHelper.checkFavorite(ID)) {
            addToBasketText.setTextColor(getResources().getColor(R.color.table_link));
            addToBasketText.setText("حذف از نشان شده ها");
            addToBasketImage.setColorFilter(getResources().getColor(R.color.table_link));
        } else {
            addToBasketText.setText("نشان کردن");
            addToBasketText.setTextColor(getResources().getColor(R.color.bottom_layout_text));
            addToBasketImage.setImageResource(R.drawable.star_icon_view_drug2);
        }

        linearBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dbHelper.checkFavorite(ID)) {
                    dbHelper.bookMark(ID);
                    addToBasketText.setTextColor(getResources().getColor(R.color.table_link));
                    addToBasketText.setText("حذف از نشان شده ها");
                    addToBasketImage.setColorFilter(getResources().getColor(R.color.table_link));
                    Toast.makeText(getActivity(), "داروی " + "\"" + drug.getName() + "\"" + " نشان شد.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.bookMark(ID);
                    addToBasketText.setText("نشان کردن");
                    addToBasketText.setTextColor(getResources().getColor(R.color.bottom_layout_text));
                    addToBasketImage.setColorFilter(getResources().getColor(R.color.bottom_layout_text));
                    Toast.makeText(getActivity(), "داروی " + "\"" + drug.getName() + "\"" + " از نشان شده ها حذف شد.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linearFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = drug.getName() + "\n \n";
                text += drug.getFaName();
                text += getHealingShare(String.valueOf(dbHelper.getHealingCategory(ID)));
                text += stripHtml(drug.getUsage());
                text += "http://www.rahbod.ir/PharmaSina.apk";
                shareText(drug.getName(), text);
            }
        });

        return view;
    }

    private void shareText(String subject, String text) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, subject);
        share.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(share, "ارسال به دوستان"));
    }

    private String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return getUsageShare(String.valueOf(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            return getUsageShare(String.valueOf(Html.fromHtml(html)));
        }
    }

    private String getHealingShare(String text) {
        if (!(text.equals("[]"))) {
            return "\n \n" + "طبقه بندی درمانی: " + text + "\n \n";
        } else
            return "";
    }

    private String getUsageShare(String text) {
        if (!(text.equals(""))) {
            return "\n \n" + "موارد مصرف: " + "\n" + text;
        } else
            return "";
    }
}