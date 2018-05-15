package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

import adapter.recyclerview.ShopCartRecyclerViewAdapter;
import library.GetInformationByPHP;
import library.ResolveJsonData;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class ShopCartActivity extends AppCompatActivity implements View.OnClickListener {
    JSONObject json;
    RecyclerView recyclerView;
    ShopCartRecyclerViewAdapter shopCartRecyclerViewAdapter;
    private Toolbar toolbar;
    TextView shop_cart_needpay, shopcart_txt_discount;
    String token, moprno;
    Button shopcart_btn_coupon, shopcart_btn_continue, shopcart_gotobuy;
    EditText shopcart_edit_coupon;
    int total, discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        token = "zI6OIYlbhfPKyhbchdOiGg==";
        initToolbar();

        initRecycleView();
    }

    private void initRecycleView() {
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
                        shopCartRecyclerViewAdapter = new ShopCartRecyclerViewAdapter(ShopCartActivity.this, json, token);
                        shopCartRecyclerViewAdapter.setClickListener(new ShopCartRecyclerViewAdapter.ClickListener() {
                            @Override
                            public void ItemClicked(int count) {
                                total = count;
                                shop_cart_needpay.setText("$" + getDeciamlString((total + discount) + ""));
                            }
                        });
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(shopCartRecyclerViewAdapter);
                        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
                        decoration.setDrawable(getResources().getDrawable(R.drawable.decoration_line));
                        recyclerView.addItemDecoration(decoration);
                        //IOS like
                        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
                        //第三方 彈跳效果
                        //  ElasticityHelper.setUpOverScroll(recyclerView, ORIENTATION.VERTICAL);
                        initTextView();
                        initButton();

                    }
                });
            }
        }).start();

    }

    private void initTextView() {
        shop_cart_needpay = findViewById(R.id.shop_cart_needpay);
        total = shopCartRecyclerViewAdapter.showPrice();
        shop_cart_needpay.setText("$" + getDeciamlString(total + ""));
        shopcart_edit_coupon = findViewById(R.id.shopcart_edit_coupon);
        shopcart_txt_discount = findViewById(R.id.shopcart_txt_discount);
        shopcart_txt_discount.setText("$" + getDeciamlString(discount + ""));
    }

    private void initButton() {
        shopcart_btn_coupon = findViewById(R.id.shopcart_btn_coupon);
        shopcart_btn_coupon.setOnClickListener(this);
        shopcart_btn_continue = findViewById(R.id.shopcart_btn_continue);
        shopcart_btn_continue.setOnClickListener(this);
        shopcart_gotobuy = findViewById(R.id.shopcart_gotobuy);
        shopcart_gotobuy.setOnClickListener(this);
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.shopcart_toolbar);
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
            case R.id.shopcart_btn_coupon:
                if (shopcart_btn_coupon.getText().equals("發送")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //shopcart_edit_coupon.getText().toString()
                            final Map<String, String> datas = ResolveJsonData.getCartDiscount(new GetInformationByPHP().setCartDiscount(token, shopcart_edit_coupon.getText().toString()));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (datas != null) {
                                        moprno = datas.get("moprno");
                                        discount = Integer.parseInt(datas.get("mdiscount"));
                                        shopcart_txt_discount.setText("$" + getDeciamlString(discount + ""));
                                        shop_cart_needpay.setText("$" + getDeciamlString((total + discount) + ""));
                                        shopcart_btn_coupon.setText("取消");
                                    }
                                }
                            });
                        }
                    }).start();
                } else if (shopcart_btn_coupon.getText().equals("取消")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final boolean success = ResolveJsonData.checkSuccess(new GetInformationByPHP().delCartDiscount(token, moprno));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        discount = 0;
                                        shopcart_txt_discount.setText("$" + getDeciamlString(discount + ""));
                                        shop_cart_needpay.setText("$" + getDeciamlString((total + discount) + ""));
                                        shopcart_btn_coupon.setText("發送");
                                    }
                                }
                            });
                        }
                    }).start();
                }
                break;
            case R.id.shopcart_btn_continue:
                finish();
                break;
            case R.id.shopcart_gotobuy:
                Log.e("Morno", "" + shopCartRecyclerViewAdapter.showMornoArray() + "\n" + shopCartRecyclerViewAdapter.showMornoString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean success = new GetInformationByPHP().goCheckout(token, shopCartRecyclerViewAdapter.showMornoString()).getBoolean("Success");
                            if (success)
                                startActivity(new Intent(ShopCartActivity.this, CountActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }

    }

    private String getDeciamlString(String str) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(Double.parseDouble(str));
    }
}
