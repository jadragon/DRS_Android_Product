package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.alex.ordersystemdemo.Fragment.Fragment_orderlist;

import java.util.ArrayList;
import java.util.List;

public class StudentAcitvity extends ToolbarAcitvity {
    private ViewPager viewPager;
    private List<Fragment> fragmentArrayList;
    private String[] tablist = new String[]{"已下定", "配送中", "已完成"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initToolbar("訂單資訊", true, false);
        initViewPagerAndTabLayout();
    }

    private void initViewPagerAndTabLayout() {
        viewPager = findViewById(R.id.viewpager);

        fragmentArrayList = new ArrayList<>();
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
