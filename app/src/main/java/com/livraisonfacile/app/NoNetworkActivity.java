package com.livraisonfacile.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

public class NoNetworkActivity extends AppCompatActivity {
    private WebView webView;
    SwipeRefreshLayout swipeRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
        webView=findViewById(R.id.noConnection);
        webView.loadUrl("file:///android_asset/pas_de_connexion.html");
        swipeRefresh = findViewById(R.id.refreshLayout);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onRefresh() {
                ConnectivityManager manager =(ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (null != activeNetwork) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        swipeRefresh.setRefreshing(false);
                        Intent i = new Intent(NoNetworkActivity.this, MainActivity.class); startActivity(i);
                        finish();
                    }
                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}