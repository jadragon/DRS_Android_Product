package com.company.alex.ordersystemdemo.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.company.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.company.alex.ordersystemdemo.API.List.RestaurantApi;
import com.company.alex.ordersystemdemo.R;
import com.company.alex.ordersystemdemo.RecyclerViewAdapter.StoreListAdapter;
import com.company.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.company.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONObject;


public class Fragment_storelist extends Fragment {
    private RecyclerView recyclerView;
    private View v;
    private SwipeRefreshLayout mSwipeLayout;
    private StoreListAdapter storeListAdapter;
    private String type;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        type = getArguments().getString("type");
        initViewPagerAndRecyclerView();
        initSwipeLayout();
        return v;
    }

    private void initSwipeLayout() {
        mSwipeLayout = v.findViewById(R.id.include_swipe_refresh);
        mSwipeLayout.setColorSchemeColors(Color.RED);
        //設定刷新動作
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return new RestaurantApi().store_data(type);
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                storeListAdapter.setFilter(jsonObject);
                            }
                        }else {
                            Toast.makeText(getContext(), "連線異常", Toast.LENGTH_SHORT).show();
                        }
                        mSwipeLayout.setRefreshing(false);
                    }
                });

            }

        });
    }

    private void initViewPagerAndRecyclerView() {
        recyclerView = v.findViewById(R.id.include_recyclerview);
        storeListAdapter = new StoreListAdapter(getContext(), null);
        recyclerView.setAdapter(storeListAdapter);
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new RestaurantApi().store_data(type);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        storeListAdapter.setFilter(jsonObject);
                    }
                }else {
                    Toast.makeText(getContext(), "連線異常", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
