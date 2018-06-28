package ru.e_meet.kyros.Items;

import org.json.JSONException;
import org.json.JSONObject;

import ru.e_meet.kyros.Item;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 01.03.2017.
 */

public class Place extends Item {

    //public static Class adapter=ItemAdapter.class;

    public Place(JSONObject input) throws JSONException{
        super(input);
        defaultIcon=R.mipmap.ic_place;
    }
}
