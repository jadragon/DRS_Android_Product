package com.example.alex.ordersystemdemo.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.RestaurantApi;
import com.example.alex.ordersystemdemo.R;
import com.example.alex.ordersystemdemo.RecyclerViewAdapter.StoreListAdapter;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                }, 2000);
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
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    storeListAdapter.setFilter(jsonObject);
                }
            }
        });

    }

}
