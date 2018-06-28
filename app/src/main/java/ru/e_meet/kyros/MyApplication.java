package ru.e_meet.kyros;

import android.app.Application;
import android.content.Context;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.vk.sdk.VKSdk;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import ru.e_meet.kyros.adapters.SQLAdapter;
import ru.e_meet.kyros.notices.Notice;
import ru.e_meet.kyros.service.NoticeService;

/**
 * Created by Владимир on 22.02.2017.
 */

public class MyApplication extends Application {

    //public static VKAccessToken vkToken;

    public static String objFilter="people";
    public static MyActivity currentActivity;
    public static AQuery aq;

    public static Map<String, Notice>noticeMap;


    //public static Map<String, Object>auth;


    public static MyFactory factory;



    public static void ajax(Map<String, Object> params, Context context, AjaxCallback<JSONObject> callback){
        Server.ajax(params, context, callback);
    }
    public static void checkAuth(final MainActivity context){

        Server.ajax_str(authParams(), context, new AjaxCallback<String>(){
           @Override
           public void callback(String url, String json, AjaxStatus status){
               if (json.equals("Empty user data")){
                   Server.auth=null;
                   VKSdk.login(context);
               }else{
                   context.init();
               }
           }
       });
    }

    public static String valueOf(EditText field){
        return MyApplication.encodeURIComponent(field.getText().toString());
    }

    public static String encodeURIComponent(String s)
    {
        String result = null;

        try
        {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        }

        // This exception should never occur.
        catch (UnsupportedEncodingException e)
        {
            result = s;
        }

        return result;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
        factory=new MyFactory(this);
    }

    public static Map<String, Object> authParams() {
        return Server.authParams();
    }
    public static void vkAuth(MainActivity ctx){
        SQLAdapter data=new SQLAdapter(ctx);
        Map<String, Object>params=data.getMap();
        if(params.isEmpty()){
            VKSdk.login(ctx);
            Server.auth=null;
        } else {
            Server.auth=params;
            checkAuth(ctx);

        }
    }

    public void loadNotices(){
        loadNotices(this, factory);
    }
    public static void loadNotices(Context context, final MyFactory factory){
        NoticeService.loadNotices(context);
    }


}
