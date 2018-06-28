package ru.e_meet.kyros;

/**
 * Created by Владимир on 22.02.2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.vk.sdk.VKAccessToken;

import org.json.JSONObject;

import java.util.Map;

import ru.e_meet.kyros.service.NoticeService;

public class MyLocationListener implements LocationListener {

    static Location imHere; // здесь будет всегда доступна самая последняя информация о местоположении пользователя.

    static Location gpsLoc;
    static Location netLoc;

    public static Context context;
    static VKAccessToken vkToken;
    static Map<String, Object> authParams;
    static String provider;
    static LocationManager locationManager;
    static final int CRITICAL_ACCURACY=500;

    public static void SetVkToken(VKAccessToken token){
        vkToken=token;
    }

    public static void SetAuthParams(Map<String, Object> params){
        authParams=params;
    }

    public static void SetUpLocationListener(Context ctx) // это нужно запустить в самом начале работы программы
    {
        context=ctx;
        locationManager = (LocationManager)
                context.getSystemService(context.LOCATION_SERVICE);

        LocationListener gpsListener = new MyLocationListener();
        LocationListener netListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        provider=LocationManager.NETWORK_PROVIDER;
       locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                10,
                new MyLocationListener());
       // getLoc(context, LocationManager.GPS_PROVIDER);

        locationManager.requestLocationUpdates(
                provider,
                1000,
                10,
                new MyLocationListener());
        //getLoc(context, LocationManager.NETWORK_PROVIDER);

    }

    public static void getLoc(Context context, String provider){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        imHere = locationManager.getLastKnownLocation(provider);

    }

    public static void checkIn(Context context){

        if(imHere!=null&&authParams!=null&&context!=null){
            authParams.put("action", "setMyLocation");
            authParams.put("loc_lat", imHere.getLatitude());
            authParams.put("loc_lng", imHere.getLongitude());

            MyApplication.ajax(authParams, context, new AjaxCallback<JSONObject>(){
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //MyApplication.currentActivity.loadData();
                }
            } );
        }
    }

    public static Location whereIAm(){
        return imHere;
    }

    @Override
    public void onLocationChanged(Location loc) {
        imHere = loc;
        //(loc.getAccuracy()>CRITICAL_ACCURACY)provider=LocationManager.GPS_PROVIDER;
        MyLocationListener.checkIn(context);
        NoticeService.loadNotices(context);
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
