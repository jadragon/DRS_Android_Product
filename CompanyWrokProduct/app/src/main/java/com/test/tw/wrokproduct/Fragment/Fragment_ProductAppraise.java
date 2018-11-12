package com.test.tw.wrokproduct.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import adapter.recyclerview.ProductAppraiseRecyclerViewAdapter;
import library.GetJsonData.ProductJsonData;

@SuppressLint("ValidFragment")
public class Fragment_ProductAppraise extends Fragment {
    private  RecyclerView recyclerView;
    private View v;
    private  ProductAppraiseRecyclerViewAdapter adapter;
    private  GlobalVariable gv;
    private String pno;

    @SuppressLint("ValidFragment")
    public Fragment_ProductAppraise(String pno) {
        this.pno = pno;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        initRecyclerView();
        return v;
    }

    private void initRecyclerView() {
        v.findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView = v.findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductAppraiseRecyclerViewAdapter(getContext(), null);
        recyclerView.setAdapter(adapter);
        setFilter();
    }

    public void setFilter() {

        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new ProductJsonData().getProductComment(pno);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                adapter.setFilter(jsonObject);
            }
        });
    }
}
