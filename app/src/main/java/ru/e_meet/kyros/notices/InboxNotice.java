package ru.e_meet.kyros.notices;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 15.03.2017.
 */

public class InboxNotice extends Notice {
    public InboxNotice(JSONObject input, Context context, int noticeId) throws JSONException {
        super(input, context, noticeId);
        customView= R.layout.inbox_meet;
        type="inbox";
    }
}
