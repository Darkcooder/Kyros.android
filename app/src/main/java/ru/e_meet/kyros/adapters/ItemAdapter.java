package ru.e_meet.kyros.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ru.e_meet.kyros.Item;
import ru.e_meet.kyros.ModalActivity;
import ru.e_meet.kyros.MyActivity;
import ru.e_meet.kyros.MyApplication;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 01.03.2017.
 */

public abstract class ItemAdapter extends BaseAdapter {
    protected Context ctx;
    AppCompatActivity act;

    static MyActivity mainActivity;

    Intent modalIntent;
    public AppCompatActivity modalActivity;

    LayoutInflater lInflater;
    ArrayList<Item> itemList;
    AQuery aq;
    View view;
    View detailsView;
    boolean updateView=false;
    public int detailsLayout;
    protected View activityDefaultLayout;
    public int addLayout;
    public int editLayout;

    public AdapterView.OnItemClickListener itemClickListener;

    public ItemAdapter(Context context, ArrayList<Item> items){
        ctx=context;
        act=(AppCompatActivity)ctx;
        aq=new AQuery(context);
        itemList=items;
        lInflater=(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        modalIntent=new Intent(ctx, ModalActivity.class);
        detailsLayout=R.layout.item_details;
        addLayout=R.layout.add_place_form;
        editLayout=R.layout.add_place_form;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        view = convertView;
        if((view==null)){
            view=lInflater.inflate(R.layout.item, parent, false);
        }
        Item i= getItem(position);
        view=makeBaseContent(view, i);
        return view;
    }

    public void setItemClickListener(final ListView list){
        final ItemAdapter adapter=this;
        if(itemClickListener==null){
            itemClickListener=new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    activityDefaultLayout=act.findViewById(android.R.id.content);
                    ModalActivity.type="details";
                    ModalActivity.adapter=adapter;
                    ModalActivity.item=getItem(position);
                    ModalActivity.layout=detailsLayout;
                    act.startActivity(modalIntent);
                }
            };
        }
        list.setOnItemClickListener(itemClickListener);
    }

    static public View makeDefaultContent(View view, Item i){
        String distance="";
        if(!i.isLocationDefined){
            distance="За горизонтом";
        }else if(i.distanceFromMe>1000){
            distance=Integer.toString(i.distanceFromMe/1000)+" км.";
        }else{
            distance=Integer.toString(i.distanceFromMe)+" м.";
        }
        ((TextView) view.findViewById(R.id.itemName)).setText(i.name);
        ((TextView) view.findViewById(R.id.itemDistance)).setText(distance);
        ((ImageView) view.findViewById(R.id.itemIcon)).setImageResource(i.defaultIcon);
        return view;
    }

    public void addItem(){
        activityDefaultLayout=act.findViewById(android.R.id.content);
        ModalActivity.type="add";
        ModalActivity.adapter=this;

        ModalActivity.layout=addLayout;
        act.startActivity(modalIntent);
    }

    public void editItem(){
        activityDefaultLayout=act.findViewById(android.R.id.content);
        ModalActivity.type="edit";
        ModalActivity.adapter=this;

        ModalActivity.layout=editLayout;
        act.startActivity(modalIntent);
    }

    public View makeAddContent(View view){
        act=modalActivity;
        //Нажата кнопка Готово
        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params=parseAddForm();
                MyApplication.ajax(params, ctx, new AjaxCallback<JSONObject>(){
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        modalActivity.finish();
                        ((MyActivity)ctx).loadData();
                    }
                });
                //(ModalActivity)modalActivity.doWait();
            }
        });

        //Нажата кнопка Отмена
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalActivity.finish();
                ((MyActivity)ctx).loadData();
            }
        });

        return view;
    }

    public View makeEditContent(View view){
        //Нажата кнопка Готово
        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params=parseEditForm();
                MyApplication.ajax(params, ctx, new AjaxCallback<JSONObject>(){
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        modalActivity.finish();
                        ((MyActivity)ctx).loadData();
                    }
                });
                //(ModalActivity)modalActivity.doWait();
            }
        });

        //Нажата кнопка Отмена
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalActivity.finish();
                ((MyActivity)ctx).loadData();
            }
        });
        return view;
    }

    protected abstract View makeBaseContent(View view, Item i);
    public abstract View makeDetailsContent(View view, Item i);
    protected abstract Map<String, Object> parseAddForm();
    protected abstract Map<String, Object> parseEditForm();
}
