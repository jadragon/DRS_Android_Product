package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.eip_product.R;
import com.example.alex.eip_product.adapter.KeyWordRecyclerViewAdapter;

import Component.AutoNewLineLayoutManager;

public class Fragment_selectFailed extends Fragment {
    View v;
    RecyclerView recyclerView;
    KeyWordRecyclerViewAdapter keyWordRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_select_failed, container, false);
        recyclerView = v.findViewById(R.id.select_failed_recyclerview);
        keyWordRecyclerViewAdapter = new KeyWordRecyclerViewAdapter(getContext(), true, true, true, true, false, true);
        AutoNewLineLayoutManager autoNewLineLayoutManager = new AutoNewLineLayoutManager(getContext());
        autoNewLineLayoutManager.setDivider(20);
        autoNewLineLayoutManager.setAloneViewType(KeyWordRecyclerViewAdapter.TYPE_HEADER);
        recyclerView.setLayoutManager(autoNewLineLayoutManager);
        recyclerView.setAdapter(keyWordRecyclerViewAdapter);
        return v;
    }

}
