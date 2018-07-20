package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.test.tw.wrokproduct.Fragment.Fragment_Myappreciate;
import com.test.tw.wrokproduct.R;

import java.util.ArrayList;

import library.Component.ToolbarActivity;

public class MyAppreciateActivity extends ToolbarActivity {
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    String[] mTabtitle;
    FragmentPagerAdapter fragmentPagerAdapter;
    int type;

    public void setmTabtitle(String[] mTabtitle) {
        this.mTabtitle = mTabtitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apprecoate);
        type = getIntent().getIntExtra("type", 0);
        initToolbar(true, "我的評價");
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        ViewPager viewPager = findViewById(R.id.myappraise_viewpager);
        TabLayout tabLayout = findViewById(R.id.myappraise_tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        if(type==0) {
            ((TextView)findViewById(R.id.myappraise_txt_tscore)).setTextColor(getResources().getColor(R.color.red));
        }else {
            ((TextView)findViewById(R.id.myappraise_txt_tscore)).setTextColor(getResources().getColor(R.color.teal));
        }
        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.white));
        tabLayout.setElevation(20);
        mTabtitle = new String[]{"  全部(0)  ", "  五顆星(0)  ", "  四顆星(0)  ", "  三顆星(0)  ", "  二顆星(0)  ", "  一顆星(0)  "};
        fragmentList = new ArrayList<>();
        Fragment_Myappreciate fragment_myappreciate = new Fragment_Myappreciate();
        fragment_myappreciate.setType(type);
        fragment_myappreciate.setRule(0);
        fragmentList.add(fragment_myappreciate);
        for (int i = 1; i < mTabtitle.length; i++) {
            fragment_myappreciate = new Fragment_Myappreciate();
            fragment_myappreciate.setType(type);
            fragment_myappreciate.setRule(mTabtitle.length - i);
            fragmentList.add(fragment_myappreciate);
        }

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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

    public void setFilter() {
        fragmentPagerAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 520) {
            ((Fragment_Myappreciate) fragmentList.get(0)).setFilter();
            ((Fragment_Myappreciate) fragmentList.get(mTabtitle.length - requestCode)).setFilter();
        }
    }
}
