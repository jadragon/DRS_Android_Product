package com.example.alex.eip_product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.alex.eip_product.adapter.KeyWordRecyclerViewAdapter;

import java.lang.reflect.Array;
import java.util.Arrays;

import Component.AutoNewLineLayoutManager;

public class SelectFailedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    KeyWordRecyclerViewAdapter keyWordRecyclerViewAdapter;
    Button select_failed_confirm, select_failed_cancel;
    private boolean type1, type2, type3, type4;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_failed);
        intent = getIntent();
        recyclerView = findViewById(R.id.select_failed_recyclerview);
        type1 = intent.getBooleanExtra("type1", false);
        type2 = intent.getBooleanExtra("type2", false);
        type3 = intent.getBooleanExtra("type3", false);
        type4 = intent.getBooleanExtra("type4", false);

        keyWordRecyclerViewAdapter = new KeyWordRecyclerViewAdapter(this, type1, type2, type3, type4, true, false);
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
                intent.putExtra("ReasonCode", keyWordRecyclerViewAdapter.getSelectFailNums());
                intent.putExtra("ReasonDescr", keyWordRecyclerViewAdapter.getSelectFailDescription());
                setResult(110, intent);
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
