package com.example.alex.designateddriving_driver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.designateddriving_driver.Fragment.Fragment_OrderList;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        ViewPager viewPager = findViewById(R.id.include_viewpager);
        fragmentList = new ArrayList<>();
        fragmentList.add(new Fragment_OrderList());
        fragmentList.add(new Fragment_OrderList());
        fragmentList.add(new Fragment_OrderList());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            String[] mTabtitle = new String[]{"長途", "鐘點", "立即"};

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
