package com.example.alex.ordersystemdemo.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.List.OrderApi;
import com.example.alex.ordersystemdemo.GlobalVariable;
import com.example.alex.ordersystemdemo.R;
import com.example.alex.ordersystemdemo.RecyclerViewAdapter.OrderListAdapter;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONObject;


public class Fragment_orderlist extends Fragment {
    private RecyclerView recyclerView;
    private View v;
    private SwipeRefreshLayout mSwipeLayout;
    private OrderListAdapter orderListAdapter;
    private String status;
    private GlobalVariable gv;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        status = getArguments().getString("status");
        gv = (GlobalVariable) getContext().getApplicationContext();
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
                        return new OrderApi().order_data(status,gv.getType()+"");
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            orderListAdapter.setFilter(jsonObject);
                        } else {

                        }
                        mSwipeLayout.setRefreshing(false);
                    }
                });
            }

        });
    }

    private void initViewPagerAndRecyclerView() {
        recyclerView = v.findViewById(R.id.include_recyclerview);
        orderListAdapter = new OrderListAdapter(getContext(), null,status);
        recyclerView.setAdapter(orderListAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_1dp_gray));
        recyclerView.addItemDecoration(decoration);

        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new OrderApi().order_data(status,gv.getType()+"");
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    orderListAdapter.setFilter(jsonObject);
                }
            }
        });

    }

}
