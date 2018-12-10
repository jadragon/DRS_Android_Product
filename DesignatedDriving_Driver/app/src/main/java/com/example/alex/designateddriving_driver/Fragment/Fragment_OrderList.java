package com.example.alex.designateddriving_driver.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.designateddriving_driver.R;
import com.example.alex.designateddriving_driver.RecyclerAdapter.OrderListRecyclerAdapter;
import com.example.alex.designateddriving_driver.Utils.AsyncTaskUtils;
import com.example.alex.designateddriving_driver.Utils.IDataCallBack;

import org.json.JSONObject;


public class Fragment_OrderList extends Fragment {
    private View v;
    private OrderListRecyclerAdapter orderListRecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        initSwipeLayout();
        initRecylcerView();
        return v;

    }

    private void initSwipeLayout() {
        swipeRefreshLayout = v.findViewById(R.id.include_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        //設定刷新動作
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setFilter();
            }
        });
    }

    private void initRecylcerView() {
        RecyclerView recyclerView = v.findViewById(R.id.include_recyclerview);
        orderListRecyclerAdapter = new OrderListRecyclerAdapter(getContext());
        recyclerView.setAdapter(orderListRecyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setFilter();
    }

    public void setFilter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return null;
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
