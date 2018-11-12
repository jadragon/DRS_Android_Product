package com.test.tw.wrokproduct.購物車;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.tw.wrokproduct.CountActivity;
import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.購物車.pojo.ShopcartCouponPojo;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import Util.StringUtil;
import adapter.recyclerview.ShopCartRecyclerViewAdapter;
import library.AnalyzeJSON.AnalyzeShopCart;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.ToastMessageDialog;
import library.Component.ToolbarActivity;
import library.GetJsonData.ReCountJsonData;
import library.GetJsonData.ShopCartJsonData;

public class ShopCartActivity extends ToolbarActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ShopCartRecyclerViewAdapter shopCartRecyclerViewAdapter;
    private TextView shop_cart_needpay, shopcart_txt_discount;
    private GlobalVariable gv;
    private Button shopcart_btn_coupon, shopcart_gotobuy;
    private EditText shopcart_edit_coupon;
    private int total;
    private TabLayout shop_cart_tablayout;
    private String mvip = "1";
    private  ToastMessageDialog toastMessageDialog;
    private  ShopcartCouponPojo shopcartCouponPojo;
    private View no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        gv = ((GlobalVariable) getApplicationContext());
        toastMessageDialog = new ToastMessageDialog(this);
        initToolbar(true, "購物車");
        initTabLayout();
        initRecycleView();
        initCouponButton();
        initTextView();
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
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new ShopCartJsonData().getCart(gv.getToken(), mvip);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                shopcartCouponPojo = AnalyzeShopCart.getCartCoupon(jsonObject) != null ? AnalyzeShopCart.getCartCoupon(jsonObject) : new ShopcartCouponPojo();
                shopCartRecyclerViewAdapter.setMvip(mvip);
                shopCartRecyclerViewAdapter.setFilter(jsonObject);
                setPrice();
            }
        });
    }

    private void initRecycleView() {
        no_data = findViewById(R.id.include_no_data);
        recyclerView = findViewById(R.id.include_recyclerview);
        findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView.setHasFixedSize(true);
        shopCartRecyclerViewAdapter = new ShopCartRecyclerViewAdapter(ShopCartActivity.this, null);
        shopCartRecyclerViewAdapter.setClickListener(new ShopCartRecyclerViewAdapter.ClickListener() {
            @Override
            public void ItemClicked(int count) {
                total = count;
                if (count < shopcartCouponPojo.getMcost()) {
                    cancelCoupon();
                } else {
                    shop_cart_needpay.setText("$" + StringUtil.getDeciamlString((total + shopcartCouponPojo.getMdiscount())));
                }
            }
        });
        shopCartRecyclerViewAdapter.setDataChangeListener(new ShopCartRecyclerViewAdapter.DataChangeListener() {
            @Override
            public void ItemClicked() {
                if (shopCartRecyclerViewAdapter.getItemCount() > 0) {
                    no_data.setVisibility(View.INVISIBLE);
                } else {
                    no_data.setVisibility(View.VISIBLE);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(shopCartRecyclerViewAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.decoration_line));
        recyclerView.addItemDecoration(decoration);
        setFilter();
        //IOS like
        //  OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        //第三方 彈跳效果
        //  ElasticityHelper.setUpOverScroll(recyclerView, ORIENTATION.VERTICAL);


                /*
                json = new ShopCartJsonData().getCart(gv.getToken(), "1");
                shopcartCouponPojo = AnalyzeShopCart.getCartCoupon(json) != null ? AnalyzeShopCart.getCartCoupon(json) : new ShopcartCouponPojo();
                */


    }

    private void initTextView() {
        shop_cart_needpay = findViewById(R.id.shop_cart_needpay);
        shopcart_edit_coupon = findViewById(R.id.shopcart_edit_coupon);
        shopcart_txt_discount = findViewById(R.id.shopcart_txt_discount);
    }

    private void setPrice() {
        if (shopcartCouponPojo.getMcoupon() != null && !shopcartCouponPojo.getMcoupon().equals("")) {
            shopcart_txt_discount.setText("$" + StringUtil.getDeciamlString(shopcartCouponPojo.getMdiscount()));
            shopcart_btn_coupon.setText("取消");
            shop_cart_needpay.setText("$" + StringUtil.getDeciamlString((total + shopcartCouponPojo.getMdiscount())));
        } else {
            shopcart_txt_discount.setText("$" + StringUtil.getDeciamlString(0));
            shopcart_btn_coupon.setText("發送");
        }
        total = shopCartRecyclerViewAdapter.showPrice();
        shop_cart_needpay.setText("$" + StringUtil.getDeciamlString(total + shopcartCouponPojo.getMdiscount()));
    }

    private void initCouponButton() {
        shopcart_btn_coupon = findViewById(R.id.shopcart_btn_coupon);
        shopcart_btn_coupon.setOnClickListener(this);
        shopcart_gotobuy = findViewById(R.id.shopcart_gotobuy);
        shopcart_gotobuy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -1://toolbar
                finish();
                break;
            case R.id.shopcart_btn_coupon:
                if (shopcart_btn_coupon.getText().equals("發送")) {
                    if (total > 0) {
                        sendCoupon();
                    } else {
                        toastMessageDialog.setMessageText("請選擇要購買的商品");
                        toastMessageDialog.confirm();
                    }
                } else if (shopcart_btn_coupon.getText().equals("取消")) {
                    cancelCoupon();
                }
                break;
            case R.id.shopcart_gotobuy:
                if (total > 0) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new ReCountJsonData().goCheckout(ReCountJsonData.COUNT, gv.getToken(), shopCartRecyclerViewAdapter.showMornoString());
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                Intent intent = new Intent(ShopCartActivity.this, CountActivity.class);
                                intent.putExtra("count_type", ReCountJsonData.COUNT);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    toastMessageDialog.setMessageText("請選擇要購買的商品");
                    toastMessageDialog.confirm();
                }
                break;
        }


    }

    private void sendCoupon() {
        if (!shopcart_edit_coupon.getText().toString().equals("")) {
            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                @Override
                public JSONObject onTasking(Void... params) {
                    return new ShopCartJsonData().setCartDiscount(gv.getToken(), shopcart_edit_coupon.getText().toString(), total);
                }

                @Override
                public void onTaskAfter(JSONObject jsonObject) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        shopcartCouponPojo = AnalyzeShopCart.getCartCoupon(jsonObject) != null ? AnalyzeShopCart.getCartCoupon(jsonObject) : new ShopcartCouponPojo();
                        shopcart_txt_discount.setText("$" + StringUtil.getDeciamlString(shopcartCouponPojo.getMdiscount()));
                        shop_cart_needpay.setText("$" + StringUtil.getDeciamlString((total + shopcartCouponPojo.getMdiscount())));
                        shopcart_btn_coupon.setText("取消");
                    } else {
                        toastMessageDialog.setMessageText(AnalyzeUtil.getMessage(jsonObject));
                        toastMessageDialog.show();
                    }

                }
            });
        } else {
            toastMessageDialog.setMessageText("請輸入折扣碼");
            toastMessageDialog.confirm();
        }
    }

    private void cancelCoupon() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new ShopCartJsonData().delCartDiscount(gv.getToken(), shopcartCouponPojo.getMoprno());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    shopcartCouponPojo = AnalyzeShopCart.getCartCoupon(jsonObject) != null ? AnalyzeShopCart.getCartCoupon(jsonObject) : new ShopcartCouponPojo();
                    shopcart_txt_discount.setText("$" + StringUtil.getDeciamlString(shopcartCouponPojo.getMdiscount()));
                    shop_cart_needpay.setText("$" + StringUtil.getDeciamlString((total + shopcartCouponPojo.getMdiscount())));
                    shopcart_btn_coupon.setText("發送");
                    shopcart_edit_coupon.setText("");
                }
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setFilter();
    }

}
