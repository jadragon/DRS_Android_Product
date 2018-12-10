package com.example.alex.designateddriving_driver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.alex.designateddriving_driver.Fragment.Fragment_myDeposit;

import java.util.ArrayList;

public class MyDepositActivity extends ToolbarActivity {
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deposit);
        initToolbar("我的儲值", true);
        initRecylcerViewAndTabLayout();
    }


    private void initRecylcerViewAndTabLayout() {
        ViewPager viewPager = findViewById(R.id.include_viewpager);
        fragmentList = new ArrayList<>();
        fragmentList.add(new Fragment_myDeposit());
        fragmentList.add(new Fragment_myDeposit());
        fragmentList.add(new Fragment_myDeposit());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] mTabtitle = new String[]{"儲值紀錄", "提領紀錄", "申請紀錄"};

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
