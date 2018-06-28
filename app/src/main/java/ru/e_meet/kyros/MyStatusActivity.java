package ru.e_meet.kyros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import ru.e_meet.kyros.Items.Distance;
import ru.e_meet.kyros.Items.Status;
import ru.e_meet.e_meet.R;
import ru.e_meet.kyros.adapters.ItemAdapter;

public class MyStatusActivity extends AppCompatActivity implements MyActivity {
    Status myStatus;

    SeekBar likedBar;
    SeekBar friendsBar;
    SeekBar publicBar;
    Distance[] distances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_form);

        setTitle("Мой горизонт");


        distances=new Distance[12];
        distances[0]=new Distance(0, "Невидимый");
        distances[1]=new Distance(10, "до 10м");
        distances[2]=new Distance(50, "до 50м");
        distances[3]=new Distance(200, "до 200м");
        distances[4]=new Distance(500, "до 500м");
        distances[5]=new Distance(2000, "до 2км");
        distances[6]=new Distance(5000, "до 5км");
        distances[7]=new Distance(20000, "до 20км");
        distances[8]=new Distance(50000, "до 50км");
        distances[9]=new Distance(200000, "до 200км");
        distances[10]=new Distance(1000000, "до 1000км");
        distances[11]=new Distance(100000000, "Без горизонта");

        likedBar=(SeekBar)findViewById(R.id.likedStatus);
        friendsBar=(SeekBar)findViewById(R.id.friendStatus);
        publicBar=(SeekBar)findViewById(R.id.publicStatus);

        final TextView likedDistance=(TextView)findViewById(R.id.likedDistance);
        final TextView friendsDistance=(TextView)findViewById(R.id.friendsDistance);
        final TextView publicDistance=(TextView)findViewById(R.id.publicDistance);

        likedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                likedDistance.setText(distances[progress].lb);
                setStatus("liked", distances[progress].m);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {            }
        });

        friendsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                friendsDistance.setText(distances[progress].lb);
                setStatus("friends", distances[progress].m);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {            }
        });

        publicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                publicDistance.setText(distances[progress].lb);
                setStatus("public", distances[progress].m);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {            }
        });

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });

        loadData();

    }

    private void closeActivity(){
        super.finish();
    }

    @Override
    public void loadData() {
        Map<String, Object>params=MyApplication.authParams();
        params.put("action", "getMyStatus");
        MyApplication.ajax(params, this, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                myStatus=new Status();
                try {
                    json=json.getJSONObject("data");
                    myStatus.iseeFriends=json.getInt("isee_friends");
                    myStatus.iseeLiked=json.getInt("isee_liked");
                    myStatus.iseePublic=json.getInt("isee_public");
                    myStatus.seemeFriends=json.getInt("seeme_friends");
                    myStatus.seemeLiked=json.getInt("seeme_liked");
                    myStatus.seemePublic=json.getInt("seeme_public");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showData();
            }
        });

    }

    private void showData() {
        likedBar.setProgress(Distance.findByDistance(distances, myStatus.iseeLiked));
        friendsBar.setProgress(Distance.findByDistance(distances, myStatus.iseeFriends));
        publicBar.setProgress(Distance.findByDistance(distances, myStatus.iseePublic));
    }

    private void setStatus(String status, int value){
        Map<String, Object>params=MyApplication.authParams();
        params.put("action", "setMyStatus");
        params.put("stat_type", status);
        params.put("stat_value", value);
        MyApplication.ajax(params, this, new AjaxCallback<JSONObject>());
    }

    @Override
    public ItemAdapter getCustomAdapter(String itFilter) {
        return null;
    }

    @Override
    public ListView showCustomList(ItemAdapter customAdapter, String title) {
        return null;
    }

    @Override
    public ListView showEmptyList(String title) {
        return null;
    }

    @Override
    public Map<String, Object> authParams() {
        return null;
    }

    @Override
    public void reload() {

    }

    @Override
    public void doWait() {

    }
}
