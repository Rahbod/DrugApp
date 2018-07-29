package com.example.behnam.app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterDrugByCategory;
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

public class ActivityVegetalDrug extends AppCompatActivity implements SpeechDelegate {

    private TextView text, txtTitle;
    private List<Drug> list;
    private AdapterDrugByCategory adapter;
    private SpeechProgressView progress;
    private ConnectivityManager connectivityManager;
    private ImageView btnListen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        //set Text Title
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("گیاهان دارویی");

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setBackground(getResources().getDrawable(R.drawable.background_focus_vegetal));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.greenVegetalStatus));
        }

        text = findViewById(R.id.editTextSearch);
        btnListen = findViewById(R.id.imgVoice);

        DbHelper dbHelper = new DbHelper(this);
        list = dbHelper.getVegetalDrug();
        RecyclerView recyclerView = findViewById(R.id.recCategoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterDrugByCategory(this, list);
        recyclerView.setAdapter(adapter);

        //        sort item
        Collections.sort(list, new Comparator<Drug>() {
            @Override
            public int compare(Drug o1, Drug o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        // Change Color Header
        RelativeLayout rel1 = findViewById(R.id.actionBarFavorite);
        RelativeLayout rel2 = findViewById(R.id.relHome1);
        rel1.setBackgroundColor(getResources().getColor(R.color.greenVegetal));
        rel2.setBackgroundColor(getResources().getColor(R.color.greenVegetal));

        // search
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

        btnListen = findViewById(R.id.imgVoice);
        progress = findViewById(R.id.progressBar);
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
                    View viewDialogMassage = LayoutInflater.from(ActivityVegetalDrug.this).inflate(R.layout.massage_dialog, null);
                    final Dialog dialog = new Dialog(ActivityVegetalDrug.this);
                    dialog.setContentView(viewDialogMassage);
                    LinearLayout linMassageDialog = viewDialogMassage.findViewById(R.id.linDialogMassage);
                    linMassageDialog.setVisibility(View.VISIBLE);
                    TextView txt = viewDialogMassage.findViewById(R.id.txt);
                    txt.setText(R.string.enable_wifi);
                    Button btnOk = viewDialogMassage.findViewById(R.id.btnOk);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (wifiManager != null)
                                wifiManager.setWifiEnabled(true);

                            else if (Speech.getInstance().isListening()) {
                                Speech.getInstance().stopListening();
                            } else {
                                if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
                                    onRecordAudioPermissionGranted();
                                else {
                                    ActivityCompat.requestPermissions(ActivityVegetalDrug.this,
                                            new String[]{Manifest.permission.RECORD_AUDIO},
                                            1);
                                }
                            }
                        }
                    });
                    Button btnCancel = viewDialogMassage.findViewById(R.id.btnCancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    if (Speech.getInstance().isListening()) {
                        Speech.getInstance().stopListening();
                    } else {
                        if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
                            onRecordAudioPermissionGranted();
                        else {
                            ActivityCompat.requestPermissions(ActivityVegetalDrug.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                        }
                    }
                }
            }
        });
    }

    private void filter(String str) {
        ArrayList<Drug> filterDrug = new ArrayList<>();
        for (Drug drug : list) {
            if (drug.getName().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(drug);
            } else if (drug.getNamePersian().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(drug);
            } else if (drug.getBrand().toLowerCase().contains(str.toLowerCase())) {
                filterDrug.add(drug);
            }
        }
        adapter.filterList(filterDrug);
    }

    private void onRecordAudioPermissionGranted() {
        btnListen.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        try {
            Speech.getInstance().stopTextToSpeech();
            Speech.getInstance().startListening(progress, ActivityVegetalDrug.this);

        } catch (SpeechRecognitionNotAvailable exc) {
            showSpeechNotSupportedDialog();

        } catch (GoogleVoiceTypingDisabledException exc) {
            showEnableGoogleVoiceTyping();
        }
    }

    private void showSpeechNotSupportedDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(ActivityVegetalDrug.this);
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
    public void onStartOfSpeech() {

    }

    @Override
    public void onSpeechRmsChanged(float value) {

    }

    @Override
    public void onSpeechPartialResults(List<String> results) {

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
}
