package com.example.behnam.app;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.os.Process;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterCategories;
import com.example.behnam.app.database.Category;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityCategories extends AppCompatActivity implements SpeechDelegate {

    private RecyclerView recyclerView;
    private TextView txtTitle, text;
    private List<Category> list;
    private AdapterCategories adapter;
    private ConnectivityManager connectivityManager;
    private SpeechProgressView progress;
    private ImageView btnListen;
    private DbHelper dbHelper;
    int type;
    private Speech speechInstance;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        if (SessionManager.getExtrasPref(this).getString("idList") != null) {
            SessionManager.getExtrasPref(this).remove("idList");
        }

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

                                    if (speechInstance.isListening()) {
                                        speechInstance.stopListening();
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
                    if (speechInstance.isListening()) {
                        speechInstance.stopListening();
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
            speechInstance.stopTextToSpeech();
            speechInstance.startListening(progress, ActivityCategories.this);

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
            speechInstance.say(getString(R.string.repeat));
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
                Intent intentDrug = new Intent(this, ActivityHome.class);
                startActivity(intentDrug);
                closeNv();
                break;
            case R.id.item10:
                Intent intentSearch = new Intent(this, ActivityDrug.class);
                intentSearch.putExtra("search", "search");
                startActivity(intentSearch);
                closeNv();
                break;
            case R.id.item11:
                if (type == 0) {
                    Intent intentPharma = new Intent(this, ActivityCategories.class);
                    intentPharma.putExtra("type", 1);
                    startActivity(intentPharma);
                    closeNv();
                } else drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.item12:
                if (type == 1) {
                    Intent intentHealing = new Intent(this, ActivityCategories.class);
                    intentHealing.putExtra("type", 0);
                    startActivity(intentHealing);
                    closeNv();
                } else drawerLayout.closeDrawer(Gravity.RIGHT);
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