package com.test.tw.wrokproduct.Fragment;

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

import adapter.recyclerview.ProductAppraiseRecyclerViewAdapter;
import library.Component.ToastMessageDialog;
import library.GetJsonData.OrderInfoJsonData;
import library.JsonDataThread;

public class Fragment_Myappreciate extends Fragment {
    JSONObject json;
    View v;
    int type, rule;
    GlobalVariable gv;
    ToastMessageDialog toastMessageDialog;
    ProductAppraiseRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        toastMessageDialog = new ToastMessageDialog(getContext());
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
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                return new OrderInfoJsonData().getMyComment(gv.getToken(), rule);
            }

            @Override
            public void runUiThread(JSONObject json) {
                adapter.setFilter(json);
            }
        }.start();
    }
}
