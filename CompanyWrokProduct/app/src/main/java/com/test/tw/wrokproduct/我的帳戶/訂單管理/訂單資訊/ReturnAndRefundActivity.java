package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import java.util.Map;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import adapter.recyclerview.ReturnAndRefundViewAdapter;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.ToastMessageDialog;
import library.Component.ToolbarActivity;
import library.GetJsonData.OrderInfoJsonData;

public class ReturnAndRefundActivity extends ToolbarActivity {
    private RecyclerView recyclerView;
    private ReturnAndRefundViewAdapter adapter;
    private GlobalVariable gv;
    private String mono;
    private Button comfirm;
    private ToastMessageDialog toastMessageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_and_refund);
        gv = (GlobalVariable) getApplicationContext();
        toastMessageDialog = new ToastMessageDialog(this);
        mono = getIntent().getStringExtra("mono");
        initToolbar(true, "申請退換貨");
        initRecyclerView();
        comfirm = findViewById(R.id.appreciate_confirm);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, String> map = adapter.getApplyReturn();
                toastMessageDialog.setTitleText("注意");
                if (map.get("type").equals("0")) {
                    toastMessageDialog.setMessageText("請選擇申請");
                    toastMessageDialog.confirm();
                } else if (map.get("numArray").equals("")) {
                    toastMessageDialog.setMessageText("請勾選待退商品");
                    toastMessageDialog.confirm();
                } else if (map.get("hasZero").equals("1")) {
                    toastMessageDialog.setMessageText("勾選商品數量不能為0");
                    toastMessageDialog.confirm();
                } else if (map.get("note").equals("")) {
                    toastMessageDialog.setMessageText("請填寫退換貨原因");
                    toastMessageDialog.confirm();
                } else {
                    toastMessageDialog.setTitleText("申請退換貨");
                    toastMessageDialog.showCheck(false, new ToastMessageDialog.ClickListener() {
                        @Override
                        public void ItemClicked(Dialog dialog, View view, String note) {
                            AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                @Override
                                public JSONObject onTasking(Void... params) {
                                    return new OrderInfoJsonData().applyReturn(gv.getToken(), map.get("type"), mono, map.get("moinoArray"), map.get("numArray"), map.get("note"), null, null, null, null);
                                }

                                @Override
                                public void onTaskAfter(JSONObject jsonObject) {
                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                        finish();
                                    } else {
                                        new ToastMessageDialog(ReturnAndRefundActivity.this, AnalyzeUtil.getMessage(jsonObject)).confirm();
                                    }

                                }
                            });
                            dialog.dismiss();
                        }

                    });

                }
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
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new OrderInfoJsonData().getMOrderReturnItem(gv.getToken(), mono);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                adapter.setFilter(jsonObject);
            }
        });
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
