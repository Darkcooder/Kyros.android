package ru.e_meet.kyros.Items;

import org.json.JSONException;
import org.json.JSONObject;

import ru.e_meet.kyros.Item;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 01.03.2017.
 */

public class Task extends Item {
    public String placeName;
    public ItemLink place;
    public int placeId;
    public Task(JSONObject input) throws JSONException {
        super(input);
        defaultIcon= R.mipmap.ic_task;
        place=new ItemLink();
        place.name=input.getJSONObject("place").getString("name");
    }
}
