package com.livraisonfacile.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import static com.livraisonfacile.app.MainActivity.url;

public class SplashActivity extends AppCompatActivity {
    String version="",update_link="";
    boolean enabled=true;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ConnectivityManager manager =(ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            readArgs();
            verify(activeNetwork);
        } else{
            //we have no connection :(
            Intent intent = new Intent(this,NoNetworkActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void readArgs(){
        SharedPreferences prefs = this.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        version = prefs.getString("last_version_apk",null);
        update_link=prefs.getString("apk_name", null);
        Log.i("versions ghi mo29ata",prefs.getString("statut","true"));
        enabled=!Boolean.getBoolean(prefs.getString("statut","true"));
    }
    public void verify(NetworkInfo activeNetwork){
        Log.i("versions-loggong-",enabled+";"+version+";"+update_link);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String v = pInfo.versionName;
            Log.i("versions-------->",version+"  "+v);
            if(!v.equals(version) && !enabled){//matkhdemch app
                Log.i("versions-Dialogue-","khass iban");
                showUpdateDialogue();
            }
            else{//tkhdem app
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                    //we have WIFI
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class); startActivity(i);
                            finish(); } }, 1000);
                }
            }
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
}