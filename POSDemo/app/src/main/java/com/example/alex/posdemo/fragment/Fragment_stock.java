package com.example.alex.posdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.StockListAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.All_Store_BrandPojo;
import library.AnalyzeJSON.Analyze_StockInfo;
import library.JsonApi.StockApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_stock extends Fragment {
    View v;
    private UserInfo userInfo;
    RecyclerView stock_recyclerview;
    StockListAdapter stockListAdapter;
    Spinner stock_spinner_store, stock_spinner_brand;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_stock_layout, container, false);
        userInfo = (UserInfo) getContext().getApplicationContext();

        initSpinner();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return new StockApi().stock_data("bUhx03Gk6LAH8dZ8NkiNog==,a42LeDpQoCqr43FmcC@_Dpw==,UoCab6MiDrGKkyPwBXcJuA==",
                        "sl87fy2O9oevXUDAxG9l9Q==,XgH1q1g@_Cl0ay0/M9ST@_vQ==,mSdI5KxZJPcbsnzuUJUQnA==,bWg9NFf4nSTZAqt1AVrZ9A==,a42LeDpQoCqr43FmcC@_Dpw==",
                        "", "", "0", "");
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initRecyclerView(jsonObject);
            }
        });

        return v;
    }

    private void initSpinner() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {


            }

            @Override
            public JSONObject onTasking(Void... params) {
                return new StockApi().all_store_brand();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                All_Store_BrandPojo all_store_brandPojo = new Analyze_StockInfo().getAll_store_brand(jsonObject);
                stock_spinner_store = v.findViewById(R.id.stock_spinner_store);
                stock_spinner_store.setAdapter(new ArrayAdapter(getContext(),
                        android.R.layout.simple_list_item_1, all_store_brandPojo.getStore()));
                stock_spinner_brand = v.findViewById(R.id.stock_spinner_brand);
                stock_spinner_brand.setAdapter(new ArrayAdapter(getContext(),
                        android.R.layout.simple_list_item_1, all_store_brandPojo.getTitle()));
            }
        });
    }

    private void initRecyclerView(JSONObject jsonObject) {
        if (stock_recyclerview != null && stockListAdapter != null) {
            stockListAdapter.setFilter(jsonObject);
        }
        stock_recyclerview = v.findViewById(R.id.stock_recyclerview);
        stockListAdapter = new StockListAdapter(getContext(), jsonObject);
        stock_recyclerview.setAdapter(stockListAdapter);
    }

}
