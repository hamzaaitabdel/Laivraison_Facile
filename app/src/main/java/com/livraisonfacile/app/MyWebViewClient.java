package com.livraisonfacile.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

class MyWebViewClient extends WebViewClient {

    Context context;
    MainActivity activity;
    LoadingDialog loadingDialog;
    public boolean isConnected = true;
    public OnRedirectToOffline redirectToOffline;
    public SharedPreferences prefs;

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
        if (url.startsWith("mailto:")||url.startsWith("tel:")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
            view.goBack();
            return true;
        }
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
        if(url.contains("voir_")){
            view.loadUrl("javascript: (function (){document.querySelector('.btn').setAttribute('onclick','Android.printPage()')})()");
        }else if(url.contains("colis_admin")){
            view.loadUrl("javascript: (function (){document.querySelector('.btn-default').setAttribute('onclick','Android.printPage()')})()");
        }else if(url.contains("etiquette_admin")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                try {
                    PrintManager printManager = null;
                    printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
                    PrintDocumentAdapter printAdapter = view.createPrintDocumentAdapter();
                    String jobName = context.getString(R.string.app_name) + " Print Test";
                    printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());

            }catch(Exception e){
                Log.i("error printing-->", e.getMessage());
            }
        }
        }
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            loadingDialog.dismiss();
        },1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if(!isConnected && !url.equals("file:///android_asset/pas_de_connexion.html")) {
            redirectToOffline.onRedirectToOffline();
            return false;
        }
        if(!url.equals("file:///android_asset/pas_de_connexion.html")){
            prefs.edit()
                    .putString("last_visited",url)
                    .apply();
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    interface OnRedirectToOffline{
        void onRedirectToOffline();
    }
}
