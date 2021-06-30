package com.azan.laivraisonfacile;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class MyWebViewClient extends WebViewClient {

    Context context;
    Activity activity;
    com.azan.laivraisonfacile.loadingDialog loadingDialog;
    public MyWebViewClient(Context c,final com.azan.laivraisonfacile.loadingDialog loadingDialog) {
        context=c;
        this.loadingDialog=loadingDialog;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        //Toast.makeText(context,"hii",Toast.LENGTH_LONG).show();
        showDialog(loadingDialog);
        view.loadUrl(url);
        return true;
    }
    public void showDialog(final com.azan.laivraisonfacile.loadingDialog loadingDialog){
            loadingDialog.startLoadingDialog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismisDialog();
                }
            },2000); //You can change this time as you wish
        }


}
