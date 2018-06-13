package com.test.tw.wrokproduct;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import adapter.recyclerview.AddCartRecyclerViewAdapter;
import adapter.recyclerview.ShipsWaysRecyclerViewAdapter;
import adapter.viewpager.PcContentPagerAdapter;
import adapter.viewpager.PcContentWebViewPagerAdapter;
import library.AnalyzeJSON.ResolveJsonData;
import library.AppManager;
import library.Component.MyViewPager;
import library.GetJsonData.GetInformationByPHP;
import library.GetJsonData.ShopCartJsonData;
import library.component.ToastMessageDialog;
import pojo.ProductInfoPojo;

public class PcContentActivity extends AppCompatActivity {
    DisplayMetrics dm;
    private ViewPager viewPager;
    private MyViewPager webviewpager;
    private JSONObject json;
    private PcContentPagerAdapter adapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    LinearLayout ship_ways;
    TextView  pccontent_txt_descs, pccontent_txt_rsprice, pccontent_txt_rprice, shopcart_txt_count;
    String pno;
    RecyclerView recyclerView;
    ImageView heart, pccontent_btn_home, pccontent_img_star;
    View enable_background;
    LinearLayout shipways_layout;
    private PopupWindow popWin = null; // 弹出窗口
    private View popView = null; // 保存弹出窗口布局
    Button pccontent_btn_addshopcart, pccontent_btn_buynow, shop_cart_confirm;
    Map<String, String> product_info;
    int count, max = 0;
    int default_color;
    Button shopcart_btn_increase, shopcart_btn_decrease;
    int myRecylcerViewHeight;
    ProductInfoPojo productInfoPojo;
    int[] stars = {R.drawable.star0_2, R.drawable.star1_2, R.drawable.star2_2, R.drawable.star3_2, R.drawable.star4_2, R.drawable.star5_2};
    String pino;
    String Message;
    String token;
    ToastMessageDialog toastMessageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pccontent);
        productInfoPojo = (ProductInfoPojo) getIntent().getSerializableExtra("productInfoPojo");
        GlobalVariable gv = (GlobalVariable) getApplicationContext();
        token = gv.getToken();
        pno = productInfoPojo.getPno();
        dm = getResources().getDisplayMetrics();
        toastMessageDialog = new ToastMessageDialog(this);
        AppManager.getAppManager().addActivity(this);
        initToolbar();
        setText();
        initHome();
        initFavorate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new GetInformationByPHP().getPcontent(token,pno);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initViewPager();
                        initTabLayout();
                        initShipWay();
                        initAddShopCart();
                        initBuyNow();
                        initEnableBackground();
                    }
                });

            }
        }).start();
    }

    //設定價錢
    public void setText() {
        //描述
        pccontent_txt_descs = findViewById(R.id.pccontent_txt_descs);
        pccontent_txt_descs.setText(productInfoPojo.getDescs());
        //售價
        pccontent_txt_rsprice = findViewById(R.id.pccontent_txt_rsprice);
        pccontent_txt_rsprice.setText("$" + getDeciamlString(productInfoPojo.getRsprice()));
        //牌價
        pccontent_txt_rprice = findViewById(R.id.pccontent_txt_rprice);
        pccontent_txt_rprice.setPaintFlags(pccontent_txt_rprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        pccontent_txt_rprice.setText("$" + getDeciamlString(productInfoPojo.getRprice()));

        if (productInfoPojo.getRsprice().equals(productInfoPojo.getRprice()))
            pccontent_txt_rprice.setVisibility(View.INVISIBLE);
        //星星
        pccontent_img_star = findViewById(R.id.pccontent_img_star);
        pccontent_img_star.setImageResource(stars[Integer.parseInt(productInfoPojo.getScore())]);

    }

    //產品圖片
    private void initViewPager() {
        viewPager = findViewById(R.id.adView);
        adapter = new PcContentPagerAdapter(getWindow().getDecorView(), json, dm.widthPixels, dm.widthPixels);
        viewPager.setAdapter(adapter);
    }

    //初始化加入購物車
    private void initAddShopCart() {
        pccontent_btn_addshopcart = findViewById(R.id.pccontent_btn_addshopcart);

        pccontent_btn_addshopcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                max = 0;
                count = 0;
                popUpView(getResources().getColor(R.color.orange), 0);
            }
        });
    }

    //初始化立即購買
    private void initBuyNow() {
        pccontent_btn_buynow = findViewById(R.id.pccontent_btn_buynow);

        pccontent_btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                max = 0;
                count = 1;
                popUpView(getResources().getColor(R.color.red), 1);
            }
        });
    }

    //設定彈出視窗
    public void popUpView(int color, final int type) {
        this.default_color = color;
        product_info = ResolveJsonData.getPcContentInformation(json);
        popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.shopcart_layout, null);
        //設定產品資訊
        ImageLoader.getInstance().displayImage(product_info.get("img"), ((ImageView) popView.findViewById(R.id.shopcart_img)));
        ((TextView) popView.findViewById(R.id.shopcart_txt_title)).setText(product_info.get("pname"));
        ((TextView) popView.findViewById(R.id.shopcart_txt_sprice)).setText("$" + product_info.get("rsprice"));
        //設定款式
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = popView.findViewById(R.id.shop_cart_review);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        shopcart_txt_count = popView.findViewById(R.id.shopcart_txt_count);
        shopcart_txt_count.setText(count + "");
        //此項為當recyclerview描繪完後，再執行
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myRecylcerViewHeight = recyclerView.getHeight();
                        AddCartRecyclerViewAdapter recylcerAdapter = new AddCartRecyclerViewAdapter(getBaseContext(), json, myRecylcerViewHeight, default_color);
                        recylcerAdapter.setItemSelectListener(new AddCartRecyclerViewAdapter.ItemSelectListener() {
                            @Override
                            public void ItemSelected(View view, int postion, ArrayList<Map<String, String>> list) {
                                ((TextView) popView.findViewById(R.id.shopcart_txt_sprice)).setText("$" + list.get(postion).get("sprice"));
                                try {
                                    max = Integer.parseInt(list.get(postion).get("total"));
                                } catch (Exception e) {
                                    max = 0;
                                }
                                ((TextView) popView.findViewById(R.id.shopcart_txt_total)).setText("商品數量:" + max);
                                pino = list.get(postion).get("pino");
                            }

                            @Override
                            public void ItemCancelSelect(View view, int postion, ArrayList<Map<String, String>> list) {
                                ((TextView) popView.findViewById(R.id.shopcart_txt_sprice)).setText("$" + product_info.get("rsprice"));
                                max = 0;
                                count = 0;
                                ((TextView) popView.findViewById(R.id.shopcart_txt_total)).setText("商品數量:" + max);
                                shopcart_txt_count.setText(max + "");
                                pino = null;
                            }
                        });
                        recyclerView.setAdapter(recylcerAdapter);
                        recyclerView.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
        //設定增加及減少產品數量
        shopcart_btn_increase = popView.findViewById(R.id.shopcart_btn_increase);
        shopcart_btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count < max) {
                    count++;
                    shopcart_txt_count.setText(count + "");
                }
            }
        });
        shopcart_btn_decrease = popView.findViewById(R.id.shopcart_btn_decrease);
        shopcart_btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0) {
                    count--;
                    shopcart_txt_count.setText(count + "");
                }
            }
        });
        shop_cart_confirm = popView.findViewById(R.id.shop_cart_confirm);
        shop_cart_confirm.setBackgroundColor(default_color);
        shop_cart_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0 && max != 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new ShopCartJsonData().setCart(token, pno, pino, count);
                                boolean success = jsonObject.getBoolean("Success");
                                Message = jsonObject.getString("Message");
                                if (success) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "" + Message, Toast.LENGTH_SHORT).show();
                                            popWin.dismiss();
                                            enable_background.setVisibility(View.INVISIBLE);
                                            if (type == 1) {
                                                startActivity(new Intent(PcContentActivity.this, ShopCartActivity.class));
                                            }

                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                }
            }
        });
        //設定POP UP
        showPopUp(popView);
    }

    //初始化運送方式
    private void initShipWay() {
        ship_ways = findViewById(R.id.ship_ways);
        ship_ways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.shipways_layout, null);
                recyclerView = popView.findViewById(R.id.ship_ways_review);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new ShipsWaysRecyclerViewAdapter(getApplicationContext(), json));
                showPopUp(popView);
            }
        });
    }

    //顯示POP UP
    public void showPopUp(View popView) {
        popWin = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, (int) (450 * dm.density), false); // 实例化PopupWindow
        // 设置PopupWindow的弹出和消失效果
        popWin.setAnimationStyle(R.style.dialogWindowAnim);
        popWin.showAtLocation(enable_background, Gravity.BOTTOM, 0, 0); // 显示弹出窗口
        enable_background.setVisibility(View.VISIBLE);
    }

    //初始化隱藏背景
    private void initEnableBackground() {
        enable_background = findViewById(R.id.enable_background);
        enable_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWin.dismiss();
                enable_background.setVisibility(View.INVISIBLE);
            }
        });
        enable_background.setVisibility(View.INVISIBLE);
    }

    //home鍵
    private void initHome() {

        pccontent_btn_home = findViewById(R.id.pccontent_btn_home);
        pccontent_btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().finishActivity(PtypeActivity.class);
                AppManager.getAppManager().finishActivity(PcContentActivity.class);

            }
        });
    }

    //加入我的最愛
    private void initFavorate() {
        heart = findViewById(R.id.pccontent_heart);
        if (productInfoPojo.getFavorite()) {
            heart.setImageResource(R.drawable.heart_on);
        } else {
            heart.setImageResource(R.drawable.heart_off);
        }
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productInfoPojo.getFavorite()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new GetInformationByPHP().delFavoriteProduct(token, productInfoPojo.getPno());
                        }
                    }).start();
                    heart.setImageResource(R.drawable.heart_off);
                    toastMessageDialog.setMessageText("已取消我的最愛");
                    toastMessageDialog.show();
                    productInfoPojo.setFavorite(false);
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new GetInformationByPHP().setFavorite(token, productInfoPojo.getPno());
                        }
                    }).start();
                    heart.setImageResource(R.drawable.heart_on);
                    toastMessageDialog.setMessageText("已加入我的最愛");
                    toastMessageDialog.show();
                    productInfoPojo.setFavorite(true);
                }

            }

        });
    }


    //初始化詳細說明及退換貨須知
    private void initTabLayout() {
        webviewpager = findViewById(R.id.pccontent_web_viewpager);
        tabLayout = findViewById(R.id.pccontent_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        Map<String, String> map = ResolveJsonData.getWebView(json);
        PcContentWebViewPagerAdapter adapter = new PcContentWebViewPagerAdapter(getSupportFragmentManager(), new String[]{"詳細說明", "退/換貨須知"}, new String[]{map.get("content"), map.get("rpolicy")});
        adapter.setViewPager(webviewpager);
        webviewpager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(webviewpager, true);
        //   webviewpager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,6500));
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText(productInfoPojo.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pc_content_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //會員區
        if (item.getItemId() == R.id.pccontent_menu_shopcart) {
            startActivity(new Intent(PcContentActivity.this, ShopCartActivity.class));
            return true;
        }

        return false;
    }


    private String getDeciamlString(String str) {
        // DecimalFormat df = new DecimalFormat("###,###");
        // return df.format(Double.parseDouble(str));
        return str;
    }

}
