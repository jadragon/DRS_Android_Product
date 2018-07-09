package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import adapter.recyclerview.AppreciateRecyclerViewAdapter;
import library.Component.ToolbarActivity;
import library.GetJsonData.OrderInfoJsonData;
import library.JsonDataThread;

public class AppreciateActivity extends ToolbarActivity {
    RecyclerView recyclerView;
    AppreciateRecyclerViewAdapter adapter;
    Button appreciate_confirm;
    GlobalVariable gv;
    String mono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciate);
        gv = ((GlobalVariable) getApplicationContext());
        mono = getIntent().getStringExtra("mono");
        initToolbar(true, "給予評價");
        initRecyclerView();
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                return new OrderInfoJsonData().getOrderComment(gv.getToken(), mono);
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
                        return new OrderInfoJsonData().setOrderComment(gv.getToken(), adapter.getJSONArray());
                    }

                    @Override
                    public void runUiThread(JSONObject json) {
                        try {
                            Toast.makeText(AppreciateActivity.this, "" + json.getString("Message"), Toast.LENGTH_SHORT).show();
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
