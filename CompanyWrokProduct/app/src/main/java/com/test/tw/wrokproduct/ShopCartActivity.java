package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import adapter.recyclerview.ShopCartRecyclerViewAdapter;
import library.GetInformationByPHP;
import library.ResolveJsonData;

public class ShopCartActivity extends AppCompatActivity {
    JSONObject json;
    RecyclerView recyclerView;
    ShopCartRecyclerViewAdapter shopCartRecyclerViewAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        initToo();
        testJson();
    }

    private void testJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new GetInformationByPHP().getCart("zI6OIYlbhfPKyhbchdOiGg==");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TestJSONONONONON", ResolveJsonData.getCartItemArray(json) + "");
                        recyclerView = findViewById(R.id.shop_cart_review);
                        recyclerView.setHasFixedSize(true);
                        //recyclerView.setItemAnimator(new DefaultItemAnimatorV2());
                        shopCartRecyclerViewAdapter = new ShopCartRecyclerViewAdapter(getApplicationContext(), json);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(shopCartRecyclerViewAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    }
                });
            }
        }).start();

    }

    private void initToo() {
        //Toolbar 建立
        toolbar = findViewById(R.id.shopcart_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
