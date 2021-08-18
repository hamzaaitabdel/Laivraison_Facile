package com.livraisonfacile.app;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebInterface {
    Context mContext;

    WebInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void printPage() {

        //todo: change this toat with the printing code
        Toast.makeText(mContext, "print has been clicked", Toast.LENGTH_SHORT).show();
    }
}