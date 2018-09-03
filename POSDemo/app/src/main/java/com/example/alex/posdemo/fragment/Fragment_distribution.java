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
import com.example.alex.posdemo.adapter.recylclerview.DistributionListAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.All_Store_BrandPojo;
import library.AnalyzeJSON.Analyze_StockInfo;
import library.JsonApi.DistributionApi;
import library.JsonApi.StockApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_distribution extends Fragment {
    View v;
    private UserInfo userInfo;
    RecyclerView distribution_recyclerview;
    DistributionListAdapter distributionListAdapter;
    Spinner distribution_spinner_brand;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_distribution_layout, container, false);
        userInfo = (UserInfo) getContext().getApplicationContext();

        initSpinner();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return new DistributionApi().distribution("sl87fy2O9oevXUDAxG9l9Q==,XgH1q1g@_Cl0ay0/M9ST@_vQ==,mSdI5KxZJPcbsnzuUJUQnA==,bWg9NFf4nSTZAqt1AVrZ9A==,a42LeDpQoCqr43FmcC@_Dpw==", "",
                        "", 0);
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
                distribution_spinner_brand = v.findViewById(R.id.distribution_spinner_brand);
                distribution_spinner_brand.setAdapter(new ArrayAdapter(getContext(),
                        android.R.layout.simple_list_item_1, all_store_brandPojo.getStore()));
            }
        });
    }

    private void initRecyclerView(JSONObject jsonObject) {
        if (distribution_recyclerview != null && distributionListAdapter != null) {
            distributionListAdapter.setFilter(jsonObject);
            return;
        }
        distribution_recyclerview = v.findViewById(R.id.distribution_recyclerview);
        distributionListAdapter = new DistributionListAdapter(getContext(), jsonObject);
        distribution_recyclerview.setAdapter(distributionListAdapter);


    }

}
