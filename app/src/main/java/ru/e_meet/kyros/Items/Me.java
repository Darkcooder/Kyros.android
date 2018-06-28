package ru.e_meet.kyros.Items;

import android.view.View;

import com.androidquery.AQuery;

import org.json.JSONException;
import org.json.JSONObject;

import ru.e_meet.e_meet.R;
import ru.e_meet.kyros.Exceptions;
import ru.e_meet.kyros.Item;

/**
 * Created by Владимир on 01.03.2017.
 */

public class Me {
    String name;
    String avatar;

    public static String avatarField="photo_200";

    public int avatarId= R.id.meAvatar;
    public int nameId= R.id.meName;

    public Me(JSONObject input) {
        JSONObject data= null;
        try {
            data = input.getJSONArray("response").getJSONObject(0);
            name=data.getString("first_name")+" "+data.getString("last_name");
            avatar=data.getString(avatarField);
        } catch (JSONException e) {
            Exceptions.json(e);
        }

    }
    public View showBar(View view){
        AQuery aq =new AQuery(view);
        aq.id(avatarId).image(avatar);
        aq.id(nameId).text(name);
        return view;
    }
}
