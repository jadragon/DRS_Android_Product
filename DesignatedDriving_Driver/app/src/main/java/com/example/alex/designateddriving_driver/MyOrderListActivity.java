package com.example.alex.designateddriving_driver;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.example.alex.designateddriving_driver.RecyclerAdapter.MyOrderListRecyclerAdapter;
import com.example.alex.designateddriving_driver.Utils.AsyncTaskUtils;
import com.example.alex.designateddriving_driver.Utils.IDataCallBack;

import org.json.JSONObject;


public class MyOrderListActivity extends ToolbarActivity {
    private MyOrderListRecyclerAdapter myOrderListRecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orderlist);
        initToolbar("我的訂單", true);
        initSwipeLayout();
        initRecylcerView();
    }

    private void initSwipeLayout() {
        swipeRefreshLayout = findViewById(R.id.include_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        //設定刷新動作
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // setFilter();
            }

        });
    }

    private void initRecylcerView() {
        RecyclerView recyclerView = findViewById(R.id.include_recyclerview);
        myOrderListRecyclerAdapter = new MyOrderListRecyclerAdapter(this);
        recyclerView.setAdapter(myOrderListRecyclerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setFilter();
    }

    private void setFilter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return null;
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
