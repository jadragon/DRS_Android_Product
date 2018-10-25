package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.OrderApi;
import com.example.alex.ordersystemdemo.API.List.RestaurantApi;
import com.example.alex.ordersystemdemo.RecyclerViewAdapter.MenuListAdapter;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONObject;

import java.util.Map;

public class StoreDetailActivity extends ToolbarAcitvity {
    private RecyclerView recyclerView;
    private MenuListAdapter menuListAdapter;
    private String s_id, name;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        gv = (GlobalVariable) getApplicationContext();
        s_id = getIntent().getStringExtra("s_id");
        name = getIntent().getStringExtra("name");
        initToolbar(name, true, false);
        initRecyclerView();
        initButton();

    }

    private void initButton() {
        findViewById(R.id.store_detail_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, String> map = menuListAdapter.getStudentInfo();

                if (!(map.get("name").equals("") || map.get("phone").equals("") || map.get("address").equals(""))) {
                    if (menuListAdapter.getTotalMoney() > 0) {
                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                            @Override
                            public JSONObject onTasking(Void... params) {

                                return new OrderApi().checkout(gv.getToken(), s_id, map.get("name"), map.get("phone"), map.get("address"), menuListAdapter.getContent(), menuListAdapter.getTotalMoney() + "");
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                    finish();
                                }
                                Toast.makeText(StoreDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(StoreDetailActivity.this, "請選擇要購買的餐點", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StoreDetailActivity.this, "請確認資料是否填寫完整", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
                }
            }
        });

    }
}
