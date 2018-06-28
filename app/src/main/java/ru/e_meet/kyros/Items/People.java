package ru.e_meet.kyros.Items;

import android.content.Context;

import com.androidquery.callback.AjaxCallback;

import org.json.JSONException;
import org.json.JSONObject;

import ru.e_meet.kyros.Item;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 01.03.2017.
 */

public class People extends Item {
    public String avatarUrl;
    public boolean isFavorite=false;

    //public static Class adapter=PeopleAdapter.class;

    public People(JSONObject input) throws JSONException {
        super(input);
        defaultIcon=R.mipmap.ic_people;
        avatarUrl=info.getString("avatar");
        if(!info.getString("is_favorite").equals("0"))isFavorite=true;
        actionSuffix="People";
    }
    public void invite(Context context){
        doAction("invite", new AjaxCallback<JSONObject>(), context);
    }
}
