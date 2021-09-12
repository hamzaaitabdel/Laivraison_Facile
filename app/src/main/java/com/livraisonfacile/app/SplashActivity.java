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
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class); startActivity(i);
                        finish(); } }, 3000);
            }

        } else{
            //we have no connection :(
            Intent intent = new Intent(this,NoNetworkActivity.class);
            startActivity(intent);
            finish();
        }
    }

}