package ru.e_meet.kyros;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ru.e_meet.kyros.notices.Notice;
import ru.e_meet.kyros.adapters.DefaultItemAdapter;
import ru.e_meet.kyros.adapters.ItemAdapter;
import ru.e_meet.kyros.adapters.MeetAdapter;
import ru.e_meet.kyros.adapters.PeopleAdapter;
import ru.e_meet.kyros.adapters.PlaceAdapter;
import ru.e_meet.kyros.adapters.TaskAdapter;

/**
 * Created by Владимир on 01.03.2017.
 */

public class MyFactory {
    String itemsType;
    ArrayList<Item>items;
    ArrayList<Notice>notices;
    Context ctx;

    public MyFactory(Context context){
        ctx=context;
    }
    public ArrayList<Notice> getItemsByJson(JSONObject json){
        notices=new ArrayList<>();
        if(json==null)return notices;
        try{
            JSONArray data = json.getJSONArray("data");
            int x=data.length();
            for (int i = 0; i < data.length(); i++) {
                JSONObject current=data.getJSONObject(i);
                Notice notice=Notice.make(current, ctx, i);
                notices.add(notice);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return notices;
    }
    public boolean findPreNotice(JSONObject json){
        if(json==null)return false;
        try{
            JSONArray data=json.getJSONArray("data");
            int x=data.length();
            for (int i = 0; i < data.length(); i++) {
                JSONObject current=data.getJSONObject(i);
                if(current.getJSONObject("info").getString("name").equals("pre"))return true;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void activateNotices(){
        Boolean firstActivation=MyApplication.noticeMap==null;
        if(firstActivation)MyApplication.noticeMap=new HashMap<>();
        for(int i=0; i<notices.size(); i++){
            Notice notice=notices.get(i);
            String noticeId=notice.type+"_"+Integer.toString(notice.item.id);
            if(firstActivation||MyApplication.noticeMap.get(noticeId)==null){
                notice.activate();
                MyApplication.noticeMap.put(noticeId, notice);
            }

        }
    }
    public String typeOfTag(String tag){
        if(tag.equals("friend"))return "people";
        if(tag.equals("unregistered"))return "people";
        return tag;
    }
    public ArrayList<Item> getItemsByJson(JSONObject json, String type){
        items=new ArrayList<>();
        itemsType=type;
        if(json==null)return items;
        try{
            JSONArray data = json.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject current=data.getJSONObject(i);
                Item item;
                String itemType=Item.getType(current);
                if(itemType.equals(itemsType)){
                    item=Item.make(current);
                    items.add(item);
                }

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }
    public ItemAdapter getAdapter(ArrayList<Item> items){
        return getCustomAdapter(items, itemsType);
    }
    public ItemAdapter getCustomAdapter(ArrayList<Item>items, String type){
        switch (type) {
            case "people": return new PeopleAdapter(ctx, items);
            case "place": return new PlaceAdapter(ctx, items);
            case "meet": return new MeetAdapter(ctx, items);
            case "task": return new TaskAdapter(ctx, items);
            case "me":  return new DefaultItemAdapter(ctx, items);
            default: return new DefaultItemAdapter(ctx, items);
        }
    }
}
