package com.example.alex.posdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.alex.posdemo.adapter.recylclerview.SelectBrandAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;

public class SelectBrandActivity extends Activity {
RecyclerView select_brand_recycleview;
    SelectBrandAdapter selectBrandAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_brand);
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return null;
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initRecyclerView(jsonObject);
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
        select_brand_recycleview.setAdapter(selectBrandAdapter);
    }
}
