package ru.e_meet.kyros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ru.e_meet.kyros.Items.Meet;
import ru.e_meet.kyros.Items.Task;
import ru.e_meet.e_meet.R;
import ru.e_meet.kyros.adapters.InboxAdapter;
import ru.e_meet.kyros.adapters.ItemAdapter;
import ru.e_meet.kyros.adapters.MeetAdapter;
import ru.e_meet.kyros.adapters.TaskAdapter;
import ru.e_meet.kyros.notices.Notice;
import ru.e_meet.kyros.service.NoticeService;

public class NoticeActivity extends AppCompatActivity {
    int noticeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);
        Intent intent=getIntent();
        noticeId=intent.getIntExtra("noticeId", 10000);
        if(noticeId!=10000)loadData();
    }

    private void loadData() {
        Notice notice= NoticeService.notices.get(noticeId);
        setContentView(notice.customView);
        Item item=notice.item;
        setAdapter(notice);
    }

    View setAdapter (Notice notice){
        switch (notice.type){
            case "meet": return MeetAdapter.makeDetailsContent(findViewById(android.R.id.content), this, (Meet)notice.item);
            case "task": return TaskAdapter.makeDetailsContent(findViewById(android.R.id.content), this, (Task)notice.item);
            case "inbox": return InboxAdapter.makeDetailsContent(findViewById(android.R.id.content), this, (Meet)notice.item);
            default: return ItemAdapter.makeDefaultContent(findViewById(android.R.id.content), notice.item);
        }
    }
}
