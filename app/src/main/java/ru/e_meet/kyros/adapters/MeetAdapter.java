package ru.e_meet.kyros.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.androidquery.callback.AjaxCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ru.e_meet.kyros.Item;
import ru.e_meet.kyros.Items.Meet;
import ru.e_meet.kyros.MyActivity;
import ru.e_meet.kyros.MyApplication;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 08.03.2017.
 */

public class MeetAdapter extends UniverseMeetAdapter {


    @Override
    protected String meetFilter() {
        itemList=active;
        return "active";
    }

    public MeetAdapter(Context context, ArrayList<Item> items) {
        super(context, items);
        detailsLayout= R.layout.meet_view;
        addLayout=R.layout.add_task_foem;
        editLayout=R.layout.add_task_foem;
    }

    @Override
    public View makeDetailsContent(View view, Item i){
        mainActivity=(MyActivity) ctx;
        view= makeDetailsContent(view, modalActivity, (Meet) i);
        mainActivity=null;
        return view;
    }

    public static View makeDetailsContent(View view, final Context context, final Meet item){
        view= makeDefaultContent(view, item);
        final MyActivity main_activity;
        if(mainActivity!=null) main_activity=mainActivity;
        else main_activity=null;

        //Нажата кнопка завершить
        view.findViewById(R.id.complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object>params= MyApplication.authParams();
                params.put("action", "completeMeet");
                params.put("id", item.id);
                MyApplication.ajax(params,  context, new AjaxCallback<JSONObject>());
                ((AppCompatActivity)context).finish();
                if(main_activity!=null)main_activity.loadData();
            }
        });

        //Нажата кнопка отменить
        view.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object>params=MyApplication.authParams();
                params.put("action", "removeMeet");
                params.put("id", item.id);
                MyApplication.ajax(params,  context, new AjaxCallback<JSONObject>());
                ((AppCompatActivity)context).finish();
                if(main_activity!=null)main_activity.loadData();
            }
        });
        PeopleAdapter.MakeForeignContent(view, item.people);

        return view;
    }
}
