package com.livraisonfacile.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.onesignal.OneSignalNotificationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.livraisonfacile.app.MainActivity.url;

public class OneSignalUtils {
    public static Context context;
    public static String one_signal_id="";
    public interface OnResponse{
        void onResponse(String[] response);
    }
    public OneSignalUtils(Context c){
        context=c;
    }
    public static String[] getUserInfo(String sublink,String cookies,OnResponse cb){
        RequestQueue queue = Volley.newRequestQueue(context);
        String res[]=new String[3];
        Log.i("OneSignal-ERRRRORR-",url+sublink);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+sublink,
                response -> {
                    try {
                        Log.i("OneSignal-ERRRRORR-","user data-----> "+response.toString());
                        JSONObject obj = new JSONObject(response);
                        res[0]=obj.getString("idu");
                        res[1]=obj.getString("nom");
                        res[2]=obj.getString("niveau");
                        cb.onResponse(res);
                        Log.i("OneSignal-ERRRRORR-E","Data pulled"+res[0]+":"+res[1]+":"+res[2]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("OneSignal-ERRRRORR-",e.getMessage());
                    }

                }, error -> {

            Log.i("versions--on error-", error.getMessage());
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Cookie", cookies);
                Log.i("-Request1331-",cookies);
                return params;

            }
        };
        queue.add(stringRequest);

        return res;
        //TODO ligltlek
        /*
        *daba had reshwa table lifih data d user bachnssiftha l OneSignal server
        * fach kanafficher dikdata f ster 41katban3adi walakin fach katreturna lMyWebViewClient.java katkhrej null null null
        *
         */
    }
    public static String getCookieFromAppCookieManager(String url) throws MalformedURLException {

        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager == null)
            return null;
        String rawCookieHeader = null;
        URL parsedURL = new URL(url);
        rawCookieHeader = cookieManager.getCookie(parsedURL.getHost());
        if (rawCookieHeader == null)
            return null;
        return rawCookieHeader;
    }
    public static void initOneSignal(String[] s) {
        SharedPreferences prefs = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        Log.i("OneSignal-OneSignal",prefs.getString("onesignal_app_id",""));
        OneSignal.initWithContext(context);
        OneSignal.setAppId(prefs.getString("onesignal_app_id",""));
        OneSignal.sendTag("idu", s[0]);
        OneSignal.sendTag("nom", s[1]);
        OneSignal.sendTag("niveau", s[2]);
        Log.i("OneSignal-ERRRRORR-","Tag sent!->"+s[0]+"-"+s[1]+"-"+s[2]);
    }

}
