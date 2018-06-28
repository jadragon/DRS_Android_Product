package com.test.tw.wrokproduct.我的帳戶;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.test.tw.wrokproduct.R;

import adapter.recyclerview.ShopRecyclerViewAdapter;

public class SearchResultActivity extends AppCompatActivity {
    RecyclerView search_result_recyclerview;
    ShopRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        search_result_recyclerview = findViewById(R.id.search_result_recyclerview);

    }

    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("關鍵字");
    }
}
