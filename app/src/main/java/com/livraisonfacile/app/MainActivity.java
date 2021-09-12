package com.livraisonfacile.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
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
    String version="",update_link="";
    boolean enabled=true;
    //TODO change this link HEEEEEEEre!!!!!!!
    public static String url="https://www.codeur.ma/demo/_fh1iow";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readArgs();
        verify();
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
        mWebView.addJavascriptInterface(new WebInterface(this,mWebView.createPrintDocumentAdapter()),"Android");
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
    public void readArgs(){
        SharedPreferences prefs = this.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        version = prefs.getString("last_version_apk",null);
        update_link=prefs.getString("apk_name", null);
        enabled=prefs.getBoolean("statut",true);
        Log.i("versions ghi mo29ata",""+enabled+"lm39ol hwa:"+prefs.getBoolean("statut",true));
    }
    public void verify(){
        Log.i("versions-loggong-",enabled+";"+version+";"+update_link);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String v = pInfo.versionName;
            Log.i("versions-------->",version+"  "+v);
            if(!enabled){
                showDisabledDialogue();
            }
            else if(!v.equals(version) && enabled){//matkhdemch app
                Log.i("versions-Dialogue-","khass iban");
                showUpdateDialogue();
            }
//            else{//tkhdem app
//                    //we have WIFI
//                new Handler().postDelayed(new Runnable() {
//                    @Override public void run() {
//                        Intent i = new Intent(MainActivity.this, MainActivity.class); startActivity(i);
//                        finish(); } }, 3000);
//
//            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showUpdateDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Mis a jour alert");

        builder.setMessage("vous devez télécharger les dernières mises à jour pour continuer à utiliser cette application")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url+"/"+update_link));
                        startActivity(browserIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    private void showDisabledDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("l'application ne fonctionne pas a ce moment");

        builder.setMessage("vous devez quittez l'application")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
