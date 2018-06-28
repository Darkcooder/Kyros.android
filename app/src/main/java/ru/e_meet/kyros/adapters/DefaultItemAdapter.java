package ru.e_meet.kyros.adapters;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Map;

import ru.e_meet.kyros.Item;
import ru.e_meet.kyros.MyActivity;
import ru.e_meet.kyros.MyApplication;
import ru.e_meet.kyros.MyLocationListener;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 08.03.2017.
 */

public class DefaultItemAdapter extends ItemAdapter {
    public DefaultItemAdapter(Context context, ArrayList<Item> items) {
        super(context, items);
    }

    @Override
    protected View makeBaseContent(View view, Item i) {
        return makeDefaultContent(view, i);
    }

    @Override
    public View makeDetailsContent(View view, Item i) {
        return makeDefaultContent(view, i);
    }
    @Override
    protected Map<String, Object> parseAddForm() {
        Map<String, Object> params=parseForm();
        params.put("action", "addPlace");

        return params;
    }
    @Override
    protected Map<String, Object> parseEditForm() {
        Map<String, Object> params=parseForm();
        params.put("action", "editPlace");

        return params;
    }

    private Map<String,Object> parseForm() {
        Map<String, Object> params=((MyActivity)ctx).authParams();
        params.put("name", MyApplication.valueOf((EditText)modalActivity.findViewById(R.id.itemName)));
        params.put("description", MyApplication.valueOf((EditText)modalActivity.findViewById(R.id.itemDescription)));
        params.put("lat", MyLocationListener.whereIAm().getLatitude());
        params.put("lng", MyLocationListener.whereIAm().getLongitude());

        return params;
    }

}
