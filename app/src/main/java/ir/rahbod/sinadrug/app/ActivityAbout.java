package ir.rahbod.sinadrug.app;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.rahbod.sinadrug.app.helper.SessionManager;
import ir.rahbod.sinadrug.app.map.MapActivity;

import java.io.File;

import ir.rahbod.sinadrug.app.helper.SessionManager;
import ir.rahbod.sinadrug.app.map.MapActivity;

public class ActivityAbout extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txt = findViewById(R.id.txt);
        // Set user name and mobile
        NavigationView navigationView = findViewById(R.id.aboutNavView);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.txtNameNav);
        navUserName.setText(SessionManager.getExtrasPref(this).getString("name"));
        TextView navUserMobile = headerView.findViewById(R.id.txtMobileNav);
        navUserMobile.setText(SessionManager.getExtrasPref(this).getString("mobile"));

        if (getIntent().getStringExtra("type").equals("rules")) {
            TextView txtTitle = findViewById(R.id.txtTitle);

            txtTitle.setText("قوانین و مقررات");
            String text = "قوانین و مقررات استفاده از اپلیکیشن سینادارو" + "\n\n" + "کلیه حقوق مادی و معنوی اپلیکیشن سینادارو و تمامی محتویات آن شامل آیکون ها، نشان  تجاری داخل اپلیکیشن طبق قانون مالکیت مادی و معنوی، به انتشارات سیناطب و رودگون اختصاص داشته و هرگونه استفاده از نام، متون، مطالب، مستندات نرم افزار بدون اجازه کتبی از پدیدآورندگان مطابق با قوانین جرایم نرم افزاری و رایانه ای، ممنوع و غیرمجاز تلقی میگردد و قابل پیگرد قانونی است." +
                    "\n" +
                    "اطلاعات دارویی ارائه شده در این نرم افزار صرفاً جهت اطلاع رسانی به کاربر است و برای هرگونه تجویز دارو به " +
                    "بیمار لازم است تا این کار توسط پزشک معالج بر مبنای معاینه دقیق و بررسی های بالینی و پاراکلینیکی صورت گیرد و " +
                    "این اطلاعات نمی تواند بعنوان مرجعی برای تجویز و یا مصرف خودسرانه هیچ دارویی محسوب گردد. \nبدینوسیله " +
                    "تأکید میشود تمامی مسئولیت استفاده از محتوای اپلیکیشن و مصرف خودسرانه داروها به طور کلی به عهده کاربر " +
                    "بوده و پدیدآورندگان این اپلیکیشن هیچ مسئولیتی در این زمینه نخواهند داشت.\n";

            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 36, 44, 0);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 77, 86, 0);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 180, 205, 0);
            txt.setText(spannableString);
        } else {
            String text = "1- اپلیکیشن سینادارو، تنها برای استفاده کاربرانی که در رشته های داروسازی، پزشکی یا زیرگروه های پزشکی مشغول به تحصیل می باشد تهیه و به بازار ارائه شده است و برای استفاده سایر رشته ها از این نرم افزار توصیه نمی شود.\nهرگونه کپی برداری و سعی در هک کردن داده های نرم افزار طبق قوانین نرم افزاری حاکم بر جمهوری اسلامی ایران غیرمجاز و ممنوع تلقی شده و علاوه بر پیگرد قانونی، شرایط پشتیبانی و خدمات پس از فروش نرم افزار را لغو خواهد نمود." +
                    "\n\n2- حفظ و نگهداری از اطلاعات نرم افزار، صرفاً بر عهده کاربر خواهد بود و در صورت کپی برداری، استفاده غیرمجاز و یا کرک از نسخه تحت مالکیت مشتری، تمامی مسئولیت های قانونی در این زمینه متوجه کاربرخواهد بود و طبق قانون حقوق مؤلفین و پدیدآورندگان نرم افزار، پیگرد قانونی خواهد داشت و بعلاوه شرایط پشتیبانی و خدمات پس از فروش نرم افزار را نیز لغو خواهد نمود. در همین راستا پدیدآورندگان هم متعهد در حفظ و نگهداری از اطلاعات کاربر هستند و نهایت دقت و امنیت را در این مورد لحاظ کرده اند." +
                    "\n\n3- استفاده از محتواى نرم افزار شامل لوگو، علائم تجارى، آیکون ها و اطلاعاتی که در این نرم افزار است، بدون اجازه کتبى پدیدآورنده، ممنوع بوده و هیچ کس نمی تواند به هر دلیلی هیچ جزئی از اطلاعات موجود در این نرم افزار را در رسانه، نرم افزار، کتاب و یا هرگونه محتواى دیجیتالی و چاپی دیگری منتشر نماید و این امر اکیداً ممنوع است." +
                    "\n\n4- اطلاعات دارویی ارائه شده در این نرم افزار صرفاً جهت اطلاع رسانی به کاربر است و برای هرگونه تجویز دارو به بیمار لازم است تا این کار توسط پزشک معالج برمبنای معاینه دقیق و بررسی های بالینی و پاراکلینیکی صورت گیرد و این اطلاعات نمی تواند بعنوان مرجعی برای تجویز و یا مصرف خودسرانه هیچ دارویی محسوب گردد. بدینوسیله تأکید می شود تمامی مسئولیت استفاده از محتوای اپلیکیشن  و مصرف خودسرانه داروها به طور کلی به عهده کاربر بوده و پدیدآورندگان این اپلیکیشن هیچ مسئولیتی در این زمینه نخواهند داشت." +
                    "\n\n5- استفاده از این اپلیکیشن و فعال سازی آن مستلزم ثبت نام و پرداخت هزینه است. این نرم افزار بر روی یک دستگاه با سیستم عامل اندروید بدون محدودیت و به دفعات قابل نصب است و نیاز به پرداخت هزینه مجدد ندارد، مگر اینکه دستگاه ریست کلی شود. برای فعال سازی نرم افزار در هنگام نصب باید دستگاه کاربر به اینترنت متصل باشد تا دیتای اپلیکیشن را دریافت نماید؛ پس از فعال سازی، نیاز به اینترنت نیست و کاربر بصورت Offline می تواند از محتویات آن استفاده کند، بنابراین هیچگونه محدودیت زمانی برای استفاده ندارد." +
                    "\n\n6- کاربر در صورت نیاز به استفاده از نرم افزار بر روی دستگاههای دیگر خود باید اعتبار جداگانه خریداری نماید." +
                    "\n\n7- نصب نرم افزار، فعالسازی و استفاده از آن به منزله رؤیت و مطالعه دقیق مفاد فوق و قبول تمامی بندهای آن است.\n" +
                    "\n" +
                    "تمامی حقوق مادی و معنوی نرم افزار متعلق به پدیدآورندگان آن می باشد.\n" +
                    "\n" +
                    "سیناطب دفتر مرکزی: 02166902745\n" +
                    "سایت: www.sinateb.net\n" +
                    "تلگرام: Sinatebpub@\n" +
                    "ایمیل: Sinatebpub@gmail.com\n" +
                    "\n" +
                    "رودگون دفتر مرکزی: 02166592252\n" +
                    "سایت:www.rodgoon.com\n" +
                    "ایمیل: rodgoonpub@gmail.com";
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 12, 20, 0);
            txt.setText(spannableString);
        }

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        drawerLayout = findViewById(R.id.DrawerLayout);
        ImageView imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
        imgOpenNvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }, 100);
            }
        });
    }

    public void openNv(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.item1:
                Intent goToListDrugInteractions = new Intent(this, ActivityListDrugInterference.class);
                startActivity(goToListDrugInteractions);
                closeNv();
                break;
            case R.id.item2:
                startActivity(new Intent(this, MapActivity.class));
                closeNv();
                break;
            case R.id.item3:
                Intent goToReminder = new Intent(this, ActivityReminderList.class);
                startActivity(goToReminder);
                closeNv();
                break;
            case R.id.item4:
                Intent goToFavorite = new Intent(this, ActivityFavorite.class);
                startActivity(goToFavorite);
                closeNv();
                break;
            case R.id.item5:
                shareApplication();
                closeNv();
                break;
            case R.id.item6:
                Intent goToErrorReport = new Intent(this, ActivityErrorReport.class);
                startActivity(goToErrorReport);
                closeNv();
                break;
            case R.id.item7:
                if (getIntent().getStringExtra("type").equals("rules")) {
                    Intent intentSearch = new Intent(this, ActivityAbout.class);
                    intentSearch.putExtra("type", "about");
                    startActivity(intentSearch);
                    closeNv();
                } else {
                    closeNv();
                }
                break;
            case R.id.item8:
                Intent intentVegetalDrug = new Intent(this, ActivityDrug.class);
                startActivity(intentVegetalDrug);
                closeNv();
                break;
            case R.id.item9:
                Intent intentDrug = new Intent(this, ActivityHome.class);
                startActivity(intentDrug);
                closeNv();
                break;
            case R.id.item10:
                if (getIntent().getStringExtra("type").equals("rules")) {
                    closeNv();
                } else {
                    Intent intentSearch = new Intent(this, ActivityAbout.class);
                    intentSearch.putExtra("type", "rules");
                    startActivity(intentSearch);
                    closeNv();
                }
                break;
            case R.id.item11:
                Intent intentPharma = new Intent(this, ActivityCategories.class);
                intentPharma.putExtra("type", 1);
                startActivity(intentPharma);
                closeNv();
                break;
            case R.id.item12:
                Intent intentHealing = new Intent(this, ActivityCategories.class);
                intentHealing.putExtra("type", 0);
                startActivity(intentHealing);
                closeNv();
                break;
            case R.id.item13:
                Intent intent = new Intent(this, ActivityListNotifications.class);
                startActivity(intent);
                closeNv();
                break;
        }
    }

    private void closeNv() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        }, 250);
    }

    private void shareApplication() {
        Intent intent = new Intent(Intent.ACTION_SEND);

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;

            intent.setType("*/*");

            File file = new File(filePath);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.rahbod.ir/SinaDrugs.apk");
        }

        startActivity(Intent.createChooser(intent, "Share app via"));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            super.onBackPressed();
    }
}
