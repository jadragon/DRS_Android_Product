package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import org.json.JSONObject;

import adapter.listview.OneExpandAdapter;
import library.GetJsonData.ShopCartJsonData;

public class ShipWayActivity extends AppCompatActivity implements View.OnClickListener {
    JSONObject json;
    String token,sno;
    Toolbar toolbar;
    OneExpandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipway);
       // token = "I0JN9@_fTxybt/YuH1j1Ceg==";
       // sno="QKwd9IJZjuUfKZa00V9CjQ==";
        token=getIntent().getStringExtra("token");
        sno=getIntent().getStringExtra("sno");
        initToolbar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new ShopCartJsonData().getStoreLogistics(token, sno);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestData();
                    }
                });
            }
        }).start();

    }

    private void requestData() {
        ListView lvProduct = findViewById(R.id.lv_products);
        lvProduct.setDivider(null);
       adapter = new OneExpandAdapter(this, json);
        lvProduct.setAdapter(adapter);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.shipway_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -1://toolbar
                finish();
                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new ShopCartJsonData().getStoreLogistics(token, sno);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setFilter(json);
                    }
                });
            }
        }).start();

    }
}
