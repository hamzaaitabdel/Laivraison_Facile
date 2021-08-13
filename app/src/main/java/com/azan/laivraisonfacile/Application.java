package com.azan.laivraisonfacile;

import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.codeur.ma/demo/_fh1iow/app/settings.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(url);
                        SharedPreferences preferences = getSharedPreferences("app_pref",MODE_PRIVATE);
                        preferences.edit()
                                .putString("last_version_apk",obj.getString("last_version_apk"))
                                .putString("apk_name",obj.getString("apk_name"))
                                .putString("statut",obj.getString("statut"))
                                .apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
        });
        queue.add(stringRequest);
    }
}
