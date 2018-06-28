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
 * Created by Владимир on 09.03.2017.
 */

public class InboxAdapter extends UniverseMeetAdapter {
    @Override
    protected String meetFilter() {
        itemList=inbox;
        return "inbox";
    }

    public InboxAdapter(Context context, ArrayList<Item> items) {
        super(context, items);
        detailsLayout= R.layout.inbox_meet;
        addLayout=R.layout.add_task_foem;
        editLayout=R.layout.add_task_foem;
    }

    @Override
    public View makeDetailsContent(View view, final Item i) {
        mainActivity=(MyActivity) ctx;
        view= makeDetailsContent(view, modalActivity, (Meet) i);
        mainActivity=null;
        return view;
    }



    public static View makeDetailsContent(View view, final Context ctx, final Meet i) {
        View content= makeDefaultContent(view, i);
        final MyActivity main_activity;
        if(mainActivity!=null) main_activity=mainActivity;
        else main_activity=null;

        content.findViewById(R.id.inboxMeetApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params= MyApplication.authParams();
                params.put("action", "proofMeetWithMe");
                params.put("meet_id", i.id);
                MyApplication.ajax(params, ctx, new AjaxCallback<JSONObject>());
                ((AppCompatActivity)ctx).finish();
                if(main_activity!=null)main_activity.loadData();
            }
        });

        content.findViewById(R.id.inboxMeetReject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params=MyApplication.authParams();
                params.put("action", "rejectMeetWithMe");
                params.put("meet_id", i.id);
                MyApplication.ajax(params, ctx, new AjaxCallback<JSONObject>());
                ((AppCompatActivity)ctx).finish();
                if(main_activity!=null)main_activity.loadData();
            }
        });
        PeopleAdapter.MakeForeignContent(view, i.people);

        return content;
    }
}
