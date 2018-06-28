package ru.e_meet.kyros.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ru.e_meet.kyros.Item;
import ru.e_meet.kyros.Items.ItemLink;
import ru.e_meet.kyros.Items.People;
import ru.e_meet.kyros.ModalActivity;
import ru.e_meet.kyros.MyActivity;
import ru.e_meet.kyros.MyApplication;
import ru.e_meet.kyros.MyLocationListener;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 01.03.2017.
 */

public class PeopleAdapter extends ItemAdapter implements SecondaryAdapter{


    public PeopleAdapter(Context context, ArrayList<Item> items) {
        super(context, items);
        detailsLayout=R.layout.people_view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=super.getView(position, convertView, parent);
        return view;
    }

    protected View makeDefaultContent(View view, People p){
        aq=new AQuery(view);
        aq.id(R.id.itemIcon).image(p.avatarUrl);
        if(p.isFavorite)((ImageView)view.findViewById(R.id.favoriteIcon)).setVisibility(View.VISIBLE);
        return view;
    }

    public static void MakeForeignContent(View context, ItemLink link){
        AQuery aq=new AQuery(context);
        ((TextView)context.findViewById(R.id.peopleName)).setText(link.name);
        aq.id(R.id.peopleIcon).image(link.imageUrl);
    }

    @Override
    protected View makeBaseContent(View view, Item i) {
        return makeDefaultContent(makeDefaultContent(view, i), (People)i);
    }

    @Override
    public View makeDetailsContent(View view, Item i) {
        mainActivity=(MyActivity) ctx;
        view= makeDetailsContent(view, modalActivity, (People) i);
        mainActivity=null;
        return view;
    }

    protected static View makeDetailsContent(View view, final Context context, final People item){
        view=ItemAdapter.makeDefaultContent(view, item);
        final MyActivity main_activity;
        if(mainActivity!=null) main_activity=mainActivity;
        else main_activity=null;
        Button favButton=(Button)view.findViewById(R.id.favorite);

        if(item.isFavorite){
            favButton.setText("Убрать из избранных");
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.doAction("remove", new AjaxCallback<JSONObject>(), context);
                    ((AppCompatActivity)context).finish();
                    if(main_activity!=null)main_activity.loadData();
                }
            });
        }else{
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.doAction("add", new AjaxCallback<JSONObject>(), context);
                    ((AppCompatActivity)context).finish();
                    if(main_activity!=null)main_activity.loadData();
                }
            });
        }

        return view;
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
    @Override
    public void addItem(){
        ModalActivity.adapter=this;
        ModalActivity.type="select";
        ModalActivity.layout=R.layout.content_main;
        ModalActivity.title="Пригласить друга";
        ModalActivity.message="Выберите друга, которого вы хотите пригласить:";

        ModalActivity.selectAdapter=((MyActivity)act).getCustomAdapter("unregistered");
        act.startActivity(modalIntent);

    }
    @Override
    public void activateSelectList(ListView customList){
        customList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((People)ModalActivity.selectAdapter.getItem(position)).invite(modalActivity);
                modalActivity.finish();
            }
        });
    }
}
