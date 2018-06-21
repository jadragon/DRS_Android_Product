package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Fragment.Fragment_oderInfo;
import library.GetJsonData.OrderInfoJsonData;

public class OrderInfoActivity extends AppCompatActivity {
    String token;
    List<Fragment_oderInfo> fragmentList;
    String[] mTabtitle = {"未付款", "未配送", "配送中", "取件完成", "申請退換貨中", "已完成", "取消"};
    ViewPager viewPager;
    TabLayout tabLayout;
    JSONObject json1, json2, json3, json4, json5, json6, json7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        initToolbar();

        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        viewPager = findViewById(R.id.orderinfo_viewpager);
        tabLayout = findViewById(R.id.orderinfo_tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        fragmentList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json1 = new OrderInfoJsonData().getMemberOrder(token, 0, 1);
                json2 = new OrderInfoJsonData().getMemberOrder(token, 1, 1);
                json3 = new OrderInfoJsonData().getMemberOrder(token, 2, 1);
                json4 = new OrderInfoJsonData().getMemberOrder(token, 3, 1);
                json5 = new OrderInfoJsonData().getMemberOrder(token, 4, 1);
                json6 = new OrderInfoJsonData().getMemberOrder(token, 5, 1);
                json7 = new OrderInfoJsonData().getMemberOrder(token, 6, 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Fragment_oderInfo fragment_oderInfo = new Fragment_oderInfo();
                        fragment_oderInfo.setJson(json1);
                        fragment_oderInfo.setIndex(0);
                        fragmentList.add(fragment_oderInfo);
                        fragment_oderInfo = new Fragment_oderInfo();
                        fragment_oderInfo.setJson(json2);
                        fragment_oderInfo.setIndex(1);
                        fragmentList.add(fragment_oderInfo);
                        fragment_oderInfo = new Fragment_oderInfo();
                        fragment_oderInfo.setJson(json3);
                        fragment_oderInfo.setIndex(2);
                        fragmentList.add(fragment_oderInfo);
                        fragment_oderInfo = new Fragment_oderInfo();
                        fragment_oderInfo.setJson(json4);
                        fragment_oderInfo.setIndex(3);
                        fragmentList.add(fragment_oderInfo);
                        fragment_oderInfo = new Fragment_oderInfo();
                        fragment_oderInfo.setJson(json5);
                        fragment_oderInfo.setIndex(4);
                        fragmentList.add(fragment_oderInfo);
                        fragment_oderInfo = new Fragment_oderInfo();
                        fragment_oderInfo.setJson(json6);
                        fragment_oderInfo.setIndex(5);
                        fragmentList.add(fragment_oderInfo);
                        fragment_oderInfo = new Fragment_oderInfo();
                        fragment_oderInfo.setJson(json7);
                        fragment_oderInfo.setIndex(6);
                        fragmentList.add(fragment_oderInfo);
                        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                            @Override
                            public Fragment getItem(int position) {
                                return fragmentList.get(position);
                            }

                            @Override
                            public int getCount() {
                                return fragmentList.size();
                            }

                            @Override
                            public CharSequence getPageTitle(int position) {
                                return mTabtitle[position];
                            }
                        });
                    }
                });
            }
        }).start();

    }

    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("訂單資訊");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
