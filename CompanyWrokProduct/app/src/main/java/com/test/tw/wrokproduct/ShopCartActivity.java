package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import Util.StringUtil;
import adapter.recyclerview.ShopCartRecyclerViewAdapter;
import library.AnalyzeJSON.ResolveJsonData;
import library.Component.ToastMessageDialog;
import library.GetJsonData.ReCountJsonData;
import library.GetJsonData.ShopCartJsonData;
import library.JsonDataThread;

public class ShopCartActivity extends AppCompatActivity implements View.OnClickListener {
    private JSONObject json;
    private RecyclerView recyclerView;
    private ShopCartRecyclerViewAdapter shopCartRecyclerViewAdapter;
    private TextView toolbar_title;
    private TextView shop_cart_needpay, shopcart_txt_discount;
    private String moprno;
    private GlobalVariable gv;
    private Button shopcart_btn_coupon, shopcart_gotobuy;
    private EditText shopcart_edit_coupon;
    private int total, discount;
    private TabLayout shop_cart_tablayout;
    private String mvip = "1";
    ToastMessageDialog toastMessageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        gv = ((GlobalVariable) getApplicationContext());
        toastMessageDialog = new ToastMessageDialog(this);
        initToolbar();
        initTabLayout();
        initRecycleView();
    }

    private void initTabLayout() {
        shop_cart_tablayout = findViewById(R.id.shop_cart_tablayout);
        shop_cart_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mvip = (tab.getPosition() + 1) + "";
                setFilter();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setFilter() {
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                return new ShopCartJsonData().getCart(gv.getToken(), mvip);
            }

            @Override
            public void runUiThread(JSONObject json) {
                shopCartRecyclerViewAdapter.setMvip(mvip);
                shopCartRecyclerViewAdapter.setFilter(json);
                total = shopCartRecyclerViewAdapter.showPrice();
                shop_cart_needpay.setText("$" + StringUtil.getDeciamlString(total));
            }
        }.start();
    }

    private void initRecycleView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new ShopCartJsonData().getCart(gv.getToken(), "1");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView = findViewById(R.id.shop_cart_review);

                        shopCartRecyclerViewAdapter = new ShopCartRecyclerViewAdapter(ShopCartActivity.this, json);
                        shopCartRecyclerViewAdapter.setClickListener(new ShopCartRecyclerViewAdapter.ClickListener() {
                            @Override
                            public void ItemClicked(int count) {
                                total = count;
                                shop_cart_needpay.setText("$" + StringUtil.getDeciamlString((total + discount)));
                            }
                        });
                        recyclerView.setHasFixedSize(true);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(shopCartRecyclerViewAdapter);
                        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
                        decoration.setDrawable(getResources().getDrawable(R.drawable.decoration_line));
                        recyclerView.addItemDecoration(decoration);
                        //IOS like
                        //  OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
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
        shop_cart_needpay.setText("$" + StringUtil.getDeciamlString(total));
        shopcart_edit_coupon = findViewById(R.id.shopcart_edit_coupon);
        shopcart_txt_discount = findViewById(R.id.shopcart_txt_discount);
        shopcart_txt_discount.setText("$" + StringUtil.getDeciamlString(discount));
    }

    private void initButton() {
        shopcart_btn_coupon = findViewById(R.id.shopcart_btn_coupon);
        shopcart_btn_coupon.setOnClickListener(this);
        shopcart_gotobuy = findViewById(R.id.shopcart_gotobuy);
        shopcart_gotobuy.setOnClickListener(this);
    }

    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        toolbar_title = findViewById(R.id.include_toolbar_title);
        toolbar_title.setText("購物車");
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
            case R.id.shopcart_btn_coupon:
                if (shopcart_btn_coupon.getText().equals("發送")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //shopcart_edit_coupon.getText().toString()
                            json = new ShopCartJsonData().setCartDiscount(gv.getToken(), shopcart_edit_coupon.getText().toString(), total);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, String> datas = ResolveJsonData.getCartDiscount(json);
                                    if (datas != null) {
                                        moprno = datas.get("moprno");
                                        discount = Integer.parseInt(datas.get("mdiscount"));
                                        shopcart_txt_discount.setText("$" + StringUtil.getDeciamlString(discount));
                                        shop_cart_needpay.setText("$" + StringUtil.getDeciamlString((total + discount)));
                                        shopcart_btn_coupon.setText("取消");
                                    }
                                    try {
                                        toastMessageDialog.setMessageText(json.getString("Message"));
                                        toastMessageDialog.show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }).start();
                } else if (shopcart_btn_coupon.getText().equals("取消")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            json = new ShopCartJsonData().delCartDiscount(gv.getToken(), moprno);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (json.getBoolean("Success")) {
                                            discount = 0;
                                            shopcart_txt_discount.setText("$" + StringUtil.getDeciamlString(discount));
                                            shop_cart_needpay.setText("$" + StringUtil.getDeciamlString((total + discount)));
                                            shopcart_btn_coupon.setText("發送");
                                        }
                                        toastMessageDialog.setMessageText(json.getString("Message"));
                                        toastMessageDialog.show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }).start();
                }
                break;
            case R.id.shopcart_gotobuy:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean success = new ReCountJsonData().goCheckout(ReCountJsonData.COUNT, gv.getToken(), shopCartRecyclerViewAdapter.showMornoString()).getBoolean("Success");
                            if (success) {
                                Intent intent = new Intent(ShopCartActivity.this, CountActivity.class);
                                intent.putExtra("count_type", ReCountJsonData.COUNT);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setFilter();

    }

}
