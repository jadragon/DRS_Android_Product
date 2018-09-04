package com.example.alex.posdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.AlbumListAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.GridSpacingItemDecoration;
import library.Component.ToastMessageDialog;
import library.JsonApi.AlbumApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_album extends Fragment {
    View v;
    RecyclerView album_recyclerview;
    AlbumListAdapter albumListAdapter;
    Button album_multidelete, photo_changecover, album_cancel, album_confirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_album_layout, container, false);
        initDeleteButton();
        initButton();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
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

    private void initButton() {
        album_cancel = v.findViewById(R.id.album_cancel);
        album_confirm = v.findViewById(R.id.album_confirm);
        photo_changecover = v.findViewById(R.id.photo_changecover);
        album_cancel.setVisibility(View.GONE);
        album_confirm.setVisibility(View.GONE);
        photo_changecover.setVisibility(View.GONE);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.album_cancel:
                        album_cancel.setVisibility(View.GONE);
                        album_confirm.setVisibility(View.GONE);
                        album_multidelete.setVisibility(View.VISIBLE);
                        albumListAdapter.changeType(AlbumListAdapter.TYPE_NORMAL);
                        break;
                    case R.id.album_confirm:
                        if (albumListAdapter != null) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

                                @Override
                                public JSONObject onTasking(Void... params) {
                                    return new AlbumApi().remove_album(albumListAdapter.showCheckedItem());
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                        albumListAdapter.resetAdapter();
                                    }
                                    new ToastMessageDialog(getContext(), ToastMessageDialog.TYPE_INFO).confirm(AnalyzeUtil.getMessage(jsonObject));
                                }
                            });
                        }
                        break;
                }
            }
        };
        album_cancel.setOnClickListener(onClickListener);
        album_confirm.setOnClickListener(onClickListener);

    }

    private void initDeleteButton() {
        album_multidelete = v.findViewById(R.id.album_multidelete);
        album_multidelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                album_cancel.setVisibility(View.VISIBLE);
                album_confirm.setVisibility(View.VISIBLE);
                album_multidelete.setVisibility(View.GONE);
                albumListAdapter.changeType(AlbumListAdapter.TYPE_SELECT);
            }
        });
    }

    private void initRecyclerView(JSONObject jsonObject) {
        if (albumListAdapter != null && album_recyclerview != null) {
            albumListAdapter.setFilter(jsonObject);
            return;
        }
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
