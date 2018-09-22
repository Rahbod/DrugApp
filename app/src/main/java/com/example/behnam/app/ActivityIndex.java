package com.example.behnam.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.behnam.app.adapter.AdapterDropList;
import com.example.behnam.app.controller.AppController;
import com.example.behnam.app.database.DropList;
import com.example.behnam.app.helper.DbHelper;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityIndex extends AppCompatActivity {
    private static long BackPressed;
    private EditText text;

    // Register properties
    private EditText etName, etNumberMobile, etField, etEmail;
    private TextView txtGrade, txtDepartment;
    private List<Integer> gradeID, departmentID;
    private List<DropList> gradeList, departmentList;
    public static Activity activityIndex = null;

//    private Speech speechInstance;
//    private SpeechProgressView progress;
//    private ImageView btnListen;
//    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityIndex = this;

        if (!SessionManager.getExtrasPref(this).getBoolean("selectedVersion")) {
            setContentView(R.layout.activity_regester);
            onCreateRegister();
        } else {
            setContentView(R.layout.activity_index);
            onCreateIndex();
        }
    }

    private void onCreateIndex() {
//        Button btn = findViewById(R.id.map);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ActivityIndex.this, ActivityMap.class);
//                startActivity(intent);
//            }
//        });




        checkUpdateDatabase(System.currentTimeMillis() / 1000);
        text = findViewById(R.id.editTextSearch);
        Button btnActive = findViewById(R.id.active);
        if (SessionManager.getExtrasPref(this).getInt("activated") == 1)
            btnActive.setVisibility(View.INVISIBLE);
        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                JSONObject params = new JSONObject();
                try {
                    params.put("id", idNumber);
                    AppController.getInstance(ActivityIndex.this).sendRequest("android/api/activate", params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String url = response.getString("url");
                                int id = response.getInt("id");
                                Intent intent = new Intent(ActivityIndex.this, PaymentActivity.class);
                                intent.putExtra("action", "activateDownload");
                                intent.putExtra("url", url);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //finish splash screen
        if (ActivitySplashScreen.activitySplashScreen != null)
            ActivitySplashScreen.activitySplashScreen.finish();

        // search
        text.setText("");
        final ImageView searchIcon = findViewById(R.id.searchIcon);
        final ImageView closeIcon = findViewById(R.id.closeIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text.getText().toString().equals("")) {
                    Intent intent = new Intent(ActivityIndex.this, ActivitySearch.class);
                    intent.putExtra("item", text.getText().toString());
                    startActivity(intent);
                    text.setText("");
                } else {
                    //hide keyboard
                    Class<? extends View.OnClickListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(ActivityIndex.this.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    }
                }
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
                    closeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            text.setText("");
                            closeIcon.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!text.getText().toString().equals("")) {
                    Intent intent = new Intent(ActivityIndex.this, ActivitySearch.class);
                    intent.putExtra("item", text.getText().toString());
                    startActivity(intent);
                    text.setText("");
                } else {
                    //hide keyboard
                    Class<? extends TextView.OnEditorActionListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
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
//                    InputMethodManager imm = (InputMethodManager) getSystemService(ActivityIndex.this.INPUT_METHOD_SERVICE);
//                    assert imm != null;
//                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
//                }
//
//                //voiceSearch
//                View viewDialogMassage = LayoutInflater.from(ActivityIndex.this).inflate(R.layout.massage_dialog, null);
//                final WifiManager wifiManager = (WifiManager) getApplicationActivityIndex.this().getSystemService(ActivityIndex.this.WIFI_SERVICE);
//                connectivityManager = (ConnectivityManager) getApplicationActivityIndex.this().getSystemService(ActivityIndex.this.CONNECTIVITY_SERVICE);
//                if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
//                    final Dialog dialog = new Dialog(ActivityIndex.this);
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
//                                    ActivityCompat.requestPermissions(ActivityIndex.this,
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
//                            ActivityCompat.requestPermissions(ActivityIndex.this,
//                                    new String[]{Manifest.permission.RECORD_AUDIO}, 100);
//                        }
//                    }
//                }
//            }
//        });
    }


    private void checkUpdateDatabase(long now) {
        @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        long week = SessionManager.getExtrasPref(this).getLong("updateCheck") + 86400;
        if (now > week) {
            if (isConnected()) {
                //send request & save time & update database
                JSONObject params = new JSONObject();
                try {
                    params.put("id", idNumber);
                    params.put("last_sync", SessionManager.getExtrasPref(this).getLong("lastSync"));
                    AppController.getInstance(ActivityIndex.this).sendRequest("android/api/dataCheckUpdate", params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("hasUpdate")) {
                                    DbHelper dbHelper = new DbHelper(ActivityIndex.this);
                                    @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                    long last_sync = SessionManager.getExtrasPref(ActivityIndex.this).getLong("lastSync");
                                    int last_drug_id = dbHelper.getMaxID("drugs");
                                    int last_category_drug_id = dbHelper.getMaxID("category_drug");
                                    int last_category_id = dbHelper.getMaxID("categories");
                                    int last_interference_id = dbHelper.getMaxID("interference");
                                    int last_index_id = dbHelper.getMaxID("indexes");
                                    String id = idNumber + "&last_sync=" + last_sync +
                                            "&did=" + last_drug_id + "&cid=" + last_category_id + "&cdid=" + last_category_drug_id +
                                            "&intid=" + last_interference_id + "&indid=" + last_index_id;
                                    Intent intent = new Intent(ActivityIndex.this, ActivitySplashScreen.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("action", "getUpdatedData");
                                    intent.putExtra("referer", "update");
                                    startActivity(intent);
                                } else {
                                    long now = System.currentTimeMillis() / 1000;
                                    SessionManager.getExtrasPref(ActivityIndex.this).putExtra("updateCheck", now);
                                    SessionManager.getExtrasPref(ActivityIndex.this).putExtra("lastSync", now);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SessionManager.getExtrasPref(this).remove("updateCheck");
                SessionManager.getExtrasPref(this).putExtra("updateCheck", System.currentTimeMillis() / 1000);
            }
        }
    }

    public void openActivity(View view) {
        switch (findViewById(view.getId()).getId()) {
            case R.id.interference:
                Intent intent = new Intent(ActivityIndex.this, ActivityListDrugInterference.class);
                startActivity(intent);
                break;
            case R.id.rules:
                Intent intentSearch = new Intent(ActivityIndex.this, ActivityAbout.class);
                intentSearch.putExtra("type", "rules");
                startActivity(intentSearch);
                break;
            case R.id.vegetalDrug:
                Intent intentVegetalDrug = new Intent(ActivityIndex.this, ActivityDrug.class);
                startActivity(intentVegetalDrug);
                break;
            case R.id.drg:
                Intent intentDrug = new Intent(ActivityIndex.this, ActivityHome.class);
                startActivity(intentDrug);
                break;
//            case R.id.healing:
//                Intent intentHealing = new Intent(ActivityIndex.this, ActivityCategories.class);
//                intentHealing.putExtra("type", 0);
//                startActivity(intentHealing);
//                break;
            case R.id.pharma:
                Intent intentPharma = new Intent(ActivityIndex.this, ActivityCategories.class);
                intentPharma.putExtra("type", 1);
                startActivity(intentPharma);
                break;
//            case R.id.location:
//                Intent intentLocation = new Intent(ActivityIndex.this, MapActivity.class);
//                startActivity(intentLocation);
//                break;
            case R.id.reminder:
                Intent intentReminder = new Intent(ActivityIndex.this, ActivityReminderList.class);
                startActivity(intentReminder);
                break;
            case R.id.favorite:
                Intent intentFavorite = new Intent(ActivityIndex.this, ActivityFavorite.class);
                startActivity(intentFavorite);
                break;
            case R.id.share:
                shareApplication();
                break;
            case R.id.reportError:
                Intent intentReportError = new Intent(ActivityIndex.this, ActivityErrorReport.class);
                startActivity(intentReportError);
                break;
            case R.id.aboutUs:
                Intent intentAboutUs = new Intent(ActivityIndex.this, ActivityAbout.class);
                intentAboutUs.putExtra("type", "about");
                startActivity(intentAboutUs);
                break;

        }
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
        if (2000 + BackPressed > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "لطفا کلید برگشت را مجددا فشار دهید.", Toast.LENGTH_SHORT).show();
            BackPressed = System.currentTimeMillis();
        }
    }

    //    private void onRecordAudioPermissionGranted() {
//        btnListen.setVisibility(View.GONE);
//        progress.setVisibility(View.VISIBLE);
//        try {
//            speechInstance.stopTextToSpeech();
//            speechInstance.startListening(progress, ActivityIndex.this);
//
//        } catch (SpeechRecognitionNotAvailable exc) {
//            showSpeechNotSupportedDialog();
//
//        } catch (GoogleVoiceTypingDisabledException exc) {
//            showEnableGoogleVoiceTyping();
//        }
//    }
//
//    private void showSpeechNotSupportedDialog() {
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(ActivityIndex.this);
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
//    public void onStartOfSpeech() {
//
//    }
//
//    @Override
//    public void onSpeechRmsChanged(float value) {
//
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
//    @Override
//    public void onSpeechResult(String result) {
//        btnListen.setVisibility(View.VISIBLE);
//        progress.setVisibility(View.GONE);
//        if (!result.isEmpty()) {
//            text.setText(result);
//            Intent intent = new Intent(ActivityIndex.this, ActivitySearch.class);
//            intent.putExtra("item", text.getText().toString());
//            startActivity(intent);
//        } else {
//            speechInstance.say(getString(R.string.repeat));
//        }
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
    @Override
    protected void onResume() {
//        text = findViewById(R.id.editTextSearch);
//        speechInstance = Speech.init(this, getPackageName());
//        if (SessionManager.getExtrasPref(this).getBoolean("selectedVersion"))
//            text.setText("");
        super.onResume();
    }

    private void onCreateRegister() {
        etNumberMobile = findViewById(R.id.numberMobile);
        etName = findViewById(R.id.name);
        etField = findViewById(R.id.field);
        etEmail = findViewById(R.id.email);
        txtGrade = findViewById(R.id.grade);
        TextView txtCheckBox = findViewById(R.id.txtCheckBox);
        txtDepartment = findViewById(R.id.department);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        final Button btnSave = findViewById(R.id.save);
        gradeID = new ArrayList<>();
        departmentID = new ArrayList<>();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    btnSave.setEnabled(true);
                    btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue));
                    btnSave.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btnSave.setEnabled(false);
                    btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue_disable));
                    btnSave.setTextColor(getResources().getColor(R.color.Gray2));
                }
            }
        });

        txtCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ActivityIndex.this);
                View view = LayoutInflater.from(ActivityIndex.this).inflate(R.layout.summary_dialog, null);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(view);
                RecyclerView recyclerView = view.findViewById(R.id.recSummaryDialog);
                recyclerView.setVisibility(View.INVISIBLE);
                TextView txtTitle = view.findViewById(R.id.txtTitle);
                TextView txtMessage = view.findViewById(R.id.txtMessage);
                txtMessage.setVisibility(View.VISIBLE);
                txtMessage.setText("متن قوانین و مقررات");
                txtTitle.setText("قوانین و مقررات");
                ImageView imgClose = view.findViewById(R.id.imgCloseDialog);
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        txtDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDepartment(txtDepartment);
            }
        });
        txtGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGrade(txtGrade);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    @SuppressLint("HardwareIds") String idNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    if (etNumberMobile.getText().toString().isEmpty())
                        Toast.makeText(ActivityIndex.this, "لطفا شماره تلفن خود را وارد کنید", Toast.LENGTH_LONG).show();
                    else if (!checkMobileNumber(etNumberMobile.getText().toString()))
                        Toast.makeText(ActivityIndex.this, "شماره تلفن وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
                    else if (etName.getText().toString().trim().isEmpty())
                        Toast.makeText(ActivityIndex.this, "لطفا نام خود را وارد کنید", Toast.LENGTH_SHORT).show();
                    else if (etField.getText().toString().trim().isEmpty())
                        Toast.makeText(ActivityIndex.this, "لطفا رشته تحصیلی خود را وارد کنید", Toast.LENGTH_SHORT).show();
                    else if (gradeID.isEmpty())
                        Toast.makeText(ActivityIndex.this, "لطفا مقطع خود را وارد کنید", Toast.LENGTH_SHORT).show();
                    else if (departmentID.isEmpty())
                        Toast.makeText(ActivityIndex.this, "لطفا گروه تحصیلی خود را وارد کنید", Toast.LENGTH_SHORT).show();
                    else {
                        btnSave.setEnabled(false);
                        btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue_disable));
                        btnSave.setTextColor(getResources().getColor(R.color.Gray2));
                        btnSave.setText("در حال ارسال اطلاعات...");
                        JSONObject object = new JSONObject();
                        JSONObject params = new JSONObject();
                        try {
                            object.put("mobile", etNumberMobile.getText().toString());
                            object.put("id", idNumber);
                            object.put("name", etName.getText().toString());
                            object.put("field", etField.getText().toString());
                            object.put("grade", gradeID.get(0));
                            object.put("department", departmentID.get(0));
                            if (!etEmail.getText().toString().trim().isEmpty())
                                object.put("email", etEmail.getText().toString());
                            params.put("User", object);
                            AppController.getInstance(ActivityIndex.this).sendRequest("android/api/register", params, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getBoolean("status")) {
                                            SessionManager.getExtrasPref(ActivityIndex.this).putExtra("activated", response.getInt("activated"));
                                            SessionManager.getExtrasPref(ActivityIndex.this).putExtra("key", response.getString("key"));
                                            SessionManager.getExtrasPref(ActivityIndex.this).putExtra("iv", response.getString("iv"));
                                            SessionManager.getExtrasPref(ActivityIndex.this).putExtra("name", response.getString("name"));
                                            SessionManager.getExtrasPref(ActivityIndex.this).putExtra("mobile", response.getString("mobile"));
                                            Intent intent = new Intent(ActivityIndex.this, ActivityCheckCode.class);
                                            startActivity(intent);
                                            btnSave.setTextColor(getResources().getColor(R.color.white));
                                            btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue));
                                            btnSave.setEnabled(true);
                                            btnSave.setText("ثبت");
                                        }
                                        else {
                                            btnSave.setTextColor(getResources().getColor(R.color.white));
                                            btnSave.setBackground(getResources().getDrawable(R.drawable.shape_button_blue));
                                            btnSave.setEnabled(true);
                                            btnSave.setText("ثبت");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else
                    Toast.makeText(ActivityIndex.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkMobileNumber(String number) {
        if (number.matches("^[0][9][0-9]{9}$"))
            return true;
        else
            return false;
    }

    private void getGrade(TextView txtName) {
        gradeList = new ArrayList<>();
        String[] strCrossSection = getResources().getStringArray(R.array.crossSection);
        JSONObject object = createJSONObject(strCrossSection, "txtCrossSection");
        try {
            JSONArray jsonArray = object.getJSONArray("txtCrossSection");
            JSONObject objectList;
            for (int i = 0; i < jsonArray.length(); i++) {
                DropList drop = new DropList();
                objectList = jsonArray.getJSONObject(i);
                drop.setName(objectList.getString("name"));
                drop.setId(objectList.getInt("id"));
                gradeList.add(drop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.summary_dialog, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recSummaryDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterDropList adapter = new AdapterDropList(this, gradeList, dialog, txtName, gradeID);
        recyclerView.setAdapter(adapter);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("مقطع");
        ImageView imgClose = view.findViewById(R.id.imgCloseDialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getDepartment(TextView txtDepartment) {
        departmentList = new ArrayList<>();
        String[] strCrossSection = getResources().getStringArray(R.array.txtDepartment);
        JSONObject object = createJSONObject(strCrossSection, "txtDepartment");
        try {
            JSONArray jsonArray = object.getJSONArray("txtDepartment");
            JSONObject objectList;
            for (int i = 0; i < jsonArray.length(); i++) {
                DropList drop = new DropList();
                objectList = jsonArray.getJSONObject(i);
                drop.setName(objectList.getString("name"));
                drop.setId(objectList.getInt("id"));
                departmentList.add(drop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.summary_dialog, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recSummaryDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterDropList adapter = new AdapterDropList(this, departmentList, dialog, txtDepartment, departmentID);
        recyclerView.setAdapter(adapter);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("گروه تحصیلی");
        ImageView imgClose = view.findViewById(R.id.imgCloseDialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private JSONObject createJSONObject(String[] strArray, String name) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < strArray.length; i++) {
            try {
                JSONObject object = new JSONObject();
                object.put("name", strArray[i]);
                object.put("id", i + 1);
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            jsonObject.put(name, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) == null) {
            return false;
        } else
            return true;
    }
}