package com.test.tw.wrokproduct;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MyPagerAdapter;
import adapter.TestAdapter;
import library.BottomNavigationViewHelper;
import library.GetInformationByPHP;
import pojo.HotkeywordsPojo;

public class MainActivity extends AppCompatActivity {
    DisplayMetrics dm;
    List<ImageView> list;
    View include;
    TextView dot1, dot2, dot3;
    ViewPager viewPager;
    Runnable runnable;
    ScrollView mainScrollView;
    int real_heigh;
    MyPagerAdapter myPagerAdapter;
    SwipeRefreshLayout mSwipeLayout;
    RecyclerView recyclerView, recyclerView2, recyclerView3;
    JSONObject json,json1, json2, json3;
    TestAdapter testAdapter1, testAdapter2, testAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initBtnNav();
        getAllImage();
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        json1 = new GetInformationByPHP().getHotkeywords();
                        json2 = new GetInformationByPHP().getPtype();
                        json3 = new GetInformationByPHP().getBrands();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                testAdapter1.setFilter(json1);
                                testAdapter2.setFilter(json2);
                                testAdapter3.setFilter(json3);
                                mSwipeLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
                // 結束更新動畫


            }

        });
        mSwipeLayout.setColorSchemeColors(Color.RED);
        //startActivity(new Intent(MainActivity.this, LoadingPage.class));


    }

    private void getAllImage() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                /**
                 * //取得Slider圖片
                 * */
/*
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        json = new GetInformationByPHP().getSlider();
                        bitmaps1 = showItemsByRunnable(json);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //高度等比縮放[   圖片高度/(圖片寬度/手機寬度)    ]
                                float real_heigh = bitmaps1.get(0).getImage().getHeight() / (bitmaps1.get(0).getImage().getWidth() / (float) dm.widthPixels);
                                include.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, (int) real_heigh));
                                initViewPager(bitmaps1);
                            }
                        });
                    }
                });

*/
                /**
                 * //取得HotkeyWords圖片
                 * */
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        json1 = new GetInformationByPHP().getHotkeywords();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //recycleView
                                real_heigh = (int) ((dm.widthPixels - 40 * dm.density) / (float) 3.5);
                                testAdapter1 = new TestAdapter(getApplicationContext(), json1, real_heigh, real_heigh * 3 / 4, 0);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(testAdapter1);
                            }
                        });
                    }
                });


                /**
                 * //取得Ptype圖片
                 * */
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        json2 = new GetInformationByPHP().getPtype();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                real_heigh = (int) ((dm.widthPixels - 10 * dm.density) / (float) 3.5);
                                testAdapter2 = new TestAdapter(getApplicationContext(), json2, real_heigh, dm.widthPixels / 4, 1);
                                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                                layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                                recyclerView2.setLayoutManager(layoutManager);
                                recyclerView2.setAdapter(testAdapter2);
                            }
                        });
                    }

                });
                /**
                 * //取得Brands圖片
                 * */
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        json3 = new GetInformationByPHP().getBrands();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                real_heigh = (int) ((dm.widthPixels - 25 * dm.density) / (float) 4);
                                testAdapter3 = new TestAdapter(getApplicationContext(), json3, real_heigh, real_heigh / 4 * 3, 2);
                                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                                layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                                recyclerView3.setLayoutManager(layoutManager);
                                recyclerView3.setAdapter(testAdapter3);

                            }
                        });
                    }

                });

                Looper.loop();
            }
        }).start();

    }

    private void init() {
        getID();
        list = new ArrayList<>();
        dm = getResources().getDisplayMetrics();
        real_heigh = (int) ((dm.widthPixels - 20 * dm.density) / (float) 3.5 / 4 * 3);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView.setHasFixedSize(true);
        real_heigh = (dm.widthPixels) / 2;
        recyclerView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView2.setHasFixedSize(true);
        real_heigh = (int) ((dm.widthPixels - 40 * dm.density) / (float) 4 / 4 * 3 + 10 * dm.density) * 2;
        recyclerView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, real_heigh));
        recyclerView3.setHasFixedSize(true);
        runnable = new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        };

    }

    private void getID() {
        mainScrollView=findViewById(R.id.mainScrollView);
        recyclerView = findViewById(R.id.reView);
        recyclerView2 = findViewById(R.id.reView2);
        recyclerView3 = findViewById(R.id.reView3);
        viewPager = findViewById(R.id.adView);
        include = findViewById(R.id.RelateView);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
    }




    private void initViewPager(ArrayList<HotkeywordsPojo> bitmaps) {
        ImageView imageView;

        for (int i = 0; i < bitmaps.size(); i++) {
            imageView = new ImageView(this);
            imageView.setImageBitmap(bitmaps.get(i).getImage());
            list.add(imageView);
        }
      //  myPagerAdapter=new MyPagerAdapter(list, viewPager, dot1, dot2, dot3);
        viewPager.setAdapter(myPagerAdapter);


    }

    protected void initBtnNav() {//BottomLayout
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tab_layout);
        new BottomNavigationViewHelper().disableShiftMode(navigation);//取消動畫
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {//監聽事件

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mainScrollView.removeAllViews();
                        return true;
                    case R.id.navigation_shop:
                        mainScrollView.requestFocus();
                        return true;
                    case R.id.navigation_my_favor:

                        return true;
                    case R.id.navigation_member:

                        return true;

                }
                return false;
            }

        });

    }


}