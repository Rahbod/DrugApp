package com.example.behnam.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.os.Process;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterCategories;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.helper.Components;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityCategories extends AppCompatActivity implements SpeechDelegate {

    RecyclerView recyclerView;
    TextView txtTitle, text;
    List<Category> list;
    AdapterCategories adapter;
    private ConnectivityManager connectivityManager;
    private SpeechProgressView progress;
    private ImageView btnListen;
    DbHelper dbHelper;
    int type;
    Speech speechInstance;
    String TAG = "iiii";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Log.e(TAG, "onCreate: " );

        if (SessionManager.getExtrasPref(this).getString("idList") != null) {
            SessionManager.getExtrasPref(this).remove("idList");
        }

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        text = findViewById(R.id.editTextSearch);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        dbHelper = new DbHelper(this);
        list = dbHelper.getCategories(type, "parent_id = 0");
        recyclerView = findViewById(R.id.recCategoryList);
        adapter = new AdapterCategories(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        txtTitle = findViewById(R.id.txtTitle);
        if (type == 0)
            txtTitle.setText("طبقه بندی درمانی");
        else txtTitle.setText("طبقه بندی بیماری");

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

        //        sort item
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        //voice search
        progress = findViewById(R.id.progressBar);
        btnListen = findViewById(R.id.imgVoice);

        speechInstance = Speech.init(this, getPackageName());
        Log.e("qqqq", "onCreate: init1" );
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCategories.this);
                    builder.setMessage(R.string.enable_wifi).setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (wifiManager != null)
                                        wifiManager.setWifiEnabled(true);

                                    if (Speech.getInstance().isListening()) {
                                        Speech.getInstance().stopListening();
                                    } else {
                                        if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
                                            onRecordAudioPermissionGranted();
                                        else {
                                            ActivityCompat.requestPermissions(ActivityCategories.this,
                                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                                    1);
                                        }
                                    }
                                }
                            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                            .show();
                } else {
                    if (Speech.getInstance().isListening()) {
                        Speech.getInstance().stopListening();
                    } else {
                        if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
                            onRecordAudioPermissionGranted();
                        else {
                            ActivityCompat.requestPermissions(ActivityCategories.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    1);
                        }
                    }
                }
            }
        });
    }

    private void onRecordAudioPermissionGranted() {
        btnListen.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        try {
            Speech.getInstance().stopTextToSpeech();
            Speech.getInstance().startListening(progress, ActivityCategories.this);

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
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(ActivityCategories.this);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.speech_not_available)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    private void showEnableGoogleVoiceTyping() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
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

    private void filter(String str) {
        ArrayList<Category> filterDrug = new ArrayList<>();
        for (Category category : list) {
            if (category.getName().toLowerCase().contains(str.toLowerCase()))
                filterDrug.add(category);

        }
        adapter.filterList(filterDrug);
    }

    @Override
    public void onStartOfSpeech() {

    }

    @Override
    public void onSpeechRmsChanged(float value) {

    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        text.setText("");
        for (String partial : results) {
            text.append(partial + " ");
        }
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
    public void onBackPressed() {
        if (SessionManager.getExtrasPref(this).getString("idList").isEmpty())
            super.onBackPressed();
        String strList = SessionManager.getExtrasPref(this).getString("idList");
        try {
            JSONArray parentListId = new JSONArray(strList);
            if (parentListId.length() > 0) {
                int parentID = parentListId.getInt(parentListId.length() - 1);
                parentListId = Components.jsonArrayRemove(parentListId, parentListId.length() - 1);
                SessionManager.getExtrasPref(this).putExtra("idList", parentListId.toString());
                list = dbHelper.getCategories(type, "parent_id=" + parentID);
                adapter = new AdapterCategories(this, list);
                recyclerView.setAdapter(adapter);
            } else {
                super.onBackPressed();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // sort list
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public void getDataToList() {
        String strList = SessionManager.getExtrasPref(this).getString("idList");
        try {
            JSONArray jsonArray = new JSONArray(strList);
            int id = jsonArray.getInt(jsonArray.length() - 1);
            list = dbHelper.getCategories(type, "parent_id=" + id);
            jsonArray = Components.jsonArrayRemove(jsonArray, jsonArray.length() - 1);
            SessionManager.getExtrasPref(this).putExtra("idList", jsonArray.toString());
            adapter = new AdapterCategories(this, list);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // sort list
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    @Override
    protected void onResume() {
        speechInstance = Speech.init(this, getPackageName());
        getDataToList();
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (speechInstance != null) {
            speechInstance.shutdown();
            speechInstance.stopListening();
            speechInstance = null;
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (speechInstance != null) {
            speechInstance.shutdown();
            speechInstance.stopListening();
            speechInstance = null;
        }
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        speechInstance = Speech.init(this, getPackageName());
    }
}