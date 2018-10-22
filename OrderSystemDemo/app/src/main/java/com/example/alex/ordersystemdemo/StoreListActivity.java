package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.alex.ordersystemdemo.Fragment.Fragment_storelist;

import java.util.ArrayList;
import java.util.List;

public class StoreListActivity  extends ToolbarAcitvity {
    private ViewPager viewPager;
    private List<Fragment> fragmentArrayList;
    private String[] tablist = new String[]{"中式", "西式", "飲品", "點心"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        initToolbar("商家列表",false,true);
        initViewPagerAndTabLayout();
    }



    private void initViewPagerAndTabLayout() {
        viewPager = findViewById(R.id.viewpager);

        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new Fragment_storelist());
        fragmentArrayList.add(new Fragment_storelist());
        fragmentArrayList.add(new Fragment_storelist());
        fragmentArrayList.add(new Fragment_storelist());
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
