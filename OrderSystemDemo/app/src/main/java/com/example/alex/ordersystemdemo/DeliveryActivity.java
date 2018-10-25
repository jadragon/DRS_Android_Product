package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.alex.ordersystemdemo.Fragment.Fragment_orderlist;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends ToolbarAcitvity {
    private ViewPager viewPager;
    private List<Fragment> fragmentArrayList;
    private String[] tablist = new String[]{"未配送", "已配對", "配送中"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        initToolbar("外送", false, false);
        initViewPagerAndTabLayout();
    }

    private void initViewPagerAndTabLayout() {
        viewPager = findViewById(R.id.viewpager);
        fragmentArrayList = new ArrayList<>();

        Fragment_orderlist fragment_orderlist = new Fragment_orderlist();
        Bundle bundle = new Bundle();
        bundle.putString("status", "2");
        fragment_orderlist.setArguments(bundle);
        fragmentArrayList.add(fragment_orderlist);

        fragment_orderlist = new Fragment_orderlist();
        bundle = new Bundle();
        bundle.putString("status", "3");
        fragment_orderlist.setArguments(bundle);
        fragmentArrayList.add(fragment_orderlist);

        fragment_orderlist = new Fragment_orderlist();
        bundle = new Bundle();
        bundle.putString("status", "4");
        fragment_orderlist.setArguments(bundle);
        fragmentArrayList.add(fragment_orderlist);
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
