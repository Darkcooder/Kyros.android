package ru.e_meet.kyros;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.e_meet.kyros.service.NoticeService;

/**
 * Created by Владимир on 26.03.2017.
 */

public class MyBroadReceiv extends BroadcastReceiver {

    final String LOG_TAG = "myLogs";

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive " + intent.getAction());
        context.startService(new Intent(context, NoticeService.class));
    }
}