package com.example.behnam.app;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.behnam.app.adapter.AdapterInterferenceStep2;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.ui.SpeechProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityInterferenceStep2 extends AppCompatActivity implements SpeechDelegate {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private ImageView btnListen, btnBack;
    private EditText text;
    private TextView txtName, txtBrand;
    private SpeechProgressView progress;
    private ConnectivityManager connectivityManager;
    private List<Drug> drugList;
    private AdapterInterferenceStep2 adapter;
    private FloatingActionButton floatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interference_step_2);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String strName = bundle.getString("name");
        String strBrand = bundle.getString("brand");

        txtName = findViewById(R.id.txtName);
        txtBrand = findViewById(R.id.txtBrand);

        txtName.setText(strName);
        txtBrand.setText(strBrand);

        floatButton = findViewById(R.id.floatButton);

        //        search
        etSearch = findViewById(R.id.editTextDrugInterferenceStep2);
        etSearch.addTextChangedListener(new TextWatcher() {
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

        SessionManager.getExtrasPref(this).remove("selectedIDs");

        final DbHelper dbHelper = new DbHelper(this);

        drugList = dbHelper.getAllDrugs();

        // Set linear recyclerView adapter
        recyclerView = findViewById(R.id.recDrugInteraction2);
        adapter = new AdapterInterferenceStep2(this, drugList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatButton.getVisibility() == View.VISIBLE) {
                    floatButton.hide();
                } else if (dy < 0 && floatButton.getVisibility() != View.VISIBLE) {
                    floatButton.show();
                }
            }
        });

//        floatingButtonClick
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedIDs = SessionManager.getExtrasPref(ActivityInterferenceStep2.this).getString("selectedIDs");
                int mainID = SessionManager.getExtrasPref(ActivityInterferenceStep2.this).getInt("mainID");
                JSONArray temp = null;
                try {
                    temp = new JSONArray(selectedIDs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String selectedIDsArr;
                try {
                    selectedIDsArr = temp.join(",");
                    JSONObject conflicts = dbHelper.checkInterference(mainID, selectedIDsArr);
                    SessionManager.getExtrasPref(ActivityInterferenceStep2.this).putExtra("conflicts", String.valueOf(conflicts));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ActivityInterferenceStep2.this, ActivityInterferenceStep3.class);
                startActivity(intent);
            }
        });

        //        voiceSearch

        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            btnListen = findViewById(R.id.imgVoiceDrugInterferenceStep2);
            text = findViewById(R.id.editTextDrugInterferenceStep2);
            progress = findViewById(R.id.progressDrugInterferenceStep2);

            Speech.init(this, getPackageName());

            btnListen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Speech.getInstance().isListening()) {
                        Speech.getInstance().stopListening();
                    } else {
                        if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
                            onRecordAudioPermissionGranted();
                        else
                            Toast.makeText(ActivityInterferenceStep2.this, R.string.permission_required, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void filter(String str) {
        ArrayList<Drug> filterDrug = new ArrayList<>();
        for (Drug drug : drugList) {
            if (drug.getName().toLowerCase().contains(str.toLowerCase())) {
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
            Speech.getInstance().startListening(progress, ActivityInterferenceStep2.this);

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
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(ActivityInterferenceStep2.this);
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
}