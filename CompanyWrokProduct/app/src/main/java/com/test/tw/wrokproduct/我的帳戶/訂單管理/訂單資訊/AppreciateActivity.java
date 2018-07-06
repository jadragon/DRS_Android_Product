package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.test.tw.wrokproduct.R;

import adapter.recyclerview.AppreciateRecyclerViewAdapter;
import library.Component.ToolbarActivity;

public class AppreciateActivity extends ToolbarActivity {
    RecyclerView recyclerView;
    AppreciateRecyclerViewAdapter adapter;
    Button appreciate_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciate);
        initToolbar(true, "給予評價");
        initRecyclerView();
        appreciate_confirm=findViewById(R.id.appreciate_confirm);
        appreciate_confirm.requestFocus();
    }

    private void initRecyclerView() {
        findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView = findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppreciateActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AppreciateRecyclerViewAdapter(AppreciateActivity.this, null);
        recyclerView.setAdapter(adapter);
    }


}
