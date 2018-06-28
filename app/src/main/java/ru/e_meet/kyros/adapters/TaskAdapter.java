package ru.e_meet.kyros.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.androidquery.callback.AjaxCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ru.e_meet.kyros.Item;
import ru.e_meet.kyros.Items.Place;
import ru.e_meet.kyros.Items.Task;
import ru.e_meet.kyros.ModalActivity;
import ru.e_meet.kyros.MyActivity;
import ru.e_meet.kyros.MyApplication;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 08.03.2017.
 */

public class TaskAdapter extends ItemAdapter implements SecondaryAdapter{

    Place selectedPlace;
    public TaskAdapter(Context context, ArrayList<Item> items) {
        super(context, items);
        detailsLayout= R.layout.task_view;
        addLayout=R.layout.add_task_foem;
        editLayout=R.layout.add_task_foem;
    }

    @Override
    protected View makeBaseContent(View view, Item i) {
        return makeDefaultContent(view, i);
    }

    @Override
    public View makeDetailsContent(View view, Item i){
        mainActivity=(MyActivity) ctx;
        view= makeDetailsContent(view, modalActivity, (Task) i);
        mainActivity=null;
        return view;
    }

    public static View makeDetailsContent(View view, final Context context, final Task item){
        view= makeDefaultContent(view, item);
        final MyActivity main_activity;
        if(mainActivity!=null) main_activity=mainActivity;
        else main_activity=null;

        PlaceAdapter.makeForeignContent((AppCompatActivity)context, item.place);

        //Нажата кнопка завершить
        view.findViewById(R.id.complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object>params= MyApplication.authParams();
                params.put("action", "completeTask");
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
                params.put("action", "removeTask");
                params.put("id", item.id);
                MyApplication.ajax(params,  context, new AjaxCallback<JSONObject>());
                ((AppCompatActivity)context).finish();
                if(main_activity!=null)main_activity.loadData();
            }
        });

        return view;
    }

    @Override
    public void addItem(){
        ModalActivity.adapter=this;
        ModalActivity.type="select";
        ModalActivity.layout=R.layout.content_main;
        ModalActivity.title="Новая задача";
        ModalActivity.message="Выберите место для новой задачи:";

        ModalActivity.selectAdapter=((MyActivity)act).getCustomAdapter("place");
        act.startActivity(modalIntent);

    }
    @Override
    public void activateSelectList(ListView customList){
        customList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPlace=(Place)ModalActivity.selectAdapter.getItem(position);
                TaskAdapter.super.addItem();
                modalActivity.finish();
            }
        });
    }

    @Override
    protected Map<String, Object> parseAddForm() {
        Map<String, Object> params=parseForm();
        params.put("action", "addAnchor");
        return params;
    }
    @Override
    protected Map<String, Object> parseEditForm() {
        Map<String, Object> params=parseForm();
        params.put("action", "editAnchor");

        return params;
    }

    private Map<String,Object> parseForm() {
        Map<String, Object> params=((MyActivity)ctx).authParams();
        params.put("name", MyApplication.valueOf((EditText)modalActivity.findViewById(R.id.itemName)));
        params.put("description", MyApplication.valueOf((EditText)modalActivity.findViewById(R.id.itemDescription)));
        params.put("distance", MyApplication.valueOf((EditText)modalActivity.findViewById(R.id.taskRadius)));
        params.put("place_id", selectedPlace.id);

        return params;
    }
}
