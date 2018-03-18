package com.example.behnam.app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Process;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.behnam.app.adapter.AdapterAlphabetIndexFastScroll;
import com.example.behnam.app.adapter.AdapterHome;
import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.CategoryDrug;
import com.example.behnam.app.fastscroll.AlphabetItem;
import com.example.behnam.app.fastscroll.DataHelper;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.controller.AppController;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityHome extends AppCompatActivity implements SpeechDelegate {

    ImageView imgOpenNvDraw;
    AdapterAlphabetIndexFastScroll adapterHome;
    EditText etSearch;
    DrawerLayout drawerLayout;
    List<Drug> drugList = new ArrayList<>();
    DbHelper dbHelper;

    private ImageView btnListen;
    private EditText text;
    private SpeechProgressView progress;
    private ConnectivityManager connectivityManager;

    private List<AlphabetItem> mAlphabetItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setContentView(R.layout.navigation_view);

        dbHelper = new DbHelper(getApplicationContext());



//        search
        etSearch = findViewById(R.id.editTextSearchHome);
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

        imgOpenNvDraw = findViewById(R.id.btnOpenNvDraw);
        drawerLayout = findViewById(R.id.DrawerLayout);

        imgOpenNvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        if (!SessionManager.getExtrasPref(this).getBoolean("primitiveRecordsExists")) {

            AppController.getInstance().sendRequest("android/api/list", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("status")) {
                            JSONArray jsonArray = response.getJSONArray("drugs");
                            JSONObject object;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                object = jsonArray.getJSONObject(i);
                                dbHelper.addDrug(new Drug(object.getInt("id"),object.getString("name"), object.getString("brand"), object.getString("pregnancy"), object.getString("lactation"), object.getString("kids"), object.getString("seniors"), object.getString("how_to_use"), object.getString("product"), object.getString("pharmacodynamic"), object.getString("usage"), object.getString("prohibition"), object.getString("caution"), object.getString("dose_adjustment"), object.getString("complication"), object.getString("interference"), object.getString("effect_on_test"), object.getString("overdose"), object.getString("description"), object.getString("relation_with_food"), object.getInt("status"), object.getString("last_modified")));
                            }
                            SessionManager.getExtrasPref(ActivityHome.this).putExtra("primitiveRecordsExists", true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            AppController.getInstance().sendRequest("android/api/category", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("status")) {
                            Log.e("cutegory=", response.toString());
                            JSONArray jsonArray = response.getJSONArray("categories");
                            JSONObject object;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                object = jsonArray.getJSONObject(i);
                                dbHelper.addCategory(new Category(object.getString("name"), object.getInt("type")));
                            }
                            SessionManager.getExtrasPref(ActivityHome.this).putExtra("primitiveRecordsExists", true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            AppController.getInstance().sendRequest("android/api/categoryDrug", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("status")) {
                            Log.e("categoryDrug=", response.toString());
                            JSONArray jsonArray = response.getJSONArray("list");
                            JSONObject object;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                object = jsonArray.getJSONObject(i);
                                dbHelper.addCategoryDrug(new CategoryDrug(object.getInt("drug_id"), object.getInt("category_id"), object.getInt("type")));
                            }

                            SessionManager.getExtrasPref(ActivityHome.this).putExtra("primitiveRecordsExists", true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

        RecyclerView mRecyclerView = findViewById(R.id.fast_scroller_recycler);

        //Recycler view data
        DbHelper dbHelper = new DbHelper(this);
        drugList = dbHelper.getAllDrugs();

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < drugList.size(); i++) {
            Drug name = drugList.get(i);
            if (name == null || name.getName().isEmpty())
                continue;

//            Drug word = name.substring(0, 1);
            String word = name.getName().substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new AdapterAlphabetIndexFastScroll(drugList));


//
//        DbHelper dbHelper = new DbHelper(this);
//        drugList = dbHelper.getAllDrugs();
//        adapterHome = new AdapterHome(this, drugList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapterHome);


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
            btnListen = findViewById(R.id.imgVoice);
            text = findViewById(R.id.editTextSearchHome);
            progress = findViewById(R.id.progressBarHome);

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
                            Toast.makeText(ActivityHome.this, R.string.permission_required, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void openNv(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.item1:
                Intent goToDrugInteractions = new Intent(this, ActivityInterferenceStep1.class);
                startActivity(goToDrugInteractions);
                closeNv();
                break;
            case R.id.item2:
                Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
                closeNv();
                break;
            case R.id.item3:
                Intent goToReminder = new Intent(this, ActivityReminderStep1.class);
                startActivity(goToReminder);
                closeNv();
                break;
            case R.id.item4:
                shareApplication();
                break;
            case R.id.item5:
                Intent goToErrorReport = new Intent(this, ActivityErrorReport.class);
                startActivity(goToErrorReport);
                closeNv();
                break;
            case R.id.item6:
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

    //    voiceSearch

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
        //Log.d(getClass().getSimpleName(), "Speech recognition rms is now " + value +  "dB");
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
}
