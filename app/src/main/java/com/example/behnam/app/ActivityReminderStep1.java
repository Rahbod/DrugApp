package com.example.behnam.app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterReminder;
import com.example.behnam.app.database.Index;
import com.example.behnam.app.helper.DbHelper;
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

public class ActivityReminderStep1 extends AppCompatActivity implements SpeechDelegate {

    private AdapterReminder adapterReminder;
    private List<Index> drugList = new ArrayList<>();
    private ImageView btnListen;
    private EditText text;
    private SpeechProgressView progress;
    private ConnectivityManager connectivityManager;
    public static Activity activityFinish;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_step_1);

        activityFinish =this;
        text = findViewById(R.id.editTextSearch);
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

        //        search
        final ImageView searchIcon = findViewById(R.id.searchIcon);
        final ImageView closeIcon = findViewById(R.id.closeIcon);
        searchIcon.setVisibility(View.VISIBLE);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String strText = text.getText().toString();
                if (strText.matches("")) {
                    closeIcon.setVisibility(View.INVISIBLE);
                    searchIcon.setVisibility(View.VISIBLE);
                } else {
                    searchIcon.setVisibility(View.INVISIBLE);
                    closeIcon.setVisibility(View.VISIBLE);
                    closeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            text.setText("");
                            searchIcon.setVisibility(View.VISIBLE);
                            closeIcon.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                filter(s.toString());
            }
        });

        DbHelper dbHelper = new DbHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recCategoryList);
        drugList = dbHelper.getAllDrugsNotInReminder();
        
//        sort item
        Collections.sort(drugList, new Comparator<Index>() {
            @Override
            public int compare(Index o1, Index o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        adapterReminder = new AdapterReminder(this, drugList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setAdapter(adapterReminder);
        recyclerView.setLayoutManager(layoutManager);


            btnListen = findViewById(R.id.imgVoice);
            progress = findViewById(R.id.progressBarHome);
            Speech.init(this, getPackageName());
            btnListen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //hide keyboard
                    Class<? extends View.OnClickListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    }

                    //voiceSearch
                    final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
                        final Dialog dialog = new Dialog(ActivityReminderStep1.this);
                        View viewDialog = LayoutInflater.from(ActivityReminderStep1.this).inflate(R.layout.massage_dialog, null);
                        dialog.setContentView(viewDialog);
                        LinearLayout linDialogMassage = viewDialog.findViewById(R.id.linDialogMassage);
                        linDialogMassage.setVisibility(View.VISIBLE);
                        TextView txt =viewDialog.findViewById(R.id.txt);
                        txt.setText(R.string.enable_wifi);
                        Button btnOk = viewDialog.findViewById(R.id.btnOk);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (wifiManager != null)
                                    wifiManager.setWifiEnabled(true);

                                if (Speech.getInstance().isListening()) {
                                    Speech.getInstance().stopListening();
                                } else {
                                    if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
                                        onRecordAudioPermissionGranted();
                                    else {
                                        ActivityCompat.requestPermissions(ActivityReminderStep1.this,
                                                new String[]{Manifest.permission.RECORD_AUDIO},
                                                1);
                                    }
                                }
                            }
                        });
                        dialog.show();
                        Button btnCancel = viewDialog.findViewById(R.id.btnCancel);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        if (Speech.getInstance().isListening()) {
                            Speech.getInstance().stopListening();
                        } else {
                            if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
                                onRecordAudioPermissionGranted();
                            else {
                                ActivityCompat.requestPermissions(ActivityReminderStep1.this,
                                        new String[]{Manifest.permission.RECORD_AUDIO},
                                        1);
                            }
                        }
                    }
                }
            });
    }

    private void filter(String str) {
        ArrayList<Index> filterDrug2 = new ArrayList<>();
        for (Index index : drugList) {
            if (index.getName().toLowerCase().contains(str.toLowerCase())) {
                filterDrug2.add(index);
            }else if (index.getFa_name().toLowerCase().contains(str.toLowerCase())) {
                filterDrug2.add(index);
            } else if (index.getBrand().toLowerCase().contains(str.toLowerCase())) {
                filterDrug2.add(index);
            }
        }
        adapterReminder.filterList(filterDrug2);
    }

    private void onRecordAudioPermissionGranted() {
        btnListen.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        try {
            Speech.getInstance().stopTextToSpeech();
            Speech.getInstance().startListening(progress, ActivityReminderStep1.this);

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
            text.append(partial + " ");
        }
    }

    private void showSpeechNotSupportedDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(ActivityReminderStep1.this);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        Speech.getInstance().shutdown();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Speech.init(this, getPackageName());
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
                onBackPressed();
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
                Intent goToAbout = new Intent(this, ActivityAbout.class);
                startActivity(goToAbout);
                closeNv();
                break;
            case R.id.item8:
                Intent intentVegetalDrug = new Intent(this, ActivityDrug.class);
                startActivity(intentVegetalDrug);
                closeNv();
                break;
            case R.id.item9:
                Intent intentHome = new Intent(this, ActivityHome.class);
                startActivity(intentHome);
                closeNv();
                break;
            case R.id.item10:
                Intent intentSearch = new Intent(this, ActivityDrug.class);
                intentSearch.putExtra("search", "search");
                startActivity(intentSearch);
                closeNv();
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
}