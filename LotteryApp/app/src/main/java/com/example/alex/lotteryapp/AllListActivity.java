package com.example.alex.lotteryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.alex.lotteryapp.library.SQLiteDatabaseHandler;

import java.util.ArrayList;
import java.util.Map;

public class AllListActivity extends ToolbarActivity {
    Button btn_new, btn_ok;
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
        btn_new = findViewById(R.id.alllist_btn_new);
        btn_ok = findViewById(R.id.alllist_btn_ok);
        //list
        listView = findViewById(R.id.alllist_listview);


        items = db.getItems();
        initAdapter();


    }

    private void initAdapter(){
        items=db.getItems();
        adapter = new SimpleAdapter(this, items, R.layout.item_list, new String[]{"type", "gift", "number"}, new int[]{R.id.item_list_type, R.id.item_list_gift, R.id.item_list_number});
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.deleteItem(items.get(position).get("type"));
                initAdapter();
                return true;
            }
        });
    }
    private void initListener() {
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllListActivity.this, NewItemActivity.class));
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}
