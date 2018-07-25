package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import adapter.recyclerview.AppreciateRecyclerViewAdapter;
import library.Component.ToastMessageDialog;
import library.Component.ToolbarActivity;
import library.GetJsonData.OrderInfoJsonData;
import library.GetJsonData.StoreJsonData;
import library.JsonDataThread;

public class AppreciateActivity extends ToolbarActivity {
    RecyclerView recyclerView;
    AppreciateRecyclerViewAdapter adapter;
    Button appreciate_confirm;

    String mono, token, type;
ToastMessageDialog toastMessageDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciate);
        toastMessageDialog=new ToastMessageDialog(this);
        token = getIntent().getStringExtra("token");
        mono = getIntent().getStringExtra("mono");
        type = getIntent().getStringExtra("type");
        if (type.equals("0")) {
            initToolbar(true, "給賣家評價");
        } else if (type.equals("1")) {
            initToolbar(true, "給買家評價");
        }
        initRecyclerView();
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                if (type.equals("0")) {
                    return new OrderInfoJsonData().getOrderComment(token, mono);
                } else if (type.equals("1")) {
                    return new StoreJsonData().getOrderComment(token, mono);
                } else {
                    return null;
                }

            }

            @Override
            public void runUiThread(JSONObject json) {
                adapter.setFilter(json);
            }
        }.start();
        appreciate_confirm = findViewById(R.id.appreciate_confirm);
        appreciate_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JsonDataThread() {
                    @Override
                    public JSONObject getJsonData() {
                        if (type.equals("0")) {
                            return new OrderInfoJsonData().setOrderComment(token, adapter.getJSONArray());
                        } else if (type.equals("1")) {
                            return new StoreJsonData().setOrderComment(token, adapter.getJSONArray());
                        } else {
                            return null;
                        }

                    }

                    @Override
                    public void runUiThread(JSONObject json) {
                        try {
                            toastMessageDialog.setMessageText(json.getString("Message"));
                            toastMessageDialog.confirm();
                            if (json.getBoolean("Success")) {
                                finish();
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppreciateActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AppreciateRecyclerViewAdapter(AppreciateActivity.this, null);
        recyclerView.setAdapter(adapter);
    }


}
