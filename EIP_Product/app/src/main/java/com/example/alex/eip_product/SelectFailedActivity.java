package com.example.alex.eip_product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.alex.eip_product.adapter.KeyWordRecyclerViewAdapter;

import Component.AutoNewLineLayoutManager;

public class SelectFailedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    KeyWordRecyclerViewAdapter keyWordRecyclerViewAdapter;
    Button select_failed_confirm, select_failed_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_failed);
        recyclerView = findViewById(R.id.select_failed_recyclerview);
        keyWordRecyclerViewAdapter = new KeyWordRecyclerViewAdapter(this);
        AutoNewLineLayoutManager autoNewLineLayoutManager = new AutoNewLineLayoutManager(this);
        autoNewLineLayoutManager.setDivider(20);
        autoNewLineLayoutManager.setAloneViewType(KeyWordRecyclerViewAdapter.TYPE_HEADER);
        recyclerView.setLayoutManager(autoNewLineLayoutManager);
        recyclerView.setAdapter(keyWordRecyclerViewAdapter);


        initButton();
    }

    private void initButton() {
        select_failed_confirm = findViewById(R.id.select_failed_confirm);
        select_failed_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("failed_reason", keyWordRecyclerViewAdapter.getSelectItems());
                setResult(110,intent);
                finish();
            }
        });
        select_failed_cancel = findViewById(R.id.select_failed_cancel);
        select_failed_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
