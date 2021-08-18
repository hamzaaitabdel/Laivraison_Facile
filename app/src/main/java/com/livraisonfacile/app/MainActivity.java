package com.livraisonfacile.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements InternetReceiver.OnConnectionListener , MyWebViewClient.OnRedirectToOffline {
    SwipeRefreshLayout swipeRefresh;
    private WebView mWebView;
    private Snackbar snackbar;
    private boolean isConnected;
    private MyWebViewClient client;
    private SharedPreferences prefs;

    //TODO change this link HEEEEEEEre!!!!!!!
    public static String url="https://www.codeur.ma/demo/_fh1iow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(new InternetReceiver(),new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mWebView = findViewById(R.id.activity_main_webview);
        swipeRefresh = findViewById(R.id.refreshLayout);
        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Uri.parse(url).getPath());
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Telechergement du fichier...", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isConnected){
                    if(mWebView.getUrl().equals("file:///android_asset/pas_de_connexion.html") && mWebView.canGoBack()){
                        mWebView.goBack();
                    }else{
                        mWebView.reload();
                    }
                }else{
                    if(!mWebView.getUrl().equals("file:///android_asset/pas_de_connexion.html")){
                        mWebView.loadUrl("file:///android_asset/pas_de_connexion.html");
                    }
                }

                swipeRefresh.setRefreshing(false);
            }
        });
        //progressBar.setIndeterminate(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        prefs = getSharedPreferences("app_prefs",MODE_PRIVATE);
        client = new MyWebViewClient(this,loadingDialog,this);
        client.redirectToOffline = this;
        client.prefs = prefs;
        mWebView.setWebViewClient(client);
        mWebView.addJavascriptInterface(new WebInterface(this),"Android");
        // REMOTE RESOURCE
        mWebView.loadUrl(prefs.getString("last_visited",url));

        // LOCAL RESOURCE
        // mWebView.loadUrl("file:///android_asset/index.html");


    }

    @Override
    public void onBackPressed() {

        if(isConnected){
            if(mWebView.canGoBack()) {
                mWebView.goBack();
                if(!mWebView.getUrl().equals("file:///android_asset/pas_de_connexion.html")){
                    prefs.edit()
                            .putString("last_visited",url)
                            .apply();
                }
                return;
            }
            super.onBackPressed();
        }
        if(!mWebView.getUrl().equals("file:///android_asset/pas_de_connexion.html")){
            mWebView.loadUrl("file:///android_asset/pas_de_connexion.html");
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConnection(boolean isConnected) {
        this.isConnected = isConnected;
        client.isConnected = isConnected;
        if(!isConnected){
            snackbar = Snackbar.make(swipeRefresh,"Vous etes hors ligne!",Snackbar.LENGTH_SHORT);
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

    @Override
    public void onRedirectToOffline() {
        mWebView.loadUrl("file:///android_asset/pas_de_connexion.html");
    }
}
