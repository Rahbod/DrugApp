package com.example.behnam.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.fonts.FontTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewDrug extends AppCompatActivity {
    private JSONObject pregnancyGroupsText = new JSONObject();
    String s = "";

    WebView webView;
    BottomNavigationView bottomNavigation;
    String strPregnancy = "";
    String groupPregnancyStr = "";
    String descriptionGroup[] = new String[28];
    String heallingStr = "", pharmaStr = "", sicknessStr = "";
    ImageView btnBack;
    List<Category> categoryList = new ArrayList<>();
    TextView nameDrug;
    FontTextView persianName;
    String strDescription;
    ScrollViewExt scrollView;
    Animation animationToDown;
    Animation animationToUp;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drug);

        webView = findViewById(R.id.web_view_drug);
        nameDrug = findViewById(R.id.name_drug);
        persianName = findViewById(R.id.persian_name);
        scrollView = findViewById(R.id.scroll_view_drug);
        bottomNavigation = findViewById(R.id.navigation_bottom);
        animationToDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animationToUp = AnimationUtils.loadAnimation(this, R.anim.fade_anim);

        bottomNavigation = findViewById(R.id.navigation_bottom);
        bottomNavigation.setVisibility(View.GONE);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                animationToDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        bottomNavigation.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                bottomNavigation.setAnimation(animationToDown);
            }
        });

//        scrollView.init(bottomNavigation,animationToDown);

        try {
            pregnancyGroupsText.put("A", "بررسی های کافی در زنان باردار نتوانسته است خطری را برای جنین در سه ماهه اول بارداری نشان دهد (و شواهدی از وجود خطر در سه ماهه دوم و سوم وجود ندارد.)");
            pregnancyGroupsText.put("B", "بررسی بر روی حیوانات عوارضی را برای جنین نشان نداده است اما مطالعات بالینی کافی بر روی زنان باردار وجود ندارد.");
            pregnancyGroupsText.put("C", "بررسی های انجام شده بر روی حیوانات نشانگر ایجاد عوارض جانبی در جنین بوده اما مطالعات کافی در انسان وجود ندارد. این داروها ممکن است با وجود خطرات بالقوه خود در زنان باردار مفید باشند.");
            pregnancyGroupsText.put("D", "شواهدی در مورد خطرساز بودن این داروها بر روی جنین انسان وجود دارد اما ممکن است با وجود خطرات بالقوه فوق، فواید بالقوه مصرف این دارو در زنان باردار قابل قبول باشد.");
            pregnancyGroupsText.put("X", "بررسی های انسانی یا حیوانی نشانگر ناهنجاری جنینی بوده یا گزاراشات مربوط به عواض جانبی نشان می دهد که دارو برای جنین خطرساز است. در این گروه دارویی، خطرات دارو برتری آشکاری نسبت به فواید آن دارد.");
            pregnancyGroupsText.put("NR", "این داروها هنوز جزو طبقه¬بندی فوق قرار نگرفته اند.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        descriptionGroup[0] = "تا زمان مشخص شدن اثرات دارو از رانندگی و سایر امور نیازمند هوشیاری اجتناب شود. از مصرف الكل و سایر داروهای سركوبگر CNS اجتناب گردد.";
        descriptionGroup[1] = "بیمار را از نظر تغییرات گلوكز سرم مانیتور كنید.";
        descriptionGroup[2] = "كاركرد كلیه و جذب و دفع مایعات را در بیمار مانیتور كنید.";
        descriptionGroup[3] = "كاركرد كبد را در بیمار مانیتور كنید.";
        descriptionGroup[4] = "CBC افتراقی را در بیمار مانیتور كنید.";
        descriptionGroup[5] = "هنگام آمادهسازی این دارو از دستكش لاتكس و عینك محافظ و در صورت امكان از هود بیولوژیك استفاده كنید. از تماس دارو با پوست یا استنشاق آن خودداری كنید.";
        descriptionGroup[6] = "هیچ واكسنی را بدون مجوز پزشك دریافت نكنید (به ویژه واكسنهای زنده ویروسی را).";
        descriptionGroup[7] = " قبل از تجویز این دارو از باردار نبودن بیمار مطمئن شوید و حین مصرف آن نیز از دو روش جلوگیری از بارداری استفاده شود. بیمار باید به محض اطلاع از بارداری فوراً اطلاع بدهد.";
        descriptionGroup[8] = "احتمال ایجاد آلوپسی در بیمار و برگشتپذیر بودن آن را به بیمار اطلاع دهید.";
        descriptionGroup[9] = "مراقب نشت دارو از رگ باشید چراكه خطر عوارض جدی جلدی وجود دارد.";
        descriptionGroup[10] = "تستهای انعقادی را مانیتور كنید.";
        descriptionGroup[11] = "محل تزریق دارو باید مرتباً جابجا شود.";
        descriptionGroup[12] = "خطر ایجاد خونریزی ناشی از دارو را به بیمار اطلاع دهید. بیمار باید مراقب علایم خونریزی گوارشی، ادراری، دهان، بینی و... باشد.";
        descriptionGroup[13] = "این دارو میتواند باعث افت فشارخون و سرگیجه شود. لذا از تغییر وضعیت ناگهانی، حمام داغ، ایستادن طولانی و ادرار كردن در وضعیت ایستاده اجتناب شود.";
        descriptionGroup[14] = "خطر ایجاد عفونت در حین مصرف این دارو را به بیمار تذكر دهید. بیمار باید تا حد امكان از رفتوآمد در مكانهای شلوغ و تماس با افراد آلوده به عفونت اجتناب كند (معمولاً در داروهای شیمی درمانی و سركوبگر ایمنی).";
        descriptionGroup[15] = "علایم حیاتی را در بیمار به دقت مانیتور كنید.";
        descriptionGroup[16] = "قبل از مصرف این دارو باید پوست را شسته و تمیز كرده و خشك نمود (معمولاً در داروهای موضعی).";
        descriptionGroup[17] = "از مصرف این دارو در اطراف چشم و پلكها اجتناب شود.";
        descriptionGroup[18] = "مراقب اختلال الكترولیتی و اختلال اسید و باز در بیمار باشید و وزن او را مانیتور كنید.";
        descriptionGroup[19] = "به بیمار توصیه كنید كه دارو را سر وقت مصرف كرده و در صورت فراموش كردن یك دوز از دو برابر كردن دوز بعدی اجتناب كند.";
        descriptionGroup[20] = "جهت اجتناب از گرمازدگی، از ورزش در هوای گرم اجتناب شود (معمولاً در داروهایی كه تعریق را كم كرده و با خطر گرمازدگی همراه هستند).";
        descriptionGroup[21] = "به بیمار توصیه كنید از قطع ناگهانی این دارو اجتناب كند. قطع این دارو باید به صورت تدریجی باشد (تا مانع ایجاد سندرم ترك دارو یا علایم بیمار شود).";
        descriptionGroup[22] = "به بیمار اطلاع دهید كه مصرف طولانیمدت این دارو میتواند باعث وابستگی و تولرانس شود. لذا مراقب سوء مصرف آن باشید.";
        descriptionGroup[23] = "مراقب شواهد ترومبوسیتوپنی در بیمار بوده و سطح پلاكت را مرتب چك كنید.";
        descriptionGroup[24] = "فشارخون بیمار را مانیتور كنید.";
        descriptionGroup[25] = "به دلیل حساسیت به نور ناشی از این دارو، از قرار گرفتن در آفتاب اجتناب شود و در صورت مجبور بودن، از ضد آفتاب و كلاه و عینك محافظ استفاده شود.";
        descriptionGroup[26] = "كاركرد قلبی و EKG را در بیمار مانیتور كنید.";
        descriptionGroup[27] = "تستهای انعقادی را به صورت دورهای مانیتور كنید.";

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final int ID = bundle.getInt("id");

        final DbHelper dbHelper = new DbHelper(this);
        final Drug drug = dbHelper.getDrug(ID);

        nameDrug.setText(drug.getName());
        persianName.setText(drug.getNamePersian());

        //btnBack
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Check Category Type
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

        String webViewHtml = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /><link rel=\"stylesheet\" type=\"text/css\" href=\"fontiran.css\" /><div class=\"container\">";

        if (!drug.getBrand().isEmpty() || !drug.getKids().isEmpty() || !drug.getSeniors().isEmpty() || !drug.getHowToUse().isEmpty()) {
            if (!drug.getBrand().isEmpty())
                webViewHtml += "<div class=\"section\"><div class=\"row\"><h4 class=\"title\">نام تجاری:</h4><div class=\"text direction-ltr\">"+ drug.getBrand()+"</div></div></div>";
            if (!heallingStr.isEmpty() || !pharmaStr.isEmpty() || !sicknessStr.isEmpty()) {
                webViewHtml += "<div class=\"section\">";
                if (!heallingStr.isEmpty())
                    webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">طبقه بندی درمانی:</h4><div class=\"text\">" + heallingStr + "</div></div>";
                if (!pharmaStr.isEmpty())
                    webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">طبقه بندی فارماکولوژیک:</h4><div class=\"text\">" + pharmaStr + "</div></div>";
                if (!sicknessStr.isEmpty())
                    webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">طبقه بندی بیماری:</h4><div class=\"text\">" + sicknessStr + "</div></div>";
                webViewHtml += "</div>";
            }


            if (!strPregnancy.isEmpty() || !drug.getLactation().isEmpty()) {
                webViewHtml += "<div class=\"section\">";
                if (!drug.getPregnancy().isEmpty()) {
                    try {
                        JSONObject jsonPregnancy = new JSONObject(drug.getPregnancy());
                        JSONArray groupPregnancy = jsonPregnancy.getJSONArray("group");
                        String textPregnancy = jsonPregnancy.getString("text");

                        for (int i = 0; i < groupPregnancy.length(); i++) {
                            pregnancyGroupsText.get(groupPregnancy.getString(i));
                            switch (groupPregnancy.getString(i)) {
                                case "A":
                                    strPregnancy += "<br>" + pregnancyGroupsText.get("A");
                                    break;
                                case "B":
                                    strPregnancy += "<br>" + pregnancyGroupsText.get("B");
                                    break;
                                case "C":
                                    strPregnancy += "<br>" + pregnancyGroupsText.get("C");
                                    break;
                                case "D":
                                    strPregnancy += "<br>" + pregnancyGroupsText.get("D");
                                    break;
                                case "X":
                                    strPregnancy += "<br>" + pregnancyGroupsText.get("X");
                                    break;
                                case "NR":
                                    strPregnancy += "<br>" + pregnancyGroupsText.get("NR");
                                    break;
                            }
                        }
                        strPregnancy += textPregnancy;
                        webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">بارداری:</h4><div class=\"text\">" + groupPregnancyStr + strPregnancy + "</div></div>";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                if (!drug.getLactation().isEmpty())
                    webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">شیردهی:</h4><div class=\"text\">"+ drug.getLactation() + "</div></div>";
                webViewHtml += "</div>";
            }


            if (!drug.getKids().isEmpty() || !drug.getSeniors().isEmpty() || !drug.getHowToUse().isEmpty()) {
                webViewHtml += "<div class=\"section\">";
                if (!drug.getKids().isEmpty())
                    webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">کودکان:</h4><div class=\"text\">" + drug.getKids() + "</div></div>";
                if (!drug.getSeniors().isEmpty())
                    webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">سالمندان:</h4><div class=\"text\">" + drug.getSeniors() + "</div></div>";
                if (!drug.getHowToUse().isEmpty())
                    webViewHtml += "<div class=\"row linear\"><h4 class=\"title\">راه مصرف:</h4><div class=\"text\">" + drug.getHowToUse() + "</div></div>";
                webViewHtml += "</div>";
            }
        }


        if (!drug.getProduct().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-product\"></i>فرآورده های دارویی:</h4><div class=\"text direction-ltr\">" + drug.getProduct() + "</div></div></div>";

        if (!drug.getPharmacodynamic().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-pharmacy\"></i>فارماکودینامیک و فارماکوکینتیک:</h4><div class=\"text\">" + drug.getPharmacodynamic() + "</div></div></div>";

        if (!drug.getUsage().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-usage\"></i>موارد مصرف و دوزاژ:</h4><div class=\"text\">" + drug.getUsage() + "</div></div></div>";

        if (!drug.getDoseAdjustment().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-dose-adjustment\"></i>تعدیل دوزاژ:</h4><div class=\"text\">" + drug.getDoseAdjustment() + "</div></div></div>";

        if (!drug.getProhibition().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-prohibition\"></i>موارد منع مصرف:</h4><div class=\"text\">" + drug.getProhibition() + "</div></div></div>";

        if (!drug.getCaution().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-caution\"></i>موارد احتیاط:</h4><div class=\"text\">" + drug.getCaution() + "</div></div></div>";

        if (!drug.getComplication().isEmpty())
            webViewHtml += "<div class=\"section i-conic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-complication\"></i>عوارض جانبی:</h4><div class=\"text\">" + drug.getComplication() + "</div></div></div>";

        if (!drug.getInterference().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-interference\"></i>تداخلات دارویی:</h4><div class=\"text\">" + drug.getInterference() + "</div></div></div>";

        if (!drug.getEffectOnTest().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-effect\"></i>اثر بر تست های آزمایشگاهی:</h4><div class=\"text\">" + drug.getEffectOnTest() + "</div></div></div>";

        if (!drug.getOverDose().isEmpty())
            webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-overdose\"></i>اوردوز و درمان:</h4><div class=\"text\">" + drug.getOverDose() + "</div></div></div>";

        if (!drug.getDescription().isEmpty()) {
            try {
                JSONObject jsonDescription = new JSONObject(drug.getDescription());
                String textDescription = jsonDescription.getString("text");
                if (!jsonDescription.isNull("code")) {
                    JSONArray jsonArrayCode = jsonDescription.getJSONArray("code");
                    strDescription = "";
                    for (int i = 0; i < jsonArrayCode.length(); i++) {
                        strDescription += descriptionGroup[jsonArrayCode.getInt(i)] + "<br>";
                        Log.e("TAG", "onCreate: " + jsonArrayCode.getInt(i));
                    }
                    strDescription += textDescription;
                    webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-description\"></i>اطلاعات کلی برای بیمار، پرستار و پزشک:</h4><div class=\"text\">" + strDescription + "</div</div></div>";
                } else
                    webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-description\"></i>اطلاعات کلی برای بیمار، پرستار و پزشک:</h4><div class=\"text\">" + textDescription + "</div></div></div>";

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (!drug.getRelationWithFood().isEmpty()) {
                webViewHtml += "<div class=\"section iconic-field\"><div class=\"row\"><h4 class=\"title\"><i class=\"icon-relation-with-food\"></i>رابطه با غذا:</h4><div class=\"text\">" + drug.getRelationWithFood() + "</div></div></div>";
            }

            webViewHtml += "</div>";
            Log.e("TAG", "oooo" + webViewHtml);
            webView.loadDataWithBaseURL("file:///android_asset/", webViewHtml, "text/html", "UTF-8", null);


            bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        //add to favorite
                        case R.id.navigation_favorite:

                            if (!dbHelper.checkFavorite(ID)) {
                                dbHelper.bookMark(ID);
                                Toast.makeText(ActivityViewDrug.this, "داروی شما با موفقیت به سبد دارو اضافه شد.", Toast.LENGTH_SHORT).show();
                            } else {
                                dbHelper.bookMark(ID);
                                Toast.makeText(ActivityViewDrug.this, "داروی شما از لیست سبد دارو حذف شد.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        //send to telegram
                        case R.id.navigation_send:
                            intentMessageTelegram("jijij");
                            break;
                    }
                    return true;
                }
            });
//            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//                @Override
//                public void onScrollChanged() {
//                    animationToDown.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            bottomNavigation.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), "svfvsvsv", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                    bottomNavigation.setAnimation(animationToDown);
//                }
//            });
//
//        //check exists to favorite and set text button
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
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_SCROLL:
//                        Log.e("SCROLL", "ACTION_SCROLL");
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.e("TAG", "ppppppppppppp" );
//                        animationToDown.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                bottomNavigation.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
////                        bottomNavigation.setVisibility(View.GONE);
//                        break;
//                    case MotionEvent.ACTION_DOWN:
//                        Log.e("SCROLL", "ACTION_DOWN");
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        Log.e("SCROLL", "ACTION_cancel");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        animationToUp.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                bottomNavigation.setVisibility(View.VISIBLE);
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
//                        break;
//                }
//                return false;
//            }
//        });
//                    bottomNavigation.startAnimation(animationToUp);

//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int scrollY = scrollView.getScrollY();
//                int totalHeight = scrollView.getChildAt(0).getHeight();
//                Log.e("TAG", "onScrollChanged: " + scrollY);
//                if (scrollY < totalHeight) {
//                    animationToDown.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            bottomNavigation.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                    bottomNavigation.startAnimation(animationToDown);
//                }
//            }
//        });
    }

    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    void intentMessageTelegram(String msg) {
        final String appName = "org.telegram.messenger";
        final boolean isAppInstalled = isAppAvailable(this, appName);
        if (isAppInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_SEND, Uri.parse("https://telegram.me"));
            myIntent.setType("text/plain");
            myIntent.setPackage(appName);
            myIntent.putExtra(Intent.EXTRA_TEXT, msg);//
            startActivity(Intent.createChooser(myIntent, "Share with"));
        } else {
            Toast.makeText(this, "تلگرام نصب نیست", Toast.LENGTH_SHORT).show();
        }
    }
}
