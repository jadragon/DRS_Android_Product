package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import adapter.recyclerview.ReturnAndRefundViewAdapter;
import library.Component.ToastMessageDialog;
import library.Component.ToolbarActivity;
import library.GetJsonData.OrderInfoJsonData;
import library.JsonDataThread;

public class ReturnAndRefundActivity extends ToolbarActivity {
    RecyclerView recyclerView;
    ReturnAndRefundViewAdapter adapter;
    GlobalVariable gv;
    String mono;
    Button comfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_and_refund);
        gv = (GlobalVariable) getApplicationContext();
        mono = getIntent().getStringExtra("mono");
        initToolbar(true, "申請退換貨");
        initRecyclerView();
        comfirm = findViewById(R.id.appreciate_confirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, String> map = adapter.getApplyReturn();
                Log.e("return", "" + map);
                new JsonDataThread() {
                    @Override
                    public JSONObject getJsonData() {
                        return new OrderInfoJsonData().applyReturn(gv.getToken(), map.get("type"), mono, map.get("moinoArray"), map.get("numArray"), map.get("note"), null, null, null, null);
                    }

                    @Override
                    public void runUiThread(JSONObject json) {
                        try {
                            if (json.getBoolean("Success")) {
                                finish();
                            } else {
                                new ToastMessageDialog(ReturnAndRefundActivity.this, json.getString("Message")).confirm();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    private void initRecyclerView() {
        findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView = findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReturnAndRefundViewAdapter(this, null);
        recyclerView.setAdapter(adapter);
        setFilter();
    }

    public void setFilter() {
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                return new OrderInfoJsonData().getMOrderReturnItem(gv.getToken(), mono);
            }

            @Override
            public void runUiThread(JSONObject json) {
                adapter.setFilter(json);
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            byte[] bis;
            switch (requestCode) {
                case 100:
                    bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 0);

                    break;
                case 200:

                    bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 1);

                    break;
                case 300:

                    bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 2);

                    break;
                case 400:
                    bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 3);
                    break;
                case 500:
                    bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 4);

                    break;
                case 600:
                    bis = data.getByteArrayExtra("picture");
                    adapter.setPhotos(BitmapFactory.decodeByteArray(bis, 0, bis.length), 5);
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    }


}
