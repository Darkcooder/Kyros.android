package ru.e_meet.kyros;

import android.widget.ListView;

import java.util.Map;

import ru.e_meet.kyros.adapters.ItemAdapter;

/**
 * Created by Владимир on 24.02.2017.
 */

public interface MyActivity {
    public void loadData();
    public ItemAdapter getCustomAdapter(String itFilter);
    public ListView showCustomList(ItemAdapter customAdapter, String title);
    public ListView showEmptyList(String title);
    public Map<String, Object>authParams();
    public void reload();
    public void doWait();
}
