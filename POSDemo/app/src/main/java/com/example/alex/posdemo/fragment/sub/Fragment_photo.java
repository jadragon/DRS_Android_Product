package com.example.alex.posdemo.fragment.sub;

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
import com.example.alex.posdemo.adapter.recylclerview.PhotoListAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.GridSpacingItemDecoration;
import library.Component.ToastMessageDialog;
import library.JsonApi.PhotoApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_photo extends Fragment {
    View v;
    RecyclerView album_recyclerview;
    PhotoListAdapter photoListAdapter;
    Button album_multidelete, album_cancel, album_confirm;
    String a_no;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_album_layout, container, false);
        initDeleteButton();
        initButton();
        a_no = getArguments().getString("a_no");
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public void onTaskBefore() {

            }

            @Override
            public JSONObject onTasking(Void... params) {
                return new PhotoApi().photo_data(a_no);
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
        album_cancel.setVisibility(View.GONE);
        album_confirm.setVisibility(View.GONE);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.album_cancel:
                        album_cancel.setVisibility(View.GONE);
                        album_confirm.setVisibility(View.GONE);
                        album_multidelete.setVisibility(View.VISIBLE);
                        photoListAdapter.changeType(AlbumListAdapter.TYPE_NORMAL);
                        break;
                    case R.id.album_confirm:
                        if (photoListAdapter != null) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public void onTaskBefore() {

                                }

                                @Override
                                public JSONObject onTasking(Void... params) {
                                    //  return new AlbumApi().remove_album(albumListAdapter.showCheckedItem());
                                    return null;
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                        photoListAdapter.resetAdapter();
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
                photoListAdapter.changeType(AlbumListAdapter.TYPE_SELECT);
            }
        });
    }

    private void initRecyclerView(JSONObject jsonObject) {
        if (photoListAdapter != null && album_recyclerview != null) {
            photoListAdapter.setFilter(jsonObject);
        } else {
            album_recyclerview = v.findViewById(R.id.album_recyclerview);
            photoListAdapter = new PhotoListAdapter(getContext(), jsonObject, a_no, AlbumListAdapter.TYPE_NORMAL);
            int spanCount = 6;//跟布局里面的spanCount属性是一致的
            int spacing = 20;//每一个矩形的间距
            boolean includeEdge = true;//如果设置成false那边缘地带就没有间距
            //设置每个item间距
            album_recyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            //   GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 6,GridLayoutManager.VERTICAL,false);
            //    album_recyclerview.setLayoutManager(layoutManager);
            album_recyclerview.setAdapter(photoListAdapter);
        }

    }
}
