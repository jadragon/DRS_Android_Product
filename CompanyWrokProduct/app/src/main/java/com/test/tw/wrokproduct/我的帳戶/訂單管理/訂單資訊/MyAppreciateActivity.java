package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.test.tw.wrokproduct.Fragment.Fragment_Myappreciate;
import com.test.tw.wrokproduct.R;

import java.util.ArrayList;

import library.Component.ToolbarActivity;

public class MyAppreciateActivity extends ToolbarActivity {
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
        Fragment_Myappreciate fragment_myappreciate = new Fragment_Myappreciate();
        fragment_myappreciate.setType(0);
        fragment_myappreciate.setRule(0);
        fragmentList.add(fragment_myappreciate);
        for (int i = 1; i < mTabtitle.length; i++) {
            fragment_myappreciate = new Fragment_Myappreciate();
            fragment_myappreciate.setType(0);
            fragment_myappreciate.setRule(mTabtitle.length - i);
            fragmentList.add(fragment_myappreciate);
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
