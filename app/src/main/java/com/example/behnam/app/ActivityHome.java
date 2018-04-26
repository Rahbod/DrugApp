package com.example.behnam.app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.behnam.app.adapter.AdapterAlphabetIndexFastScroll;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.fastscroll.AlphabetItem;
import com.example.behnam.app.helper.Components;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;
import com.example.behnam.app.map.MapActivity;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.ui.SpeechProgressView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

public class ActivityHome extends AppCompatActivity implements SpeechDelegate {

    private ImageView imgOpenNvDraw;
    private AdapterAlphabetIndexFastScroll adapterHome;
    private DrawerLayout drawerLayout;
    private List<Drug> drugList = new ArrayList<>();
    List<Drug> list = new ArrayList<>();
    private ImageView btnListen;
    private EditText text;
    private SpeechProgressView progress;
    private ConnectivityManager connectivityManager;
    private List<AlphabetItem> mAlphabetItems;


    SharedPreferences sharedPreferences;
    RecyclerView mRecyclerView;
    private static final int time = 2000;
    private static long BackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setContentView(R.layout.navigation_view);

        text = findViewById(R.id.editTextSearch);

        //finish splash screen
        ActivitySplashScreen.activitySplashScreen.finish();

        if (!SessionManager.getExtrasPref(this).getBoolean("firstDataIsComplete"))
            Components.downloadData(this, "first");

        //help screen voice
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = sharedPreferences.getBoolean("firstRun", true);
        if (isFirstRun) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
            btnListen = findViewById(R.id.imgVoice);
            new GuideView.Builder(this)
                    .setTitle("جستجوی صوتی")
                    .setTitleTextSize(25)
                    .setContentText("جهت جستجوی داروی مورد نظر کافیست نام آن را تلفظ کنید.")
                    .setContentTextSize(18)
                    .setDismissType(GuideView.DismissType.anywhere)
                    .setTargetView(btnListen)
                    .build()
                    .show();
        }

        // search
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
        drawerLayout = findViewById(R.id.DrawerLayout);

        imgOpenNvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                Class<? extends View.OnClickListener> view = this.getClass();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }, 100);
            }
        });

        showDrugs(this);

        btnListen = findViewById(R.id.imgVoice);
        progress = findViewById(R.id.progressBarHome);

        Speech.init(this, getPackageName());

        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        voiceSearch
                final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHome.this);
                    builder.setMessage(R.string.enable_wifi).setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (wifiManager != null)
                                        wifiManager.setWifiEnabled(true);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                } else {
//                  //hide keyboard
                    Class<? extends View.OnClickListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    }

                    if (Speech.getInstance().isListening()) {
                        Speech.getInstance().stopListening();
                    } else {
                        if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
                            onRecordAudioPermissionGranted();
                        else {
                            ActivityCompat.requestPermissions(ActivityHome.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    1);
                        }
                    }
                }
            }
        });
    }

    public void openNv(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.item1:
                Intent goToDrugInteractions = new Intent(this, ActivityInterferenceStep1.class);
                startActivity(goToDrugInteractions);
                closeNv();
                break;
            case R.id.item2:
                startActivity(new Intent(ActivityHome.this, MapActivity.class));
                closeNv();
                break;
            case R.id.item3:
                Intent goToReminder = new Intent(this, ActivityReminderStep1.class);
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
                break;
            case R.id.item6:
                Intent goToErrorReport = new Intent(this, ActivityErrorReport.class);
                startActivity(goToErrorReport);
                closeNv();
                break;
            case R.id.item7:
                Intent goToAbout = new Intent(this, ActivityAbout.class);
                startActivity(goToAbout);
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
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "Share app via"));
    }

    private void filter(String str) {
        ArrayList<Drug> filterDrug = new ArrayList<>();
        for (Drug drug : drugList) {
            if (drug.getName().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(drug);
            }
        }
        adapterHome.filterList(filterDrug);
    }

    private void onRecordAudioPermissionGranted() {
        btnListen.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        try {
            Speech.getInstance().stopTextToSpeech();
            Speech.getInstance().startListening(progress, ActivityHome.this);

        } catch (SpeechRecognitionNotAvailable exc) {
            showSpeechNotSupportedDialog();

        } catch (GoogleVoiceTypingDisabledException exc) {
            showEnableGoogleVoiceTyping();
        }
    }

    @Override
    public void onStartOfSpeech() {
    }

    @Override
    public void onSpeechRmsChanged(float value) {
    }

    @Override
    public void onSpeechResult(String result) {

        btnListen.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        if (!result.isEmpty()) {
            text.setText(result);
        } else {
            Speech.getInstance().say(getString(R.string.repeat));
        }
    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        text.setText("");
        for (String partial : results) {
            text.append(partial + "");
        }
    }

    private void showSpeechNotSupportedDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(ActivityHome.this);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.speech_not_available)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    private void showEnableGoogleVoiceTyping() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.enable_google_voice_typing)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else if (time + BackPressed > System.currentTimeMillis()) {
            super.onBackPressed();
        } else
            Toast.makeText(this, "لطفا کلید برگشت را مجددا فشار دهید.", Toast.LENGTH_SHORT).show();

        BackPressed = System.currentTimeMillis();
    }

    public void showDrugs(final Context context) {
        mRecyclerView = ((ActivityHome) context).getWindow().getDecorView().findViewById(R.id.fast_scroller_recycler);
        DbHelper dbHelper = new DbHelper(context);

        drugList = dbHelper.getAllDrugs();

        //Recycler view data
        adapterHome = new AdapterAlphabetIndexFastScroll(drugList, context);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(adapterHome);

        drugList = dbHelper.getAllDrugs();

//        sort item
        Collections.sort(drugList, new Comparator<Drug>() {
            @Override
            public int compare(Drug o1, Drug o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        adapterHome = new AdapterAlphabetIndexFastScroll(drugList, context);

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < drugList.size(); i++) {
            Drug name = drugList.get(i);
            if (name == null || name.getName().isEmpty())
                continue;
            String word = name.getName().substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(adapterHome);
    }
}