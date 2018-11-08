package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.RestaurantApi;
import com.example.alex.ordersystemdemo.Fragment.Fragment_storelist;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreListActivity extends ToolbarAcitvity {

    private List<Fragment> fragmentArrayList;
    private ArrayList<String> tablist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        setNavigationType(1);
        initToolbar("商家列表", true, true);
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new RestaurantApi().store_type();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tablist.add(jsonArray.getString(i));
                            }
                            initViewPagerAndTabLayout();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Toast.makeText(StoreListActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initViewPagerAndTabLayout() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        fragmentArrayList = new ArrayList<>();
        ((TabLayout) findViewById(R.id.tablayout)).setTabMode(TabLayout.MODE_SCROLLABLE);
        Fragment_storelist fragment_storelist = null;
        Bundle bundle = null;
        for (String type : tablist) {
            fragment_storelist = new Fragment_storelist();
            bundle = new Bundle();
            bundle.putString("type", type);
            fragment_storelist.setArguments(bundle);
            fragmentArrayList.add(fragment_storelist);
        }
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
                return tablist.get(position);
            }
        });
    }

}
