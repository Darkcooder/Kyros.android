package ru.e_meet.kyros;

import android.content.Context;
import android.location.Location;

import com.androidquery.callback.AjaxCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import ru.e_meet.kyros.Items.Me;
import ru.e_meet.kyros.Items.Meet;
import ru.e_meet.kyros.Items.People;
import ru.e_meet.kyros.Items.Place;
import ru.e_meet.kyros.Items.Task;
import ru.e_meet.e_meet.R;


/**
 * Created by Владимир on 01.03.2017.
 */

public class Item {
    public int defaultIcon= R.mipmap.ic_unknown;
    public String name;
    public String description;
    public Location location;
    public int distanceFromMe;
    public int id;
    public boolean isLocationDefined;
    protected String actionSuffix="";

    protected JSONObject info;


    public Item(JSONObject input) throws JSONException{
        info = input.getJSONObject("info");
        name=info.getString("name");
        if(!info.isNull("distance"))distanceFromMe=info.getInt("distance");
        if(!input.isNull("id"))id=input.getInt("id");
        if(!info.isNull("description"))description=info.getString("description");
        isLocationDefined=!input.isNull("location");
    }
    public void doAction(String name, AjaxCallback<JSONObject> callback, Context context){
        Map<String, Object> params=Server.authParams();
        params.put("action", name+actionSuffix);
        params.put("id", this.id);
        Server.ajax(params,  context, callback);
    }

    public static String getType(JSONObject input) throws JSONException{
        return input.getString("type");
    }
    public static Item make(JSONObject input) throws JSONException{
        switch (Item.getType(input)) {
            case "people":return new People(input);
            case "place":return new Place(input);
            case "meet":return new Meet(input);
            case "task":return new Task(input);
            default:return new Item(input);
        }
    }
}
