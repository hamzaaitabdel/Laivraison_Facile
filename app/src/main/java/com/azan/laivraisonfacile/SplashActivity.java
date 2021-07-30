package com.azan.laivraisonfacile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ConnectivityManager manager =(ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                //we have WIFI
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class); startActivity(i);
                        finish(); } }, 1000);
            }
        } else{
            //we have no connection :(
            Intent intent = new Intent(this,NoNetworkActivity.class);
            startActivity(intent);
            finish();
        }
    }

}