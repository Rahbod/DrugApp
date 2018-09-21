package com.example.behnam.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.behnam.app.adapter.AdapterSearch;
import com.example.behnam.app.database.Index;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;
import com.example.behnam.app.map.MapActivity;

import java.io.File;
import java.util.List;

public class ActivitySearch extends AppCompatActivity {
    //    private ImageView btnListen;
//    private SpeechProgressView progress;
//    private ConnectivityManager connectivityManager;
//    private Speech speechInstance;
    private List<Index> list;
    private EditText text;
    private AdapterSearch adapter;
    private RecyclerView recyclerView;
    private DbHelper dbHelper;
    private TextView txtName;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Set user name and mobile
        NavigationView navigationView = findViewById(R.id.searchNavView);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.txtNameNav);
        navUserName.setText(SessionManager.getExtrasPref(this).getString("name"));
        TextView navUserMobile = headerView.findViewById(R.id.txtMobileNav);
        navUserMobile.setText(SessionManager.getExtrasPref(this).getString("mobile"));

        // open navigation
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

        // back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //list drug
        txtName = findViewById(R.id.txtName);
        dbHelper = new DbHelper(this);
        list = dbHelper.getSearchItem(getIntent().getStringExtra("item"));
        adapter = new AdapterSearch(this, list);
        recyclerView = findViewById(R.id.recCategoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        if (list.isEmpty()) {
            txtName.setText("هیچ دارویی با این نام وجود ندارد.");
            txtName.setVisibility(View.VISIBLE);
        }

        // search
        text = findViewById(R.id.editTextSearch);
        text.setText(getIntent().getStringExtra("item"));
        final ImageView searchIcon = findViewById(R.id.searchIcon);
        final ImageView closeIcon = findViewById(R.id.closeIcon);
        closeIcon.setVisibility(View.VISIBLE);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().equals("")) {
                    //hide keyboard
                    Class<? extends View.OnClickListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    }
                    list.clear();
                    txtName.setVisibility(View.VISIBLE);
                    txtName.setText("شما دارویی جستجو نکرده اید.");
                } else {
                    //hide keyboard
                    Class<? extends View.OnClickListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    }
                    txtName.setVisibility(View.INVISIBLE);
                    list = dbHelper.getSearchItem(text.getText().toString());
                    adapter = new AdapterSearch(ActivitySearch.this, list);
                    recyclerView.setAdapter(adapter);
                    if (list.isEmpty()) {
                        txtName.setVisibility(View.VISIBLE);
                        txtName.setText("هیچ دارویی با این نام وجود ندارد.");
                    }
                }
            }
        });
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
                closeIcon.setVisibility(View.INVISIBLE);
            }
        });
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
                } else {
                    closeIcon.setVisibility(View.VISIBLE);
                }
            }
        });

        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (text.getText().toString().equals("")) {
                    //hide keyboard
                    Class<? extends TextView.OnEditorActionListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    }
                    list.clear();
                    txtName.setVisibility(View.VISIBLE);
                    txtName.setText("شما دارویی جستجو نکرده اید.");
                } else {
                    //hide keyboard
                    Class<? extends TextView.OnEditorActionListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    }
                    txtName.setVisibility(View.INVISIBLE);
                    list = dbHelper.getSearchItem(text.getText().toString());
                    adapter = new AdapterSearch(ActivitySearch.this, list);
                    recyclerView.setAdapter(adapter);
                    if (list.isEmpty()) {
                        txtName.setVisibility(View.VISIBLE);
                        txtName.setText("هیچ دارویی با این نام وجود ندارد.");
                    }
                }
                return true;
            }
        });

//        btnListen = findViewById(R.id.imgVoice);
//        progress = findViewById(R.id.progressBar);
//        speechInstance = Speech.init(this, getPackageName());
//        btnListen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //hide keyboard
//                Class<? extends View.OnClickListener> view = this.getClass();
//                if (view != null) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    assert imm != null;
//                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
//                }
//
//                //voiceSearch
//                View viewDialogMassage = LayoutInflater.from(ActivitySearch.this).inflate(R.layout.massage_dialog, null);
//                final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
//                    final Dialog dialog = new Dialog(ActivitySearch.this);
//                    dialog.setContentView(viewDialogMassage);
//                    LinearLayout linMassageDialog = viewDialogMassage.findViewById(R.id.linDialogMassage);
//                    linMassageDialog.setVisibility(View.VISIBLE);
//                    TextView txt = viewDialogMassage.findViewById(R.id.txt);
//                    txt.setText(R.string.enable_wifi);
//                    Button btnOk = viewDialogMassage.findViewById(R.id.btnOk);
//                    btnOk.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                            if (wifiManager != null)
//                                wifiManager.setWifiEnabled(true);
//
//                            else if (speechInstance.isListening()) {
//                                speechInstance.stopListening();
//                            } else {
//                                if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
//                                    onRecordAudioPermissionGranted();
//                                else {
//                                    ActivityCompat.requestPermissions(ActivitySearch.this,
//                                            new String[]{Manifest.permission.RECORD_AUDIO},
//                                            1);
//                                }
//                            }
//                        }
//                    });
//                    Button btnCancel = viewDialogMassage.findViewById(R.id.btnCancel);
//                    btnCancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                } else {
//                    if (speechInstance.isListening()) {
//                        speechInstance.stopListening();
//                    } else {
//                        if (checkPermission(Manifest.permission.RECORD_AUDIO, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED)
//                            onRecordAudioPermissionGranted();
//                        else {
//                            ActivityCompat.requestPermissions(ActivitySearch.this,
//                                    new String[]{Manifest.permission.RECORD_AUDIO}, 100);
//                        }
//                    }
//                }
//            }
//        });
    }

//    private void onRecordAudioPermissionGranted() {
//        btnListen.setVisibility(View.GONE);
//        progress.setVisibility(View.VISIBLE);
//        try {
//            speechInstance.stopTextToSpeech();
//            speechInstance.startListening(progress, ActivitySearch.this);
//
//        } catch (SpeechRecognitionNotAvailable exc) {
//            showSpeechNotSupportedDialog();
//
//        } catch (GoogleVoiceTypingDisabledException exc) {
//            showEnableGoogleVoiceTyping();
//        }
//    }
//
//    @Override
//    public void onStartOfSpeech() {
//    }
//
//    @Override
//    public void onSpeechRmsChanged(float value) {
//    }
//
//    @Override
//    public void onSpeechResult(String result) {
//        btnListen.setVisibility(View.VISIBLE);
//        progress.setVisibility(View.GONE);
//        if (!result.isEmpty()) {
//            text.setText(result);
//            list = dbHelper.getSearchItem(text.getText().toString());
//            adapter = new AdapterSearch(ActivitySearch.this, list);
//            recyclerView.setAdapter(adapter);
//        } else {
//            speechInstance.say(getString(R.string.repeat));
//        }
//    }
//
//    @Override
//    public void onSpeechPartialResults(List<String> results) {
//        text.setText("");
//        for (String partial : results) {
//            text.append(partial + "");
//        }
//    }
//
//    private void showSpeechNotSupportedDialog() {
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(ActivitySearch.this);
//                        break;
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        break;
//                }
//            }
//        };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(R.string.speech_not_available)
//                .setCancelable(false)
//                .setPositiveButton(R.string.yes, dialogClickListener)
//                .setNegativeButton(R.string.no, dialogClickListener)
//                .show();
//    }
//
//    private void showEnableGoogleVoiceTyping() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(R.string.enable_google_voice_typing)
//                .setCancelable(false)
//                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // do nothing
//                    }
//                })
//                .show();
//    }
//
//    @Override
//    protected void onStop() {
//        if (speechInstance != null) {
//            progress.setVisibility(View.INVISIBLE);
//            btnListen.setVisibility(View.VISIBLE);
//            speechInstance.shutdown();
//            speechInstance.stopListening();
//            speechInstance = null;
//        }
//        super.onStop();
//    }
//
//    @Override
//    protected void onResume() {
//        speechInstance = Speech.init(this, getPackageName());
//        super.onResume();
//    }

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
                Intent intentAbout = new Intent(this, ActivityAbout.class);
                intentAbout.putExtra("type", "about");
                startActivity(intentAbout);
                closeNv();
                break;
            case R.id.item8:
                Intent intentVegetalDrug = new Intent(this, ActivityDrug.class);
                startActivity(intentVegetalDrug);
                closeNv();
                break;
            case R.id.item9:
                drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.item10:
                Intent intentRules = new Intent(this, ActivityAbout.class);
                intentRules.putExtra("type", "rules");
                startActivity(intentRules);
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            super.onBackPressed();
    }
}