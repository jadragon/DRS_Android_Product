package com.test.tw.wrokproduct.帳務管理.波克點值and庫瓦點值and雙閃幣and電子錢包;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Fragment.Fragment_Point;
import Util.StringUtil;
import library.Component.ToolbarActivity;
import library.GetJsonData.BillJsonData;

public class PointActivity extends ToolbarActivity {
    List<Fragment_Point> fragmentList;
    String[] mTabtitle;
    int point_type;
    int count = 3;
    JSONObject json1, json2, json3;
    GlobalVariable gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        gv = (GlobalVariable) getApplicationContext();
        point_type = getIntent().getIntExtra("point_type", 1);
        switch (point_type) {
            case 1:
                mTabtitle = getResources().getStringArray(R.array.polk_title);
                break;
            case 2:
                mTabtitle = getResources().getStringArray(R.array.kuva_title);
                break;
            case 3:
                count = 1;
                mTabtitle = getResources().getStringArray(R.array.acoin_title);
                break;
            case 4:
                mTabtitle = getResources().getStringArray(R.array.ewallet_title);
                break;
        }
        String title = "";
        switch (point_type) {
            case 1:
                title = "波克點值";
                break;
            case 2:
                title = "庫瓦點值";
                break;
            case 3:
                title = "雙閃幣";
                break;
            case 4:
                title = "電子錢包";
                break;
        }
        initToolbar(true, title);
        initRecylcerViewAndTabLayout();
    }


    private void initRecylcerViewAndTabLayout() {
        ViewPager viewPager = findViewById(R.id.include_viewpager);
        TabLayout tabLayout = findViewById(R.id.include_tabLayout);
        fragmentList = new ArrayList<>();
        Fragment_Point fragment_point;
        for (int i = 1; i <= count; i++) {
            fragment_point = new Fragment_Point();
            fragment_point.setPoint_type(point_type);
            fragment_point.setType(i);
            fragmentList.add(fragment_point);
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
        setFilter();
    }

    public void setFilter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (point_type) {
                    case 1:
                        json1 = new BillJsonData().getPolk(gv.getToken(), 1);
                        json2 = new BillJsonData().getPolk(gv.getToken(), 2);
                        json3 = new BillJsonData().getPolk(gv.getToken(), 3);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView) findViewById(R.id.activity_point)).setText(StringUtil.getDeciamlString(json1.getJSONObject("Data").getString("point")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                fragmentList.get(0).setFilter(json1);
                                fragmentList.get(1).setFilter(json2);
                                fragmentList.get(2).setFilter(json3);
                            }
                        });
                        break;
                    case 2:
                        json1 = new BillJsonData().getKuva(gv.getToken(), 1);
                        json2 = new BillJsonData().getKuva(gv.getToken(), 2);
                        json3 = new BillJsonData().getKuva(gv.getToken(), 3);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView) findViewById(R.id.activity_point)).setText(StringUtil.getDeciamlString(json1.getJSONObject("Data").getString("point")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                fragmentList.get(0).setFilter(json1);
                                fragmentList.get(1).setFilter(json2);
                                fragmentList.get(2).setFilter(json3);
                            }
                        });
                        break;
                    case 3:
                        json1 = new BillJsonData().getAcoin(gv.getToken(), 1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView) findViewById(R.id.activity_point)).setText(StringUtil.getDeciamlString(json1.getJSONObject("Data").getString("point")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                fragmentList.get(0).setFilter(json1);
                            }
                        });
                        break;
                    case 4:
                        json1 = new BillJsonData().getEwallet(gv.getToken(), 1);
                        json2 = new BillJsonData().getEwallet(gv.getToken(), 2);
                        json3 = new BillJsonData().getEwallet(gv.getToken(), 3);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView) findViewById(R.id.activity_point)).setText(StringUtil.getDeciamlString(json1.getJSONObject("Data").getString("point")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                fragmentList.get(0).setFilter(json1);
                                fragmentList.get(1).setFilter(json2);
                                fragmentList.get(2).setFilter(json3);
                            }
                        });
                        break;
                }


            }
        }).start();
    }
}
