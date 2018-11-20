package com.example.alex.lotteryapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

import java.util.ArrayList;
import java.util.Map;

public class AllListActivity extends ToolbarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    Button alllist_btn_export, alllist_btn_reset;
    ListView listView;
    SQLiteDatabaseHandler db;
    SimpleAdapter adapter;

    TextView type;
    String[] array;
    ArrayList<Map<String, String>> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);
        array = getResources().getStringArray(R.array.type_list);
        initToolbar(true, "項目列表");
        db = new SQLiteDatabaseHandler(this);
        initView();
        initListener();
    }

    private void initView() {
        //btn
        alllist_btn_export = findViewById(R.id.alllist_btn_export);
        alllist_btn_reset = findViewById(R.id.alllist_btn_reset);
        //list
        listView = findViewById(R.id.alllist_listview);
        items = db.getItems();
        initAdapter();


    }

    private void initAdapter() {
        items = db.getItems();
        adapter = new SimpleAdapter(this, items, R.layout.item_list, new String[]{"type", "gift", "number"}, new int[]{R.id.item_list_type, R.id.item_list_gift, R.id.item_list_number});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    private void initListener() {
        alllist_btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AllListActivity.this, "輸出成功", Toast.LENGTH_SHORT).show();
            }
        });
        alllist_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.resetAllAward();
                initAdapter();
                Toast.makeText(AllListActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String type = items.get(position).get("type");
        ArrayList<String> winners = db.getWinnerNames(type);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(type);
        builder.setMessage(winners + "");
        builder.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
