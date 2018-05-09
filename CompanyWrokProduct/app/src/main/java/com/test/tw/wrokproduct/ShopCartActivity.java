package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import adapter.recyclerview.ShopCartRecyclerViewAdapter;
import library.GetInformationByPHP;
import library.ResolveJsonData;

public class ShopCartActivity extends AppCompatActivity {
    JSONObject json;
    RecyclerView recyclerView;
    ShopCartRecyclerViewAdapter shopCartRecyclerViewAdapter;
    private Toolbar toolbar;
TextView shop_cart_needpay;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        token="zI6OIYlbhfPKyhbchdOiGg==";
        initToo();
        shop_cart_needpay=findViewById(R.id.shop_cart_needpay);
        testJson();
    }

    private void testJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new GetInformationByPHP().getCart(token);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TestJSONONONONON", ResolveJsonData.getCartItemArray(json) + "");
                        recyclerView = findViewById(R.id.shop_cart_review);
                        recyclerView.setHasFixedSize(true);
                        //recyclerView.setItemAnimator(new DefaultItemAnimatorV2());
                        shopCartRecyclerViewAdapter = new ShopCartRecyclerViewAdapter(getApplicationContext(), json,token);
                        shopCartRecyclerViewAdapter.setClickListener(new ShopCartRecyclerViewAdapter.ClickListener() {
                            @Override
                            public void ItemClicked(int count) {
                                shop_cart_needpay.setText("$"+count);
                            }
                        });

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(shopCartRecyclerViewAdapter);
                        shop_cart_needpay.setText(shopCartRecyclerViewAdapter.showPrice()+"");
                        DividerItemDecoration decoration=new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL);
                        decoration.setDrawable(getResources().getDrawable(R.drawable.decoration_line));
                        recyclerView.addItemDecoration(decoration);
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
