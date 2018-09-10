package com.example.alex.posdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.alex.posdemo.adapter.recylclerview.SelectBrandAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.JsonApi.NewBrandApi;

public class SelectBrandActivity extends Activity {
    RecyclerView select_brand_recycleview;
    SelectBrandAdapter selectBrandAdapter;
    View select_brand_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_brand);
        initButton();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new NewBrandApi().typeRel();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initRecyclerView(jsonObject);
            }
        });

    }

    private void initButton() {
        select_brand_back = findViewById(R.id.select_brand_back);
        select_brand_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("title", selectBrandAdapter.getSelectTitle());
                intent.putExtra("code", selectBrandAdapter.getSelectCode());
                setResult(200, intent);
                finish();
            }
        });
    }


    private void initRecyclerView(JSONObject jsonObject) {
        if (select_brand_recycleview != null && selectBrandAdapter != null) {
            selectBrandAdapter.setFilter(jsonObject);
            return;
        }
        select_brand_recycleview = findViewById(R.id.select_brand_recycleview);
        selectBrandAdapter = new SelectBrandAdapter(this, jsonObject);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        select_brand_recycleview.setLayoutManager(layoutManager);
        select_brand_recycleview.setAdapter(selectBrandAdapter);
    }

}
