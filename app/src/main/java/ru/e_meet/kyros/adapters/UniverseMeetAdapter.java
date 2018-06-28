package ru.e_meet.kyros.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Map;

import ru.e_meet.kyros.Item;
import ru.e_meet.kyros.Items.Meet;
import ru.e_meet.kyros.Items.People;
import ru.e_meet.kyros.ModalActivity;
import ru.e_meet.kyros.MyActivity;
import ru.e_meet.kyros.MyApplication;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 10.03.2017.
 */

abstract public class UniverseMeetAdapter extends ItemAdapter  implements SecondaryAdapter{
    protected People selectedPeople;
    protected ArrayList<Item>active;
    protected ArrayList<Item> inbox;
    protected ArrayList<Item> waiting;
    protected ArrayList<Item> all;
    abstract protected String meetFilter();

    public UniverseMeetAdapter(Context context, ArrayList<Item> items) {
        super(context, items);
        inbox=new ArrayList<>();
        waiting=new ArrayList<>();
        active=new ArrayList<>();
        all=items;
        for (int i = 0; i < items.size(); i++) {
            Meet current=(Meet)items.get(i);
            if(current.isActive) active.add(current);
            else if(current.isInbox) inbox.add(current);
            else if(current.isNew)waiting.add(current);
        }
        View tabLayout=act.getLayoutInflater().inflate(R.layout.meet_tab, null);
        ((LinearLayout)act.findViewById(R.id.header)).addView(tabLayout);
        TabHost tab=(TabHost)tabLayout.findViewById(R.id.tab);
        tab.setup();

        TabHost.TabSpec activeTab=tab.newTabSpec("active");
        activeTab.setIndicator("Активные");
        activeTab.setContent(R.id.empty);
        tab.addTab(activeTab);

        TabHost.TabSpec inboxTab=tab.newTabSpec("inbox");
        inboxTab.setIndicator("Входящие");
        inboxTab.setContent(R.id.empty);
        tab.addTab(inboxTab);

        TabHost.TabSpec outboxTab=tab.newTabSpec("outbox");
        outboxTab.setIndicator("Исходящие");
        outboxTab.setContent(R.id.empty);
        tab.addTab(outboxTab);

        tab.setCurrentTabByTag(meetFilter());

        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                ItemAdapter customAdapter;
                ListView list=((MyActivity)act).showEmptyList(" ");
                switch(tabId){
                    case "active":                                      //Выбрана вкладка Активные
                        customAdapter=new MeetAdapter(ctx, all);
                        break;
                    case "inbox":                                       //Выбрана вкладка Входящие
                        customAdapter=new InboxAdapter(ctx, all);
                        break;
                    default:                                            //Выбрана вкладка Исходящие
                        customAdapter=new OutboxAdapter(ctx, all);
                }
                list.setAdapter(customAdapter);
                customAdapter.setItemClickListener(list);
            }
        });
    }

    @Override
    protected View makeBaseContent(View view, Item i) {
        return makeDefaultContent(view, i);
    }

    @Override
    public View makeDetailsContent(View view, Item i) {
        PeopleAdapter.MakeForeignContent(view, ((Meet)i).people);
        return makeDefaultContent(view, i);
    }

    @Override
    public void addItem(){
        ModalActivity.adapter=this;
        ModalActivity.type="select";
        ModalActivity.layout=R.layout.content_main;
        ModalActivity.title="Новая встреча";
        ModalActivity.message="Выберите человека для новой встречи:";

        ModalActivity.selectAdapter=((MyActivity)act).getCustomAdapter("friend");
        act.startActivity(modalIntent);

    }
    @Override
    public void activateSelectList(ListView customList){
        customList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPeople=(People)ModalActivity.selectAdapter.getItem(position);
                UniverseMeetAdapter.super.addItem();
                modalActivity.finish();
            }
        });
    }

    protected Map<String, Object> parseForm(){
        Map<String, Object> params=((MyActivity)ctx).authParams();
        params.put("name", MyApplication.valueOf((EditText)modalActivity.findViewById(R.id.itemName)));
        params.put("description", MyApplication.valueOf((EditText)modalActivity.findViewById(R.id.itemDescription)));
        params.put("distance", MyApplication.valueOf((EditText)modalActivity.findViewById(R.id.taskRadius)));
        params.put("people_id", selectedPeople.id);

        return params;
    }

    @Override
    protected Map<String, Object> parseAddForm() {
        Map<String, Object> params=parseForm();
        params.put("action", "addMeet");

        return params;
    }
    @Override
    protected Map<String, Object> parseEditForm() {
        Map<String, Object> params=parseForm();
        params.put("action", "editMeet");

        return params;
    }


}
