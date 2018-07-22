package com.example.behnam.app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.ui.SpeechProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityReminderStep1 extends AppCompatActivity implements SpeechDelegate {

    AdapterReminder adapterReminder;
    List<Drug> drugList = new ArrayList<>();
    ImageView btnBack;

    private ImageView btnListen;
    private EditText text;
    private SpeechProgressView progress;
    private Boolean speechInitialized = false;
    private ConnectivityManager connectivityManager;
    public static Activity activityFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_step_1);

        activityFinish =this;

        text = findViewById(R.id.editTextSearch);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        Collections.sort(drugList, new Comparator<Drug>() {
            @Override
            public int compare(Drug o1, Drug o2) {
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
            speechInitialized = true;

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
        ArrayList<Drug> filterDrug = new ArrayList<>();
        for (Drug drug : drugList) {
            if (drug.getName().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(drug);
            }else if (drug.getNamePersian().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(drug);
            } else if (drug.getBrand().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(drug);
            }
        }
        adapterReminder.filterList(filterDrug);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (speechInitialized)
//            Speech.getInstance().shutdown();
//    }

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

}
