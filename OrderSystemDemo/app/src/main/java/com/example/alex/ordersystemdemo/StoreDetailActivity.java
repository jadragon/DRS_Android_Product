package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.RestaurantApi;
import com.example.alex.ordersystemdemo.RecyclerViewAdapter.MenuListAdapter;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONObject;

public class StoreDetailActivity extends ToolbarAcitvity {
    private RecyclerView recyclerView;
    private MenuListAdapter menuListAdapter;
    private String s_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        s_id = getIntent().getStringExtra("s_id");
        initToolbar("食富通超坑錢飯館", true, false);
        initRecyclerView();

    }

    private void initRecyclerView() {
        findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView = findViewById(R.id.include_recyclerview);
        menuListAdapter = new MenuListAdapter(this, null);
        recyclerView.setAdapter(menuListAdapter);

        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new RestaurantApi().menu(s_id);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    menuListAdapter.setFilter(jsonObject);
                } else {
                }
                Log.e("STORE", jsonObject + "");
            }
        });

    }
}
