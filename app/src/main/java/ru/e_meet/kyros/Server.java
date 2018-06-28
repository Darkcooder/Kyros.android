package ru.e_meet.kyros;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.vk.sdk.VKAccessToken;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Владимир on 21.04.2017.
 */

public class Server {
    public static Map<String, Object>auth;
    public static VKAccessToken vkToken;
    public static final String uRL="http://kyros.ru/android";

    public static Map<String, Object> authParams() {
        if(auth==null){
            VKAccessToken tk = vkToken;
            if(tk!=null){
                auth = new HashMap<String, Object>();
                auth.put("vk_token", tk.accessToken);
                auth.put("vk_id", tk.userId);
                auth.put("vk_email", tk.email);
                //params.put("vk_exp", tk.expiresIn);
                return auth;
            }else{
                return null;
            }
        }else{
            Map<String, Object> par=new HashMap<>();
            par.put("vk_token", auth.get("vk_token"));
            par.put("vk_id", auth.get("vk_id"));
            par.put("vk_email", auth.get("vk_email"));
            return par;
        }
    }

    public static void ajax_str(Map<String, Object> params, Context context, AjaxCallback<String> callback){
        AQuery aq=new AQuery(context.getApplicationContext());


        aq.ajax(uRL, params, String.class, callback);
    }

    public static void ajax(Map<String, Object> params, Context context, AjaxCallback<JSONObject> callback){
        AQuery aq=new AQuery(context.getApplicationContext());

        aq.ajax(uRL, params, JSONObject.class, callback);
    }
}
