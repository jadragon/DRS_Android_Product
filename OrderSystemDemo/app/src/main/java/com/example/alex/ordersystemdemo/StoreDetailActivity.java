package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.example.alex.ordersystemdemo.RecyclerViewAdapter.MenuListAdapter;

public class StoreDetailActivity extends ToolbarAcitvity {
RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        initToolbar("食富通超坑錢飯館",true,false);
        initRecyclerView();

    }
    private void initRecyclerView() {
        findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView = findViewById(R.id.include_recyclerview);
        recyclerView.setAdapter(new MenuListAdapter(this, null));
    }
}
