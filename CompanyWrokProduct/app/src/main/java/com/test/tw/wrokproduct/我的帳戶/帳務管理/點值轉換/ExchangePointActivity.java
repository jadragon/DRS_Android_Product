package com.test.tw.wrokproduct.我的帳戶.帳務管理.點值轉換;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.test.tw.wrokproduct.Fragment.Fragment_ExchangePoint;
import library.Component.ToolbarActivity;
import library.GetJsonData.BillJsonData;

public class ExchangePointActivity extends ToolbarActivity {
    List<Fragment_ExchangePoint> fragmentList;
    String[] mTabtitle = {"波克點值", "庫瓦點值"};
    int count = 2;
    int point_type;
    JSONObject json1, json2;
    GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_point);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar(true, "點值轉換");
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        ViewPager viewPager = findViewById(R.id.include_viewpager);
        TabLayout tabLayout = findViewById(R.id.include_tabLayout);
        tabLayout.setElevation(20);
        fragmentList = new ArrayList<>();
        Fragment_ExchangePoint fragment_exchangePoint;
        for (int i = 1; i <= count; i++) {
            fragment_exchangePoint = new Fragment_ExchangePoint();
            fragment_exchangePoint.setPoint_type(point_type);
            fragment_exchangePoint.setType(i);
            fragmentList.add(fragment_exchangePoint);
        }

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(7);
        refreshText();
    }

    public void refreshText() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                json1 = new BillJsonData().getPolkRate(gv.getToken());
                json2 = new BillJsonData().getKuvaRate(gv.getToken());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragmentList.get(0).setText(json1);
                        fragmentList.get(1).setText(json2);
                    }
                });
            }
        }).start();

    }

}
