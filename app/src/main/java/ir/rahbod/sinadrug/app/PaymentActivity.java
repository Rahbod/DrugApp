package ir.rahbod.sinadrug.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import ir.rahbod.sinadrug.ActivityCheckTransaction;

import ir.rahbod.sinadrug.ActivityCheckTransaction;

public class PaymentActivity extends AppCompatActivity {
    private int id;
    public static Activity activityPayment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        activityPayment = this;

        String url = getIntent().getStringExtra("url");
        id = getIntent().getIntExtra("id", 0);
        final WebView webView = findViewById(R.id.webView);
        webView.clearCache(true);
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals("http://rahbod.ir/android/api/verify")){
                    Intent intent = new Intent(PaymentActivity.this, ActivityCheckTransaction.class);
                    intent.putExtra("id", id);
                    intent.putExtra("action", getIntent().getStringExtra("action"));
                    startActivity(intent);
                }
                super.onPageFinished(view, url);
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);
    }
}
