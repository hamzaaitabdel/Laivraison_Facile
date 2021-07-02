package com.azan.laivraisonfacile;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class MyWebViewClient extends WebViewClient {

    Context context;
    MainActivity activity;
    com.azan.laivraisonfacile.loadingDialog loadingDialog;
    public MyWebViewClient(Context c, final com.azan.laivraisonfacile.loadingDialog loadingDialog, MainActivity mainActivity) {
        context=c;
        this.loadingDialog=loadingDialog;
        this.activity=mainActivity;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        //Toast.makeText(context,"hii",Toast.LENGTH_LONG).show();
        showDialog(loadingDialog);
        view.loadUrl(url);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.purple_700));
            Log.i("COLOR----->",view.getSolidColor()+"");
        }
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
            },1000); //You can change this time as you wish
        }


}
