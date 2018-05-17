package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.text.DecimalFormat;

import adapter.recyclerview.CountRecyclerViewAdapter;
import library.GetJsonData.ShopCartJsonData;

public class CountActivity extends AppCompatActivity implements View.OnClickListener {
    JSONObject json;
    RecyclerView recyclerView;
    CountRecyclerViewAdapter countRecyclerViewAdapter;
    private Toolbar toolbar;
    String token;
    Button  count_gotobuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        token = gv.getToken();
        initToolbar();
        initRecycleView();
    }

    private void initRecycleView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new ShopCartJsonData().getCheckout(token);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = findViewById(R.id.count_review);
                        recyclerView.setHasFixedSize(true);
                        countRecyclerViewAdapter = new CountRecyclerViewAdapter(CountActivity.this, json, token);
                        countRecyclerViewAdapter.setClickListener(new CountRecyclerViewAdapter.ClickListener() {
                            @Override
                            public void ItemClicked(int count) {

                            }
                        });
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(countRecyclerViewAdapter);
                        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
                        decoration.setDrawable(getResources().getDrawable(R.drawable.decoration_line));
                        recyclerView.addItemDecoration(decoration);
                        //IOS like
                       // OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
                        //第三方 彈跳效果
                      //  ElasticityHelper.setUpOverScroll(recyclerView, ORIENTATION.VERTICAL);
                        initButton();

                    }
                });
            }
        }).start();

    }


    private void initButton() {
        count_gotobuy = findViewById(R.id.count_gotobuy);
        count_gotobuy.setOnClickListener(this);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.count_toolbar);
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
            case R.id.count_gotobuy:
                break;
        }

    }
    private String getDeciamlString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }

}
