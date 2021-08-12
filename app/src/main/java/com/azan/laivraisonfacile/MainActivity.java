package com.azan.laivraisonfacile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefresh;
    private WebView mWebView;
    //TODO change this link HEEEEEEEre!!!!!!!
    private String url="https://www.codeur.ma/demo/_fh1iow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);
        swipeRefresh = findViewById(R.id.refreshLayout);
        Runnable runnable = new Runnable(){
            public void run() {
                while (true){
                    Log.i("fffffffffffffffff","ffffffffffff");
                    ConnectivityManager manager =(ConnectivityManager) getApplicationContext()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                    if (null == activeNetwork) {
                        Log.i("fffffffffffffffff","nullllllllll");
                        Intent intent = new Intent(getApplicationContext(),NoNetworkActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        };
        runnable.run();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
                swipeRefresh.setRefreshing(false);
            }
        });
        //progressBar.setIndeterminate(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        mWebView.setWebViewClient(new MyWebViewClient(this,loadingDialog,this));
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
