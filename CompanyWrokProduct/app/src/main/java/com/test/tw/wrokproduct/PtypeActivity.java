package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import Fragment.Fragment_shop_content;
import adapter.recyclerview.PtypeRecyclerAdapter;
import adapter.recyclerview.ShopRecyclerViewAdapter;
import adapter.viewpager.ShopViewPagerAdapter;
import library.AnalyzeJSON.ResolveJsonData;
import library.AppManager;
import library.GetJsonData.GetInformationByPHP;
import library.LoadingView;

public class PtypeActivity extends AppCompatActivity {
    DisplayMetrics dm;
    TabLayout tabLayout;
    ViewPager viewPager;

    RecyclerView recyclerView;
    JSONObject json;
    ArrayList<Map<String, String>> list;
    ShopViewPagerAdapter shopViewPagerAdapter;
    int index;
    int POSITION;
    PtypeRecyclerAdapter adapter;
    ArrayList<Fragment_shop_content> fragmentArrayList;
    Fragment_shop_content fragment_shop_content;
    String currentPtno;
    GlobalVariable gv;
    String mvip;
    ArrayList<String> tabtitle;
    private Fragment_shop_content fragment_shop_content1, fragment_shop_content2, fragment_shop_content3, fragment_shop_content4, fragment_shop_content5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ptype_layout);
        tabtitle = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.shop_header_title)));
        initSearchToolbar();
        gv = ((GlobalVariable) getApplicationContext());
        mvip = gv.getMvip();
        AppManager.getAppManager().addActivity(this);
        POSITION = getIntent().getIntExtra("position", 0);
        dm = getResources().getDisplayMetrics();
        //setupTabIcons();
        tabLayout = findViewById(R.id.ptype_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = findViewById(R.id.ptype_viewpager);
        //
        fragment_shop_content1 = new Fragment_shop_content(ShopRecyclerViewAdapter.HIDE_BANNER);
        fragment_shop_content1.setType(0);
        fragment_shop_content2 = new Fragment_shop_content(ShopRecyclerViewAdapter.HIDE_BANNER);
        fragment_shop_content2.setType(1);
        fragment_shop_content3 = new Fragment_shop_content(ShopRecyclerViewAdapter.HIDE_BANNER);
        fragment_shop_content3.setType(2);
        fragment_shop_content4 = new Fragment_shop_content(ShopRecyclerViewAdapter.HIDE_BANNER);
        fragment_shop_content4.setType(3);
        fragment_shop_content5 = new Fragment_shop_content(ShopRecyclerViewAdapter.HIDE_BANNER);
        fragment_shop_content5.setType(4);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        resetViewPager(mvip);
        //分類
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new GetInformationByPHP().getPtype();
                list = ResolveJsonData.getPtypeDetail(json, POSITION);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView();
                        currentPtno = list.get(0).get("ptno");
                        setFilter();
                    }
                });
            }
        }).start();
    }

    public void resetViewPager(String mvip) {
        if (mvip.equals("2")) {
            fragmentArrayList = new ArrayList<>();
            tabtitle = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.shop_header_title)));
            fragmentArrayList.add(fragment_shop_content1);
            fragmentArrayList.add(fragment_shop_content2);
            fragmentArrayList.add(fragment_shop_content3);
            fragmentArrayList.add(fragment_shop_content4);
            fragmentArrayList.add(fragment_shop_content5);
            shopViewPagerAdapter = new ShopViewPagerAdapter(getSupportFragmentManager(), tabtitle, fragmentArrayList);
            viewPager.setAdapter(shopViewPagerAdapter);
        } else {
            fragmentArrayList = new ArrayList<>();
            fragmentArrayList.add(fragment_shop_content1);
            fragmentArrayList.add(fragment_shop_content2);
            fragmentArrayList.add(fragment_shop_content3);
            fragmentArrayList.add(fragment_shop_content4);
            if (tabtitle.size() == 5)
                tabtitle.remove(4);
            shopViewPagerAdapter = new ShopViewPagerAdapter(getSupportFragmentManager(), tabtitle, fragmentArrayList);
            viewPager.setAdapter(shopViewPagerAdapter);
        }
    }

    private void initSearchToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.include_search_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PtypeActivity.this, SearchBarActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.ptype_title);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((dm.widthPixels - 80 * dm.density) / 4 + 40 * dm.density));
        recyclerView.setLayoutParams(params);
        adapter = new PtypeRecyclerAdapter(getApplicationContext(), list, (int) ((dm.widthPixels - 80 * dm.density) / 4), (int) ((dm.widthPixels - 80 * dm.density) / 4 + 40 * dm.density));
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(new PtypeRecyclerAdapter.ClickListener() {
            @Override
            public void ItemClicked(View view, int postion, ArrayList<Map<String, String>> list) {
                index = postion;
                viewPager.setCurrentItem(0);
                resetRecyclerView(index);

            }
        });
    }

    public void setFilter() {
        fragment_shop_content1.setPtno(currentPtno);
        fragment_shop_content2.setPtno(currentPtno);
        fragment_shop_content3.setPtno(currentPtno);
        fragment_shop_content4.setPtno(currentPtno);
        fragment_shop_content5.setPtno(currentPtno);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        LoadingView.setMessage("更新資料");
                        LoadingView.show(getCurrentFocus());
                    }
                });
                final JSONObject json1 = new GetInformationByPHP().getPlist(currentPtno, 0, gv.getToken(), 1);
                final JSONObject json2 = new GetInformationByPHP().getPlist(currentPtno, 1, gv.getToken(), 1);
                final JSONObject json3 = new GetInformationByPHP().getPlist(currentPtno, 2, gv.getToken(), 1);
                final JSONObject json4 = new GetInformationByPHP().getPlist(currentPtno, 3, gv.getToken(), 1);
                final JSONObject json5;
                if (mvip.equals("2")) {
                    json5 = new GetInformationByPHP().getPlist(currentPtno, 4, gv.getToken(), 1);
                } else {
                    json5 = null;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_shop_content1.setFilter(json1);
                        fragment_shop_content2.setFilter(json2);
                        fragment_shop_content3.setFilter(json3);
                        fragment_shop_content4.setFilter(json4);
                        fragment_shop_content5.setFilter(json5);
                        LoadingView.hide();
                    }
                });
            }
        }).start();

    }

    public void resetRecyclerView(int position) {
        currentPtno = list.get(position).get("ptno");
        fragment_shop_content1.setPtno(currentPtno);
        fragment_shop_content2.setPtno(currentPtno);
        fragment_shop_content3.setPtno(currentPtno);
        fragment_shop_content4.setPtno(currentPtno);
        fragment_shop_content5.setPtno(currentPtno);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject json1 = new GetInformationByPHP().getPlist(currentPtno, 0, gv.getToken(), 1);
                final JSONObject json2 = new GetInformationByPHP().getPlist(currentPtno, 1, gv.getToken(), 1);
                final JSONObject json3 = new GetInformationByPHP().getPlist(currentPtno, 2, gv.getToken(), 1);
                final JSONObject json4 = new GetInformationByPHP().getPlist(currentPtno, 3, gv.getToken(), 1);
                final JSONObject json5;
                if (mvip.equals("2")) {
                    json5 = new GetInformationByPHP().getPlist(currentPtno, 4, gv.getToken(), 1);
                } else {
                    json5 = null;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment_shop_content1.resetRecyclerView(json1);
                        fragment_shop_content2.resetRecyclerView(json2);
                        fragment_shop_content3.resetRecyclerView(json3);
                        fragment_shop_content4.resetRecyclerView(json4);
                        fragment_shop_content5.resetRecyclerView(json5);
                    }
                });
            }
        }).start();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (!gv.getMvip().equals(mvip)) {
            LoadingView.show(getCurrentFocus());
            mvip = gv.getMvip();
            resetViewPager(mvip);
            //网络数据刷新
            setFilter();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dm = null;
        tabLayout = null;
        viewPager = null;
        fragment_shop_content = null;
        recyclerView = null;
        json = null;
        System.gc();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pc_content_menu, menu);
        menu.getItem(1).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //會員區
        if (item.getItemId() == R.id.pccontent_menu_shopcart) {
            if (gv.getToken() != null)
                startActivity(new Intent(PtypeActivity.this, ShopCartActivity.class));
            else
                startActivity(new Intent(PtypeActivity.this, LoginActivity.class));
            return true;
        }

        return false;
    }

}