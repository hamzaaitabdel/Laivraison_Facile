package com.azan.laivraisonfacile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class NoNetworkActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
        webView=findViewById(R.id.noConnection);
        webView.loadUrl("file:///android_asset/pas_de_connexion.html");
    }
}