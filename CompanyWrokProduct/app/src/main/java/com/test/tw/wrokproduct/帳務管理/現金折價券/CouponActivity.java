package com.test.tw.wrokproduct.帳務管理.現金折價券;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.recyclerview.CouponRecyclerAdapter;
import library.Component.ToolbarActivity;
import library.GetJsonData.BillJsonData;
import library.JsonDataThread;

public class CouponActivity extends ToolbarActivity {
    RecyclerView recyclerView;
    CouponRecyclerAdapter adapter;
    GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar(true, "現金折價券");
        initRecyclerView();
    }

    private void initRecyclerView() {
        findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView = findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CouponActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CouponRecyclerAdapter(this, null);
        recyclerView.setAdapter(adapter);
        setFilter();
    }

    public void setFilter() {
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                return new BillJsonData().getCoupon(gv.getToken());
            }

            @Override
            public void runUiThread(JSONObject json) {
                adapter.setFilter(json);
            }
        }.start();

    }
}
