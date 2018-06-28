package ru.e_meet.kyros.notices;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Владимир on 15.03.2017.
 */

public class ApplyOutboxNotice extends Notice {
    public ApplyOutboxNotice(JSONObject input, Context context, int noticeId) throws JSONException {
        super(input, context, noticeId);
    }
}
