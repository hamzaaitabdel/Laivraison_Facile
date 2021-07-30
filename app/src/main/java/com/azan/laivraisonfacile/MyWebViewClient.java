package com.azan.laivraisonfacile;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class MyWebViewClient extends WebViewClient {

    Context context;
    MainActivity activity;
    LoadingDialog loadingDialog;

    public MyWebViewClient(Context c, final LoadingDialog loadingDialog, MainActivity mainActivity) {
        context=c;
        this.loadingDialog=loadingDialog;
        this.activity=mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        //Toast.makeText(context,"hii",Toast.LENGTH_LONG).show();
        loadingDialog.show();
        view.loadUrl(url);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.statue_bar_color));
            Log.i("COLOR----->",view.getSolidColor()+"");
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            loadingDialog.dismiss();
        },1000);
    }

}
