package com.livraisonfacile.app;

import android.content.Context;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class WebInterface {
    Context mContext;
    WebView webView;
    PrintDocumentAdapter printAdapter;
    WebInterface(Context c,PrintDocumentAdapter printAdapter1) {
        mContext = c;
        printAdapter=printAdapter1;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    public void printPage() {

        //todo: change this toat with the printing code
        Toast.makeText(mContext, "print has been clicked", Toast.LENGTH_SHORT).show();
        createWebPrintJob(printAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createWebPrintJob( PrintDocumentAdapter printAdapter1) {
        try{
            Log.i("printing-->","1");
            //create object of print manager in your device
            PrintManager printManager = (PrintManager) mContext.getSystemService(Context.PRINT_SERVICE);
            Log.i("printing-->","2");
            //create object of print adapter
            PrintDocumentAdapter printAdapter = printAdapter1;
            Log.i("printing-->","3");
            //provide name to your newly generated pdf file
            String jobName = mContext.getString(R.string.app_name) + " Print Test";
            Log.i("printing-->","4");
            //open print dialog
            printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());

        }catch (Exception e){
            Log.i("error printing-->",e.getMessage());
        }

    }

}