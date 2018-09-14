package com.example.alex.eip_product;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.alex.eip_product.adapter.KeyWordRecyclerViewAdapter;

import Component.AutoNewLineLayoutManager;

public class SelectFailedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
KeyWordRecyclerViewAdapter keyWordRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_failed);
        recyclerView = findViewById(R.id.select_failed_recyclerview);
        keyWordRecyclerViewAdapter=new KeyWordRecyclerViewAdapter(this);
        AutoNewLineLayoutManager   autoNewLineLayoutManager = new AutoNewLineLayoutManager(this);
        autoNewLineLayoutManager.setAloneViewType(KeyWordRecyclerViewAdapter.TYPE_HEADER);
        recyclerView.setLayoutManager(autoNewLineLayoutManager);
        recyclerView.setAdapter(keyWordRecyclerViewAdapter);
    }
}
