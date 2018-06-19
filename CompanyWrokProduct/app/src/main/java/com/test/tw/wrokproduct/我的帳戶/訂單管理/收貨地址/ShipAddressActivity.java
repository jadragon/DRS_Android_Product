package com.test.tw.wrokproduct.我的帳戶.訂單管理.收貨地址;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.recyclerview.ShipAddressRecyclerAdapter;
import library.GetJsonData.LogisticsJsonData;

public class ShipAddressActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ShipAddressRecyclerAdapter adapter;
    JSONObject json;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_address);
        token=((GlobalVariable)getApplicationContext()).getToken();
        initToolbar();
        initRecyclerView();


    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.ship_address_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShipAddressActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                json=new LogisticsJsonData().getLogistics(token,0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ShipAddressRecyclerAdapter(ShipAddressActivity.this, json);
                        recyclerView.setAdapter(adapter);
                    }
                });

            }
        }).start();


    }

    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("收貨地址");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
