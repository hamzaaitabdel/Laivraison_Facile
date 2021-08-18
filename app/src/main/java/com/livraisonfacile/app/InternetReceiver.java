package com.livraisonfacile.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetReceiver extends BroadcastReceiver {
    public static OnConnectionListener onConnectionListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(onConnectionListener!=null){
            onConnectionListener.onConnection(isConnected(context));
        }
    }

    public boolean isConnected(Context context){
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = con.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    interface OnConnectionListener{
        void onConnection(boolean isConnected);
    }
}