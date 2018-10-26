package com.example.alex.ordersystemdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.alex.ordersystemdemo.Fragment.Fragment_orderlist;

import java.util.ArrayList;
import java.util.List;

public class StudentAcitvity extends ToolbarAcitvity {
    private ViewPager viewPager;
    private List<Fragment_orderlist> fragmentArrayList;
    private String[] tablist = new String[]{"已下單", "配送中", "已完成"};


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

        Fragment_orderlist fragment_orderlist = new Fragment_orderlist();
        Bundle bundle = new Bundle();
        bundle.putString("status", "1");
        fragment_orderlist.setArguments(bundle);
        fragmentArrayList.add(fragment_orderlist);

        fragment_orderlist = new Fragment_orderlist();
        bundle = new Bundle();
        bundle.putString("status", "4");
        fragment_orderlist.setArguments(bundle);
        fragmentArrayList.add(fragment_orderlist);

        fragment_orderlist = new Fragment_orderlist();
        bundle = new Bundle();
        bundle.putString("status", "5");
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
    public void refreashAllFragment() {
        for (Fragment_orderlist fragment_orderlist : fragmentArrayList) {
            fragment_orderlist.refreashAdapter();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            refreashAllFragment();
        }
    }
}
