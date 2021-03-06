package com.test.tw.wrokproduct.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.recyclerview.ShipAddressRecyclerAdapter;

public class Fragment_shipAddress extends Fragment {
    private  View v;
    private ShipAddressRecyclerAdapter adapter;
    private  RecyclerView recyclerView;
    private JSONObject json;
    private  int type;

    public void setType(int type) {
        this.type = type;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        v.findViewById(R.id.include_swipe_refresh).setEnabled(false);
        initRecyclerView();
        return v;
    }

    private void initRecyclerView() {
        recyclerView = v.findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShipAddressRecyclerAdapter(getContext(), json, type);
        recyclerView.setAdapter(adapter);
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        adapter.setFilter(json);
    }

}
