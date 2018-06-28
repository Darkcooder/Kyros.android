package ru.e_meet.kyros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import ru.e_meet.e_meet.R;
import ru.e_meet.kyros.adapters.ItemAdapter;
import ru.e_meet.kyros.adapters.SecondaryAdapter;

public class ModalActivity extends AppCompatActivity {

    static public Item item;
    static public ItemAdapter adapter;
    static public int layout= R.layout.item_details;
    static public String type;
    static public String title;
    static public String message;
    static public ItemAdapter selectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(title);
        setContentView(layout);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        adapter.modalActivity=this;
        switch (type){
            case "details":
                adapter.makeDetailsContent(findViewById(android.R.id.content), item);
                break;
            case "add":
                adapter.makeAddContent(findViewById(android.R.id.content));
                break;
            case "edit":
                adapter.makeEditContent(findViewById(android.R.id.content));
                break;
            case "select":
                ((TextView)findViewById(R.id.main_txt)).setText(message);
                ((ListView)findViewById(R.id.list)).setAdapter(selectAdapter);
                ((SecondaryAdapter)adapter).activateSelectList((ListView)findViewById(R.id.list));
                break;
        }
    }
    public void doWait(){

    }
}
