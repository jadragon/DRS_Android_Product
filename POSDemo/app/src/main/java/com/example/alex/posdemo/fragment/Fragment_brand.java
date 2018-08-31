package com.example.alex.posdemo.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.BrandListAdapter;

import org.json.JSONObject;

import java.io.IOException;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.Component.GridSpacingItemDecoration;
import library.JsonApi.BrandApi;

import static android.app.Activity.RESULT_OK;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_brand extends Fragment {
    View v;
    RecyclerView brand_recyclerview;
    BrandListAdapter brandListAdapter;

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
                return new BrandApi().brand_data();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                initRecyclerView(jsonObject);
            }
        });
        return v;
    }


    private void initRecyclerView(JSONObject jsonObject) {
        if (brandListAdapter != null && brand_recyclerview != null) {
            brandListAdapter.setFilter(jsonObject);
        } else {
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

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();
            brandListAdapter.resetAdapter();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { //此處的 RESULT_OK 是系統自定義得一個常量
            return;
        }
//外界的程式訪問ContentProvider所提供數據 可以通過ContentResolver介面
        ContentResolver resolver = getContext().getContentResolver();
//此處的用於判斷接收的Activity是不是你想要的那個
        if (requestCode == 200) {
            try {
                Uri originalUri = data.getData(); //獲得圖片的uri
         Bitmap  bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri); //顯得到bitmap圖片
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > height) {
                    bitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
                } else if (width < height) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
                }

                brandListAdapter.reloadImage(bitmap);

                /*
                // 這裏開始的第二部分，獲取圖片的路徑：
                String[] proj = {MediaStore.Images.Media.DATA};
//好像是android多媒體數據庫的封裝介面，具體的看Android文檔
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
//按我個人理解 這個是獲得用戶選擇的圖片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//將光標移至開頭 ，這個很重要，不小心很容易引起越界
                cursor.moveToFirst();
//最後根據索引值獲取圖片路徑
                String path = cursor.getString(column_index);
                */
            } catch (IOException e) {
            }

        }

    }
}
