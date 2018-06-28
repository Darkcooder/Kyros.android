package ru.e_meet.kyros;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ru.e_meet.e_meet.R;
import ru.e_meet.kyros.Items.Me;
import ru.e_meet.kyros.adapters.ItemAdapter;
import ru.e_meet.kyros.adapters.SQLAdapter;
import ru.e_meet.kyros.service.NoticeService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MyActivity {
    private AQuery aq;
    private String filter;
    String tag="friend";
    private Context context;
    MyFactory factory;
    ItemAdapter adapter;
    JSONObject lastData;
    Map<String, ArrayList<Item>>cache;
    private VKAccessToken token;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

 /*   private MainActivity(){

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Друзья");
        reload();

    }

    @Override
    public void reload(){
        cache=new HashMap<>();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        factory=new MyFactory(this);
        MyApplication.currentActivity = this;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addItem();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
            @Override
            public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
                if (newToken == null) {
                    // VKAccessToken is invalid
                    VKSdk.login((MainActivity)context);
                }
            }
        };
        vkAccessTokenTracker.startTracking();

        token = Server.vkToken;
        if (token == null) MyApplication.vkAuth(this);
        else init();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void doWait(){
        //setContentView(R.layout.activity_main);
        Snackbar.make(findViewById(android.R.id.content), "Пожалуйста, подождите ...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    void init(){
        final MyApplication app=(MyApplication) getApplication();
        Timer timer=new Timer();
        startService(new Intent(this, NoticeService.class));
        TimerTask task=new TimerTask(){
            @Override
            public void run() {
                loadData();
            }
        };
        task.run();
        timer.schedule(task, 0, 60000);
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                JSONObject data=response.json;
                Me me=new Me(data);
                me.showBar(findViewById(R.id.nav_view));
                super.onComplete(response);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                token = res;
                Server.vkToken = res;
                SQLAdapter data=new SQLAdapter(context);
                data.putMap(MyApplication.authParams());
                init();
            }

            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public Map<String, Object> authParams() {
        return MyApplication.authParams();
    }

    @Override
    public void loadData() {

        filter = MyApplication.objFilter;
        if(tag==null)tag=filter;
        Map<String, Object> params = this.authParams();
        params.put("action", "glist_"+tag);

        MyApplication.ajax(params, this, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                lastData=json;
                ArrayList<Item>items=factory.getItemsByJson(json, filter);
                showData(items);
                cacheData("friend");
                cacheData("place");
                cacheData("unregistered");

            }
        });

    }

    public void cacheData(final String tag){
        Map<String, Object> params = this.authParams();
        params.put("action", "glist_"+tag);

        MyApplication.ajax(params, this, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                lastData=json;
                cache.put(tag, factory.getItemsByJson(json, factory.typeOfTag(tag)));
            }
        });
    }

    @Override
    public ItemAdapter getCustomAdapter(String itFilter){
        return factory.getCustomAdapter(cache.get(itFilter), factory.typeOfTag(itFilter));
    }

    @Override
    public ListView showCustomList(ItemAdapter customAdapter, String title){
        setContentView(R.layout.activity_main);
        ListView list=showEmptyList(title);
        list.setAdapter(customAdapter);
        return list;
    }

    @Override
    public ListView showEmptyList(String title){

        ListView list=(ListView) findViewById(R.id.list);
        return list;
    }


    public void showData(ArrayList<Item> data) {
        ((LinearLayout)findViewById(R.id.header)).removeAllViews();
        ListView list = (ListView) findViewById(R.id.list);
        adapter = factory.getAdapter(data);
        list.setAdapter(adapter);
        adapter.setItemClickListener(list);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.actions_check_in) {
            MyLocationListener.checkIn(this);
            Snackbar.make(findViewById(android.R.id.content), "Местоположение обновлено", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            MyApplication.objFilter = "people";
            tag="friend";
            this.loadData();
            setTitle("Друзья");

        } else if (id == R.id.nav_peoples) {
            MyApplication.objFilter = "people";
            tag=null;
            this.loadData();
            setTitle("Люди");

        } else if (id == R.id.nav_places) {
            MyApplication.objFilter = "place";
            tag=null;
            this.loadData();
            setTitle("Места");

        } else if (id == R.id.nav_meets) {
            MyApplication.objFilter = "meet";
            tag=null;
            this.loadData();
            setTitle("Встречи");

        } else if (id == R.id.nav_tasks) {
            MyApplication.objFilter = "task";
            tag=null;
            this.loadData();
            setTitle("Задачи");

        } else if (id == R.id.nav_isee) {
            Intent intent=new Intent(MainActivity.this, MyStatusActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
