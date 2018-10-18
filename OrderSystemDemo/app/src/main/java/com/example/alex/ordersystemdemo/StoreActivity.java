package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.alex.ordersystemdemo.Fragment.Fragment_orderlist;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends ToolbarAcitvity {
    private ViewPager viewPager;
    private List<Fragment> fragmentArrayList;
    private String[] tablist = new String[]{"未接單", "已接單", "配送中", "已配送"};
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        initToolbar("商家");
        initViewPagerAndTabLayout();
    }



    private void initViewPagerAndTabLayout() {
        viewPager = findViewById(R.id.viewpager);

        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new Fragment_orderlist());
        fragmentArrayList.add(new Fragment_orderlist());
        fragmentArrayList.add(new Fragment_orderlist());
        fragmentArrayList.add(new Fragment_orderlist());
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentArrayList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentArrayList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tablist[position];
            }
        });
    }

}
