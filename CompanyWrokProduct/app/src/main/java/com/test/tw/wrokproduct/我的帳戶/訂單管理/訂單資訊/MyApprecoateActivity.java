package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.test.tw.wrokproduct.Fragment.Fragment_ExchangePoint;
import com.test.tw.wrokproduct.R;

import java.util.ArrayList;

import library.Component.ToolbarActivity;

public class MyApprecoateActivity extends ToolbarActivity {
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    String[] mTabtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apprecoate);
        initToolbar(true, "我的評價");
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        ViewPager viewPager = findViewById(R.id.include_viewpager);
        TabLayout tabLayout = findViewById(R.id.include_tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setElevation(20);
        mTabtitle = new String[]{"全部", "五顆星", "四顆星", "三顆星", "二顆星", "一顆星"};
        fragmentList = new ArrayList<>();
        Fragment_ExchangePoint fragment_exchangePoint;
        for (int i = 1; i <= mTabtitle.length; i++) {
            fragment_exchangePoint = new Fragment_ExchangePoint();
            fragment_exchangePoint.setPoint_type(0);
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
    }

}
