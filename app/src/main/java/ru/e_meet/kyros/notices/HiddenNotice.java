package ru.e_meet.kyros.notices;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Владимир on 28.04.2017.
 */

public class HiddenNotice extends Notice {
    public HiddenNotice(JSONObject input, Context context, int noticeId) throws JSONException {
        super(input, context, noticeId);
    }
    @Override
    public void activate(){}
}
