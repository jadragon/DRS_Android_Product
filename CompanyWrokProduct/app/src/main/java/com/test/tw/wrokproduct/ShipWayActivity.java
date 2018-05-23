package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import adapter.recyclerview.ShowShipWayRecyclerViewAdapter;
import library.GetJsonData.ShopCartJsonData;

public class ShipWayActivity extends AppCompatActivity implements View.OnClickListener {
    JSONObject json;
    String token,sno;
    Toolbar toolbar;
    TextView toolbar_title;
    RecyclerView recyclerView;
    ShowShipWayRecyclerViewAdapter showShipWayRecyclerViewAdapter;
    LinearLayoutManager layoutManager;
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
        recyclerView = findViewById(R.id.lv_products);
       showShipWayRecyclerViewAdapter = new ShowShipWayRecyclerViewAdapter(this, json);
        recyclerView.setHasFixedSize(true);
      layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(showShipWayRecyclerViewAdapter);
    }
    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        toolbar_title = findViewById(R.id.include_toolbar_title);
        toolbar_title.setText("運送方式");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        Log.e("onRestart","onRestart");
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new ShopCartJsonData().getStoreLogistics(token, sno);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShipWayRecyclerViewAdapter.setFilterAfterAdd(json);
                    }
                });
            }
        }).start();

    }
}
