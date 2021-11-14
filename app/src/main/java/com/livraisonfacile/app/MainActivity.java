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
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements InternetReceiver.OnConnectionListener , MyWebViewClient.OnRedirectToOffline {
    SwipeRefreshLayout swipeRefresh;
    private WebView mWebView;
    private Snackbar snackbar;
    private boolean isConnected;
    private MyWebViewClient client;
    private SharedPreferences prefs;
    String version="",update_link="";

    public static String color1="";
    public static String color2="";
    boolean enabled=true;
    //TODO change this link HEEEEEEEre!!!!!!!
    public static String url="https://www.codeur.ma/demo/_fh1iow";

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);
        Toast.makeText(this,"Ssssssss",Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // OneSignal Initialization
        prefs = getSharedPreferences("app_prefs",MODE_PRIVATE);
        Log.i("Rotation-debug",prefs.getString("last_visited",url));
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i("Thread1212",prefs.getString("last_visited",url));
            }
        }, 0, 1000);
        mWebView = findViewById(R.id.activity_main_webview);
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(result -> {
            String launchURL = result.getNotification().getLaunchURL();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            if (launchURL!=null){
                prefs.edit().putString("last_visited",launchURL).apply();
                Toast.makeText(this,"Redirection...!",Toast.LENGTH_LONG).show();
                intent.putExtra("link",launchURL);
                startActivity(intent);
            }else{
                mWebView.loadUrl(prefs.getString("last_visited",url));
            }
//            mWebView.loadUrl(launchURL);

        });
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statue_bar_color));
        }
        readArgs();
        verify();
        registerReceiver(new InternetReceiver(),new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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
        client = new MyWebViewClient(this,loadingDialog,this);
        client.redirectToOffline = this;
        client.prefs = prefs;
        mWebView.setWebViewClient(client);
        mWebView.addJavascriptInterface(new WebInterface(this,mWebView.createPrintDocumentAdapter()),"Android");
        // REMOTE RESOURCE
        //TODO remove cmnts mWebView.loadUrl(prefs.getString("last_visited",url));

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
                    Log.i("url_li_kat1",url);
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
            snackbar = Snackbar.make(swipeRefresh,"Vous n'êtes pas connecté !",Snackbar.LENGTH_SHORT);
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
        mWebView.loadUrl(prefs.getString("last_visited",url));
        InternetReceiver.onConnectionListener = this;

    }

    @Override
    public void onRedirectToOffline() {
        mWebView.loadUrl("file:///android_asset/pas_de_connexion.html");
    }
    public void readArgs(){
        SharedPreferences prefs = this.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        Intent i=getIntent();
        if (i!=null && i.getStringExtra("link")!=null){
            mWebView.loadUrl(i.getStringExtra("link"));
        }else
            mWebView.loadUrl(prefs.getString("last_visited",url));
        Log.i("url_li_kat9leb 3lih",url+"whada "+prefs.getString("last_visited","url khawi"));
        version = prefs.getString("last_version_apk",null);
        update_link=prefs.getString("apk_name", null);
        enabled=prefs.getBoolean("statut",true);
        color1=prefs.getString("color1","#00365c");
        color2= prefs.getString("color2","#0bc6fd");
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
        mWebView.loadUrl("https://www.codeur.ma/demo/_fh1iow/index.php?am=indisponible_app");
        url="https://www.codeur.ma/demo/_fh1iow/index.php?am=indisponible_app";
        Log.e("showDisabledDialogue()",mWebView.getUrl());
        /*
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

         */
    }

}
