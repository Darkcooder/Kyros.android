package ru.e_meet.kyros.Items;

import org.json.JSONException;
import org.json.JSONObject;

import ru.e_meet.kyros.Item;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 01.03.2017.
 */

public class Meet extends Item {
    public Boolean isActive=false;
    public Boolean isInbox=false;
    public Boolean isNew=false;

    public ItemLink people;

    public Meet(JSONObject input)  throws JSONException {
        super(input);
        defaultIcon= R.mipmap.ic_meet;
        String passive=input.getString("passive");
        if ((!input.isNull("passive")) && input.getString("passive").equals("1")) isInbox = true;
        if ((!input.isNull("proof")) && input.getString("proof").equals("1")) isActive = true;
        if ((!input.isNull("isnew")) && input.getString("isnew").equals("1")) isNew = true;
        people=new ItemLink();
        people.name=input.getJSONObject("people").getString("name");
        people.imageUrl=input.getJSONObject("people").getString("avatar");
    }

    public static String getType(JSONObject input) throws JSONException{
        if(!(input.isNull("proof")||input.isNull("passive"))){
            if(input.getString("proof").equals("1"))return "active";
            else if(input.getString("passive").equals("1"))return "inbox";
            else if(input.getString("proof").equals("2")) return "apply_outbox";
        }
        return "unknown";
    }
}
