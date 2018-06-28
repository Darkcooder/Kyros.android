package ru.e_meet.kyros.adapters;

import android.content.Context;

import java.util.ArrayList;

import ru.e_meet.kyros.Item;
import ru.e_meet.e_meet.R;

/**
 * Created by Владимир on 09.03.2017.
 */

public class OutboxAdapter extends UniverseMeetAdapter {
    @Override
    protected String meetFilter() {
        itemList=waiting;
        return "outbox";
    }

    public OutboxAdapter(Context context, ArrayList<Item> items) {
        super(context, items);
        detailsLayout= R.layout.meet_view;
        addLayout=R.layout.add_task_foem;
        editLayout=R.layout.add_task_foem;
    }
}
