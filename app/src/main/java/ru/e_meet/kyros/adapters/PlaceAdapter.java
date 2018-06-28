package ru.e_meet.kyros.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ru.e_meet.kyros.Item;
import ru.e_meet.kyros.Items.ItemLink;
import ru.e_meet.kyros.Items.Place;
import ru.e_meet.kyros.MyActivity;
import ru.e_meet.kyros.MyApplication;
import ru.e_meet.kyros.MyLocationListener;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 08.03.2017.
 */

public class PlaceAdapter extends ItemAdapter {
    public PlaceAdapter(Context context, ArrayList<Item> items) {
        super(context, items);
    }

    @Override
    protected View makeBaseContent(View view, Item i) {
        view=makeDefaultContent(view, i);

        return view;
    }
    public static void makeForeignContent(AppCompatActivity activity, ItemLink link){
        ((TextView)(activity.findViewById(R.id.placeName))).setText(link.name);
    }

    @Override
    public View makeDetailsContent(View view, final Item i) {
        ((TextView)view.findViewById(R.id.itemDescription)).setText(((Place)i).description);

        //Нажали кнопку Изменить
        view.findViewById(R.id.itemEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppCompatActivity act=modalActivity;
                act.setContentView(editLayout);
                final EditText nameField=(EditText)act.findViewById(R.id.itemName);
                final EditText descriptionField=(EditText)act.findViewById(R.id.itemDescription);
                nameField.setText(i.name);
                descriptionField.setText(i.description);

                //Нажали кнопку Готово
                act.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> params=parseEditForm();
                        MyApplication.ajax(params, ctx, new AjaxCallback<JSONObject>());
                        modalActivity.finish();
                        ((MyActivity)ctx).loadData();
                    }
                });

                //Нажали кнопку Отмена
                act.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modalActivity.setContentView(detailsLayout);
                        makeDetailsContent(modalActivity.findViewById(android.R.id.content), i);
                    }
                });
            }
        });

        //Нажали кнопку Удалить
        view.findViewById(R.id.itemRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params=((MyActivity)ctx).authParams();
                params.put("action", "removePlace");
                params.put("id", i.id);
                MyApplication.ajax(params, ctx, new AjaxCallback<JSONObject>());
                final AppCompatActivity act=(AppCompatActivity)ctx;
                modalActivity.finish();
                ((MyActivity)ctx).loadData();
            }
        });
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
