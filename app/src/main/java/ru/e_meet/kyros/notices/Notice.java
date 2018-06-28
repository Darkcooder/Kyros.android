package ru.e_meet.kyros.notices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import ru.e_meet.kyros.Item;
import ru.e_meet.kyros.Items.Meet;
import ru.e_meet.kyros.NoticeActivity;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 01.03.2017.
 */

public class Notice {
    public Item item;
    int id;
    static int counter=100;
    NotificationManager notificationManager;
    Notification notification;
    Context ctx;
    Boolean important=false;
    public int customView=R.layout.task_view;
    public String type="default";

    Intent notificationIntent;

    public Notice(JSONObject input, Context context, int noticeId) throws JSONException{
        item=Item.make(input);
        id=noticeId;
        ctx=context;
    }
    public static Notice make(JSONObject input, Context context, int noticeId) throws JSONException{
        String type=Item.getType(input);
        switch (type){
            case "task": return new TaskNotice(input, context, noticeId);
            case "meet": switch(Meet.getType(input)){
                case "active": return new MeetNotice(input, context, noticeId);
                case "inbox": return new InboxNotice(input, context, noticeId);
                case "apply_outbox": return new ApplyOutboxNotice(input, context, noticeId);
                default: return new Notice(input, context, noticeId);
            }
            case "hidden": return new HiddenNotice(input, context, noticeId);
            default: return new Notice(input, context, noticeId);
        }
    }

    public void activate(){

        notificationIntent = new Intent(ctx, NoticeActivity.class);
        notificationIntent.putExtra("noticeId", id);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx,
                id, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        //Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(ctx);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(item.defaultIcon)
                // большая картинка
                //.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.hungrycat))
                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker(item.name)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle(item.name)
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText(item.description); // Текст уведомления

        notification = builder.getNotification(); // до API 16
        //Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;

        if(important)notification.flags = notification.flags | Notification.FLAG_INSISTENT;



        notificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id+counter, notification);
    }
}
