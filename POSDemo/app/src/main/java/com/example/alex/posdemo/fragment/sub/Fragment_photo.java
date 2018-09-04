package com.example.alex.posdemo.fragment.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.PhotoListAdapter;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.GridSpacingItemDecoration;
import library.Component.ToastMessageDialog;
import library.JsonApi.AlbumApi;
import library.JsonApi.PhotoApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_photo extends Fragment {
    View v;
    RecyclerView album_recyclerview;
    PhotoListAdapter photoListAdapter;
    Button album_multidelete, photo_changecover, album_cancel, album_confirm;
    String a_no;
    byte type;
    ImageView album_btn_search;
    EditText album_edit_search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_album_layout, container, false);
        initSearch();
        initDeleteButton();
        initButton();
        a_no = getArguments().getString("a_no");
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

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

    private void initSearch() {
        album_btn_search = v.findViewById(R.id.album_btn_search);
        album_edit_search = v.findViewById(R.id.album_edit_search);
        album_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

                    @Override
                    public JSONObject onTasking(Void... params) {
                        return new PhotoApi().search_photo(album_edit_search.getText().toString());
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        photoListAdapter.setFilter(jsonObject);
                    }
                });
            }

        });
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
                        photo_changecover.setVisibility(View.VISIBLE);
                        type = PhotoListAdapter.TYPE_NORMAL;
                        photoListAdapter.changeType(type);
                        break;
                    case R.id.album_confirm:
                        if (photoListAdapter != null) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public JSONObject onTasking(Void... params) {
                                    if (type == PhotoListAdapter.TYPE_CHECK)
                                        return new PhotoApi().remove_photo(photoListAdapter.showCheckedItem());
                                    else
                                        return new AlbumApi().update_album_cover(a_no, photoListAdapter.getAlbumCover());
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
        photo_changecover = v.findViewById(R.id.photo_changecover);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.album_multidelete:
                        album_cancel.setVisibility(View.VISIBLE);
                        album_confirm.setVisibility(View.VISIBLE);
                        album_multidelete.setVisibility(View.GONE);
                        photo_changecover.setVisibility(View.GONE);
                        type = PhotoListAdapter.TYPE_CHECK;
                        photoListAdapter.changeType(type);

                        break;
                    case R.id.photo_changecover:
                        album_cancel.setVisibility(View.VISIBLE);
                        album_confirm.setVisibility(View.VISIBLE);
                        album_multidelete.setVisibility(View.GONE);
                        photo_changecover.setVisibility(View.GONE);
                        type = PhotoListAdapter.TYPE_SELECT;
                        photoListAdapter.changeType(type);
                        break;
                }
            }
        };
        album_multidelete.setOnClickListener(onClickListener);
        photo_changecover.setOnClickListener(onClickListener);
    }

    private void initRecyclerView(JSONObject jsonObject) {
        if (photoListAdapter != null && album_recyclerview != null) {
            photoListAdapter.setFilter(jsonObject);
            return;
        }

        album_recyclerview = v.findViewById(R.id.album_recyclerview);
        type = PhotoListAdapter.TYPE_NORMAL;
        photoListAdapter = new PhotoListAdapter(getContext(), jsonObject, a_no, type);
        int spanCount = 6;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = true;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        album_recyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //   GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 6,GridLayoutManager.VERTICAL,false);
        //    album_recyclerview.setLayoutManager(layoutManager);
        album_recyclerview.setAdapter(photoListAdapter);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoListAdapter.resetAdapter();
    }

}
