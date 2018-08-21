package com.example.behnam.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.behnam.app.helper.MCrypt;
import com.example.behnam.app.helper.SessionManager;
import com.example.behnam.app.map.MapActivity;

import java.io.File;
import java.util.Arrays;

public class ActivityIndex extends AppCompatActivity{
    private static long BackPressed;
    private EditText text;
//    private Speech speechInstance;
//    private SpeechProgressView progress;
//    private ImageView btnListen;
//    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Button btnActiv = findViewById(R.id.activ);
        btnActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityIndex.this, ActivityActivation.class);
                startActivity(intent);
            }
        });

        //finish splash screen
        if (ActivitySplashScreen.activitySplashScreen != null)
            ActivitySplashScreen.activitySplashScreen.finish();

        // search
        text = findViewById(R.id.editTextSearch);
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
                }else {
                    //hide keyboard
                    Class<? extends View.OnClickListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                }else {
                    //hide keyboard
                    Class<? extends TextView.OnEditorActionListener> view = this.getClass();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    assert imm != null;
//                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
//                }
//
//                //voiceSearch
//                View viewDialogMassage = LayoutInflater.from(ActivityIndex.this).inflate(R.layout.massage_dialog, null);
//                final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            case R.id.healing:
                Intent intentHealing = new Intent(ActivityIndex.this, ActivityCategories.class);
                intentHealing.putExtra("type", 0);
                startActivity(intentHealing);
                break;
            case R.id.pharma:
                Intent intentPharma = new Intent(ActivityIndex.this, ActivityCategories.class);
                intentPharma.putExtra("type", 1);
                startActivity(intentPharma);
                break;
            case R.id.location:
                Intent intentLocation = new Intent(ActivityIndex.this, MapActivity.class);
                startActivity(intentLocation);
                break;
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
//        speechInstance = Speech.init(this, getPackageName());
        text.setText("");
        super.onResume();
    }
}