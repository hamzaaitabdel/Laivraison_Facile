package com.azan.laivraisonfacile;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class loadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    loadingDialog(Activity myactivity)
    {
        activity= myactivity;
    }

    void startLoadingDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }


    void dismisDialog()
    {
        alertDialog.dismiss();
    }
}