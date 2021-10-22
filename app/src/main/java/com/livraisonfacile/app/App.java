package com.livraisonfacile.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class App extends Application {

    static String url = "https://www.codeur.ma/demo/_fh1iow/app/options.json";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("versions","url");
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.i("versions",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        SharedPreferences preferences = getSharedPreferences("app_pref",MODE_PRIVATE);
                        preferences.edit()
                                .putString("last_version_apk",obj.getString("derniere_version_apk"))
                                .putString("apk_name",obj.getString("nom_de_fichier_apk"))
                                .putBoolean("statut",obj.getBoolean("app_statut"))
                                .putString("color1",obj.getString("couleur_1"))
                                .putString("color2",obj.getString("couleur_2"))
                                .putString("onesignal_app_id",obj.getString("onesignal_app_id"))
                                .apply();
                        Log.i("versions---",obj.getString("last_version_apk")+"----"+obj.getString("statut"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("versions-error",e.getMessage());
                    }

                }, error -> {
            Log.i("versions---", String.valueOf(error));
        });
        queue.add(stringRequest);
    }
}
