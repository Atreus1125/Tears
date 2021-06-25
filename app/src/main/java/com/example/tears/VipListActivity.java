package com.example.tears;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Map;

public class VipListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> list;

    Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_list);
        listView = findViewById(R.id.vipList);

        list = util.getAllFiles(getApplicationContext().getExternalFilesDir(null).toString(), "mp3");
        simpleAdapter = new SimpleAdapter(this, list, R.layout.activity_item,
                new String[] {"name", "artist"}, new int[] {R.id.name, R.id.artist});
        listView.setAdapter(simpleAdapter);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("2015", String.valueOf(position));//列表点击反馈
        int musicId[] = new int[parent.getCount()];
        String musicPath[] = new String[parent.getCount()];
        for (int i = 0; i < parent.getCount(); i++) {
            musicId[i] = ((Map<String, Integer>) parent.getItemAtPosition(i)).get("id");
            musicPath[i] = ((Map<String, String>) parent.getItemAtPosition(i)).get("path");
            Log.i("2016", String.valueOf(parent.getCount()));
            Log.i("2017", String.valueOf(musicId[i]));
        }
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("musicId", musicId);
        intent.putExtra("musicPath", musicPath);
        intent.putExtra("position", position);
        intent.putExtra("sourceTag", 1);
        startActivity(intent);
    }
}