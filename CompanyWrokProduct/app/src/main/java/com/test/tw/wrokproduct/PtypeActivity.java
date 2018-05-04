package com.test.tw.wrokproduct;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import Fragment.Fragment_shop_content;
import adapter.recyclerview.PtypeRecyclerAdapter;
import adapter.viewpager.ShopViewPagerAdapter;
import library.AppManager;
import library.GetInformationByPHP;
import library.ResolveJsonData;

public class PtypeActivity extends AppCompatActivity {
    DisplayMetrics dm;
    TabLayout tabLayout;
    ViewPager viewPager;
    JSONObject json1, json2, json3, json4;
    Fragment_shop_content fragment_shop_content1, fragment_shop_content2, fragment_shop_content3, fragment_shop_content4;
    RecyclerView recyclerView;
    JSONObject json;
    ArrayList<Map<String, String>> list;
    ShopViewPagerAdapter shopViewPagerAdapter;
    Bitmap[][] bitmaps;
    int index;
    int POSITION;
    PtypeRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ptype_layout);
        AppManager.getAppManager().addActivity(this);
        POSITION = getIntent().getIntExtra("position", 0);
        dm = getResources().getDisplayMetrics();
        //setupTabIcons();
        tabLayout = findViewById(R.id.ptype_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = findViewById(R.id.ptype_viewpager);
        fragment_shop_content1 = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
        fragment_shop_content2 = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
        fragment_shop_content3 = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
        fragment_shop_content4 = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
        shopViewPagerAdapter = new ShopViewPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.shop_header_title), new Fragment_shop_content[]{fragment_shop_content1, fragment_shop_content2, fragment_shop_content3, fragment_shop_content4});
        viewPager.setAdapter(shopViewPagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new GetInformationByPHP().getPtype();
                list = ResolveJsonData.getPtypeDetail(json, POSITION);
                bitmaps = new Bitmap[2][list.size()];
                try {
                    for (int i = 0; i < list.size(); i++) {
                        bitmaps[0][i] = ImageLoader.getInstance().loadImageSync(list.get(i).get("image"));
                        bitmaps[1][i] = ImageLoader.getInstance().loadImageSync(list.get(i).get("aimg"));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                initRecyclerView();
                            } catch (NullPointerException e) {
                                e.getMessage();
                            }
                        }
                    });
                    setFilter(0);
                } catch (NullPointerException e) {
                    e.getMessage();
                }

            }
        }).start();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.ptype_title);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((dm.widthPixels - 80 * dm.density) / 4 + 40 * dm.density));
        recyclerView.setLayoutParams(params);
        adapter = new PtypeRecyclerAdapter(getApplicationContext(), list, bitmaps, (int) ((dm.widthPixels - 80 * dm.density) / 4), (int) ((dm.widthPixels - 80 * dm.density) / 4 + 40 * dm.density));
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(new PtypeRecyclerAdapter.ClickListener() {
            @Override
            public void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list) {
                index = postion;
                viewPager.setCurrentItem(0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        resetRecyclerView(index);
                    }
                }).start();

            }
        });
    }

    public void setFilter(int position) {
        json1 = new GetInformationByPHP().getPlist(list.get(position).get("ptno"), 0, 1);
        json2 = new GetInformationByPHP().getPlist(list.get(position).get("ptno"), 1, 1);
        json3 = new GetInformationByPHP().getPlist(list.get(position).get("ptno"), 2, 1);
        json4 = new GetInformationByPHP().getPlist(list.get(position).get("ptno"), 3, 1);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    fragment_shop_content1.setFilter(json1);
                    fragment_shop_content2.setFilter(json2);
                    fragment_shop_content3.setFilter(json3);
                    fragment_shop_content4.setFilter(json4);
                } catch (NullPointerException e) {
                    e.getMessage();
                }

            }
        });

    }

    public void resetRecyclerView(int position) {
        this.index = position;
        new Thread(new Runnable() {
            @Override
            public void run() {
                json1 = new GetInformationByPHP().getPlist(list.get(index).get("ptno"), 0, 1);
                json2 = new GetInformationByPHP().getPlist(list.get(index).get("ptno"), 1, 1);
                json3 = new GetInformationByPHP().getPlist(list.get(index).get("ptno"), 2, 1);
                json4 = new GetInformationByPHP().getPlist(list.get(index).get("ptno"), 3, 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_shop_content1.resetRecyclerView(json1);
                        fragment_shop_content2.resetRecyclerView(json2);
                        fragment_shop_content3.resetRecyclerView(json3);
                        fragment_shop_content4.resetRecyclerView(json4);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dm = null;
        tabLayout = null;
        viewPager = null;
        json1 = null;
        json2 = null;
        json3 = null;
        json4 = null;
        fragment_shop_content1 = null;
        fragment_shop_content2 = null;
        fragment_shop_content3 = null;
        fragment_shop_content4 = null;
        recyclerView = null;
        try {
            for (int i = 0; i < bitmaps[0].length; i++) {
                if (bitmaps[0][i] != null && !bitmaps[0][i].isRecycled()) {
                    bitmaps[0][i].recycle();
                }
                if (bitmaps[1][i] != null && !bitmaps[1][i].isRecycled()) {
                    bitmaps[1][i].recycle();
                }
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }

        bitmaps = null;
        json = null;
        System.gc();
        //释放内存
        /*
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
        */
        //释放被持有的bitmap
        /*
        if (mBitmaps != null) {
            for (Bitmap bitmap : mBitmaps) {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
        */
    }


}