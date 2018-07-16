package com.test.tw.wrokproduct.商家管理.商品訂單;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.Fragment.Fragment_OrderInfo;
import com.test.tw.wrokproduct.R;

import java.util.ArrayList;
import java.util.List;

public class ProductOrderActivity extends AppCompatActivity {
    List<Fragment_OrderInfo> fragmentList;
    String[] mTabtitle;
    ViewPager viewPager;
    TabLayout tabLayout;
    FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabtitle = getResources().getStringArray(R.array.productorder_type);
        setContentView(R.layout.activity_order_info);
        initToolbar();
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        viewPager = findViewById(R.id.orderinfo_viewpager);
        tabLayout = findViewById(R.id.orderinfo_tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        fragmentList = new ArrayList<>();
      Fragment_OrderInfo fragment_orderInfo = new Fragment_OrderInfo();
        fragment_orderInfo.setType(1);
        fragment_orderInfo.setIndex(0);
        fragmentList.add(fragment_orderInfo);
        fragment_orderInfo = new Fragment_OrderInfo();
        fragment_orderInfo.setType(1);
        fragment_orderInfo.setIndex(1);
        fragmentList.add(fragment_orderInfo);
        fragment_orderInfo = new Fragment_OrderInfo();
        fragment_orderInfo.setType(1);
        fragment_orderInfo.setIndex(2);
        fragmentList.add(fragment_orderInfo);
        fragment_orderInfo = new Fragment_OrderInfo();
        fragment_orderInfo.setType(1);
        fragment_orderInfo.setIndex(3);
        fragmentList.add(fragment_orderInfo);
        fragment_orderInfo = new Fragment_OrderInfo();
        fragment_orderInfo.setType(1);
        fragment_orderInfo.setIndex(4);
        fragmentList.add(fragment_orderInfo);
        fragment_orderInfo = new Fragment_OrderInfo();
        fragment_orderInfo.setType(1);
        fragment_orderInfo.setIndex(5);
        fragmentList.add(fragment_orderInfo);
        fragment_orderInfo = new Fragment_OrderInfo();
        fragment_orderInfo.setType(1);
        fragment_orderInfo.setIndex(6);
        fragmentList.add(fragment_orderInfo);
        fragment_orderInfo = new Fragment_OrderInfo();
        fragment_orderInfo.setType(1);
        fragment_orderInfo.setIndex(7);
        fragmentList.add(fragment_orderInfo);
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
        viewPager.setOffscreenPageLimit(8);

    }

    public void setFilterByIndex(int... indexes) {
        for (int index : indexes) {
            fragmentList.get(index).updateData();
        }
    }


    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("訂單資訊");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 110) {
            setFilterByIndex(0, 1);
        } else if (requestCode == 113) {
            setFilterByIndex(3, 4);
        }
    }
}
