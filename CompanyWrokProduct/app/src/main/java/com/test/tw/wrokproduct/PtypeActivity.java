package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import Fragment.Fragment_shop_content;
import adapter.recyclerview.PtypeRecyclerAdapter;
import adapter.viewpager.ShopViewPagerAdapter;
import library.AnalyzeJSON.ResolveJsonData;
import library.AppManager;
import library.GetJsonData.GetInformationByPHP;

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
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ptype_layout);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        AppManager.getAppManager().addActivity(this);
        POSITION = getIntent().getIntExtra("position", 0);
        dm = getResources().getDisplayMetrics();
        //setupTabIcons();
        tabLayout = findViewById(R.id.ptype_header_tablayout);
        tabLayout.setSelectedTabIndicatorHeight(6);
        viewPager = findViewById(R.id.ptype_viewpager);
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
                    }
                });
                fragmentArrayList = new ArrayList<>();
                fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
                fragment_shop_content.setJson(null, new GetInformationByPHP().getPlist(list.get(0).get("ptno"), 0, token, 1));
                fragment_shop_content.setPtno(list.get(0).get("ptno"));
                fragment_shop_content.setType(0);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
                fragment_shop_content.setJson(null, new GetInformationByPHP().getPlist(list.get(0).get("ptno"), 1, token, 1));
                fragment_shop_content.setPtno(list.get(0).get("ptno"));
                fragment_shop_content.setType(1);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
                fragment_shop_content.setJson(null, new GetInformationByPHP().getPlist(list.get(0).get("ptno"), 2, token, 1));
                fragment_shop_content.setPtno(list.get(0).get("ptno"));
                fragment_shop_content.setType(2);
                fragmentArrayList.add(fragment_shop_content);
                fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
                fragment_shop_content.setJson(null, new GetInformationByPHP().getPlist(list.get(0).get("ptno"), 3, token, 1));
                fragment_shop_content.setPtno(list.get(0).get("ptno"));
                fragment_shop_content.setType(3);
                fragmentArrayList.add(fragment_shop_content);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shopViewPagerAdapter = new ShopViewPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.shop_header_title), fragmentArrayList);
                        viewPager.setAdapter(shopViewPagerAdapter);
                        viewPager.setOffscreenPageLimit(fragmentArrayList.size() - 1);
                        tabLayout.setTabMode(TabLayout.MODE_FIXED);
                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    }
                });
            }
        }).start();


        /*
        fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
        fragment_shop_content.setType(0);
        fragmentArrayList.add(fragment_shop_content);
        fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
        fragment_shop_content.setType(1);
        fragmentArrayList.add(fragment_shop_content);
        fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
        fragment_shop_content.setType(2);
        fragmentArrayList.add(fragment_shop_content);
        fragment_shop_content = new Fragment_shop_content(Fragment_shop_content.HIDE_BANNER);
        fragment_shop_content.setType(3);
        fragmentArrayList.add(fragment_shop_content);
*/


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
        currentPtno = list.get(position).get("ptno");
        fragmentArrayList.get(0).setPtno(currentPtno);
        fragmentArrayList.get(1).setPtno(currentPtno);
        fragmentArrayList.get(2).setPtno(currentPtno);
        fragmentArrayList.get(3).setPtno(currentPtno);
        fragmentArrayList.get(0).setFilter(new GetInformationByPHP().getPlist(currentPtno, 0, token, 1));
        fragmentArrayList.get(1).setFilter(new GetInformationByPHP().getPlist(currentPtno, 1, token, 1));
        fragmentArrayList.get(2).setFilter(new GetInformationByPHP().getPlist(currentPtno, 2, token, 1));
        fragmentArrayList.get(3).setFilter(new GetInformationByPHP().getPlist(currentPtno, 3, token, 1));

    }

    public void resetRecyclerView(int position) {
        currentPtno = list.get(position).get("ptno");
        fragmentArrayList.get(0).setPtno(currentPtno);
        fragmentArrayList.get(1).setPtno(currentPtno);
        fragmentArrayList.get(2).setPtno(currentPtno);
        fragmentArrayList.get(3).setPtno(currentPtno);
        fragmentArrayList.get(0).resetRecyclerView(new GetInformationByPHP().getPlist(currentPtno, 0, token, 1));
        fragmentArrayList.get(1).resetRecyclerView(new GetInformationByPHP().getPlist(currentPtno, 1, token, 1));
        fragmentArrayList.get(2).resetRecyclerView(new GetInformationByPHP().getPlist(currentPtno, 2, token, 1));
        fragmentArrayList.get(3).resetRecyclerView(new GetInformationByPHP().getPlist(currentPtno, 3, token, 1));
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
        fragment_shop_content = null;
        recyclerView = null;
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