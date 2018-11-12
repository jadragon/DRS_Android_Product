package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import adapter.recyclerview.ShowShipWayRecyclerViewAdapter;
import library.GetJsonData.ReCountJsonData;

public class ShipWayActivity extends AppCompatActivity implements View.OnClickListener {
    private String sno;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView recyclerView;
    private ShowShipWayRecyclerViewAdapter showShipWayRecyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private int count_type;
    private GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipway);
        gv = (GlobalVariable) getApplicationContext();
        sno = getIntent().getStringExtra("sno");
        count_type = getIntent().getIntExtra("count_type", 0);
        initToolbar();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new ReCountJsonData().getStoreLogistics(count_type, gv.getToken(), sno);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                requestData(jsonObject);
            }
        });

    }

    private void requestData(JSONObject json) {
        recyclerView = findViewById(R.id.lv_products);
        showShipWayRecyclerViewAdapter = new ShowShipWayRecyclerViewAdapter(this, json, count_type);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_10dp_invisble));
        recyclerView.addItemDecoration(decoration);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                @Override
                public JSONObject onTasking(Void... params) {
                    return new ReCountJsonData().getStoreLogistics(count_type, gv.getToken(), sno);
                }

                @Override
                public void onTaskAfter(JSONObject jsonObject) {
                    showShipWayRecyclerViewAdapter.setFilterAfterAdd(jsonObject);
                }
            });

        }
    }
}
