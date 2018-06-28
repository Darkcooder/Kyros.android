package ru.e_meet.kyros.notices;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Владимир on 15.03.2017.
 */

public class MeetNotice extends Notice {
    public MeetNotice(JSONObject input, Context context, int noticeId) throws JSONException {
        super(input, context, noticeId);
        important=true;
        type="meet";
    }
}
