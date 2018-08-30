package com.example.alex.posdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.AlbumListAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.Component.GridSpacingItemDecoration;
import library.JsonApi.AlbumApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_brand extends Fragment {
    View v;
    RecyclerView album_recyclerview;
    AlbumListAdapter albumListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_brand_layout, container, false);
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return new AlbumApi().album_data();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initRecyclerView(jsonObject);
            }
        });
        return v;
    }


    private void initRecyclerView(JSONObject jsonObject) {
        if (albumListAdapter != null && album_recyclerview != null) {
            albumListAdapter.setFilter(jsonObject);
        } else {
            album_recyclerview = v.findViewById(R.id.album_recyclerview);
            albumListAdapter = new AlbumListAdapter(getContext(), jsonObject, AlbumListAdapter.TYPE_NORMAL);
            int spanCount = 6;//跟布局里面的spanCount属性是一致的
            int spacing = 20;//每一个矩形的间距
            boolean includeEdge = true;//如果设置成false那边缘地带就没有间距
            //设置每个item间距
            album_recyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            //   GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 6,GridLayoutManager.VERTICAL,false);
            //    album_recyclerview.setLayoutManager(layoutManager);
            album_recyclerview.setAdapter(albumListAdapter);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();
            albumListAdapter.resetAdapter();
        }
    }

}
