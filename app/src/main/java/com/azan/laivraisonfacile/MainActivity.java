package com.azan.laivraisonfacile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    //TODO change this link HEEEEEEEre!!!!!!!
    private String url="https://www.codeur.ma/demo/_fh1iow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);
        //progressBar.setIndeterminate(true);
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
        loadingDialog loadingDialog = new loadingDialog(MainActivity.this);
        mWebView.setWebViewClient(new MyWebViewClient(this,loadingDialog));
        // REMOTE RESOURCE
        mWebView.loadUrl(url);

        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
