package ru.e_meet.kyros.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ru.e_meet.kyros.MyFactory;
import ru.e_meet.kyros.MyLocationListener;
import ru.e_meet.kyros.Server;
import ru.e_meet.kyros.adapters.SQLAdapter;
import ru.e_meet.kyros.notices.Notice;

/**
 * Created by Владимир on 19.03.2017.
 */

public class NoticeService extends Service {
    NotificationManager nm;
    static MyFactory factory;
    public static ArrayList<Notice> notices;
    public static ArrayList<Notice>oldNotices;
    public static int updateTime=60000;
    public static Map<String, Object>auth;
    static Timer noticeTimer;
    static TimerTask noticeTask;
    public static Context context;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        factory=new MyFactory(this);
        SQLAdapter data=new SQLAdapter(this);
        auth=data.getMap();
        if(Server.auth==null)Server.auth=auth;
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Context context=this;


        MyLocationListener.SetAuthParams(Server.authParams());
        MyLocationListener.SetUpLocationListener(context);

        noticeTimer=new Timer();
        loadNotices(context);
        return super.onStartCommand(intent, flags, startId);
    }

    public static void loadNotices(final Context context){
        Map<String, Object> params= Server.authParams();
        params.put("action", "getNotices");
        noticeTimer.cancel();
        Server.ajax(params, context, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                if(notices!=null)oldNotices=notices;
                notices=factory.getItemsByJson(json);
                factory.activateNotices();
                if(factory.findPreNotice(json))updateTime=5000;
                else updateTime=60000;
                noticeTimer=new Timer();
                TimerTask noticeTask=new TimerTask(){
                    @Override
                    public void run() {
                        if(Server.authParams()==null)Server.auth=auth;
                        //MyLocationListener.checkIn(context);
                        loadNotices(context);
                    }
                };
                noticeTimer.schedule(noticeTask, updateTime);
            }
        });
    }

}
