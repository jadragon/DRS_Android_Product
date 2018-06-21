package com.test.tw.wrokproduct.我的帳戶.訂單管理.收貨地址;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Fragment.Fragment_shipAddress;
import library.GetJsonData.LogisticsJsonData;

public class ShipAddressActivity extends AppCompatActivity {
    JSONObject json1, json2;
    String token;
    List<Fragment_shipAddress> fragmentList;
    ViewPager viewpager;
    String[] mTabtitle = {"超商地址", "一般地址"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        initToolbar();
        initRecylcerViewAndTabLayout();
    }

    private void initRecylcerViewAndTabLayout() {
        viewpager = findViewById(R.id.contact_viewpager);
        fragmentList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json1 = new LogisticsJsonData().getLogistics(token, 0);
                json2 = new LogisticsJsonData().getLogistics(token, 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                Fragment_shipAddress fragment_shipAddress=new Fragment_shipAddress();
                        fragment_shipAddress.setJson(json1);
                        fragment_shipAddress.setType(0);
                        fragmentList.add(fragment_shipAddress);
                        fragment_shipAddress = new Fragment_shipAddress();
                        fragment_shipAddress.setJson(json2);
                        fragment_shipAddress.setType(1);
                        fragmentList.add(fragment_shipAddress);
                        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                        });
                    }
                });
            }
        }).start();

    }

    /*
        private void initRecyclerView() {
            recyclerView = findViewById(R.id.ship_address_recyclerview);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(ShipAddressActivity.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    json = new LogisticsJsonData().getLogistics(token, type);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ShipAddressRecyclerAdapter(ShipAddressActivity.this, json, type);
                            recyclerView.setAdapter(adapter);
                        }
                    });

                }
            }).start();


        }
    */
    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("收貨地址");
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    json1 = new LogisticsJsonData().getLogistics(token, 0);
                    json2 = new LogisticsJsonData().getLogistics(token, 1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragmentList.get(0).setFilter(json1);
                            fragmentList.get(1).setFilter(json2);
                        }
                    });
                }
            }).start();

        }
    }
}
