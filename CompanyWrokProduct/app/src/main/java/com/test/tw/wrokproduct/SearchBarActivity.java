package com.test.tw.wrokproduct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import adapter.recyclerview.KeyWordRecyclerViewAdapter;
import library.component.AutoNewLineLayoutManager;

public class SearchBarActivity extends AppCompatActivity {
RecyclerView search_bar_recyclerview;
KeyWordRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        search_bar_recyclerview=findViewById(R.id.search_bar_recyclerview);
        adapter=new KeyWordRecyclerViewAdapter(this,null);
        search_bar_recyclerview.setHasFixedSize(true);
        AutoNewLineLayoutManager layoutManager=new AutoNewLineLayoutManager(this);
        layoutManager.setDivider(10);
        search_bar_recyclerview.setLayoutManager(layoutManager);
        search_bar_recyclerview.setAdapter(adapter);
    }
}
