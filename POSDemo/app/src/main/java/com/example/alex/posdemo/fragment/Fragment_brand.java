package com.example.alex.posdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.BrandListAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.GridSpacingItemDecoration;
import library.JsonApi.BrandApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_brand extends Fragment {
    View v;
    RecyclerView brand_recyclerview;
    BrandListAdapter brandListAdapter;
    EditText brand_edit_namesearch, brand_edit_codesearch;
    ImageView brand_btn_namesearch, brand_btn_codesearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_brand_layout, container, false);
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new BrandApi().brand_data();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initRecyclerView(jsonObject);
            }
        });
        initSearch();
        return v;
    }

    private void initSearch() {
        brand_edit_namesearch = v.findViewById(R.id.brand_edit_namesearch);
        brand_edit_codesearch = v.findViewById(R.id.brand_edit_codesearch);
        brand_btn_namesearch = v.findViewById(R.id.brand_btn_namesearch);
        brand_btn_codesearch = v.findViewById(R.id.brand_btn_codesearch);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.brand_btn_namesearch:

                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

                            @Override
                            public JSONObject onTasking(Void... params) {
                                return new BrandApi().search_name(brand_edit_namesearch.getText().toString());
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                    brandListAdapter.setFilter(jsonObject);
                                }
                            }
                        });

                        break;

                    case R.id.brand_btn_codesearch:
                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

                            @Override
                            public JSONObject onTasking(Void... params) {
                                return new BrandApi().search_code(brand_edit_codesearch.getText().toString());
                            }

                            @Override
                            public void onTaskAfter(JSONObject jsonObject) {
                                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                    brandListAdapter.setFilter(jsonObject);
                                }
                            }
                        });
                        break;
                }
            }
        };
        brand_btn_namesearch.setOnClickListener(onClickListener);
        brand_btn_codesearch.setOnClickListener(onClickListener);
    }


    private void initRecyclerView(JSONObject jsonObject) {
        if (brandListAdapter != null && brand_recyclerview != null) {
            brandListAdapter.setFilter(jsonObject);
            return;
        }
        brand_recyclerview = v.findViewById(R.id.brand_recyclerview);
        brandListAdapter = new BrandListAdapter(getContext(), jsonObject);
        int spanCount = 3;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = true;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        brand_recyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //   GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 6,GridLayoutManager.VERTICAL,false);
        //    album_recyclerview.setLayoutManager(layoutManager);
        brand_recyclerview.setAdapter(brandListAdapter);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            brandListAdapter.resetAdapter();
        }
    }
}
