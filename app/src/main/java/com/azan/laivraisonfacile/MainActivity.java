package com.azan.laivraisonfacile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements InternetReceiver.OnConnectionListener {
    SwipeRefreshLayout swipeRefresh;
    private WebView mWebView;
    private Snackbar snackbar;
    private boolean isConnected;

    //TODO change this link HEEEEEEEre!!!!!!!
    private String url="https://www.codeur.ma/demo/_fh1iow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(new InternetReceiver(),new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mWebView = findViewById(R.id.activity_main_webview);
        swipeRefresh = findViewById(R.id.refreshLayout);
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
            if(isConnected){
                mWebView.goBack();
                return;
            }
            if(mWebView.getUrl().equals("file:///android_asset/pas_de_connexion.html")){
                return;
            }
            mWebView.loadUrl("file:///android_asset/pas_de_connexion.html");
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConnection(boolean isConnected) {
        this.isConnected = isConnected;
        if(!isConnected){
            snackbar = Snackbar.make(swipeRefresh,"You are offline",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else{
            if(snackbar!=null){
                snackbar.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        InternetReceiver.onConnectionListener = this;
    }
}
