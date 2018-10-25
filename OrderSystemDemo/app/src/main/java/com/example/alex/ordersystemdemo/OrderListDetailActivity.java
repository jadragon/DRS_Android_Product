package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.example.alex.ordersystemdemo.API.Analyze.Pojo.OrderDataPojo;
import com.example.alex.ordersystemdemo.API.List.OrderApi;
import com.example.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.example.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONObject;

public class OrderListDetailActivity extends ToolbarAcitvity {
    private GlobalVariable gv;
    private OrderDataPojo orderDataPojo;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_detail);
        status = getIntent().getStringExtra("status");
        orderDataPojo = (OrderDataPojo) getIntent().getSerializableExtra("OrderDataPojo");
        gv = (GlobalVariable) getApplicationContext();
        initToolbar("訂單資訊", true, false);
        initInfomation();
        initButton();
    }

    private void initInfomation() {
        //學生
        ((TextView) findViewById(R.id.m_name)).setText(orderDataPojo.getM_name());
        ((TextView) findViewById(R.id.m_phone)).setText(orderDataPojo.getM_phone());
        ((TextView) findViewById(R.id.m_address)).setText(orderDataPojo.getM_address());
        //外送員
        ((TextView) findViewById(R.id.d_name)).setText(orderDataPojo.getD_name());
        ((TextView) findViewById(R.id.d_phone)).setText(orderDataPojo.getD_phone());
        //商家
        ((TextView) findViewById(R.id.s_name)).setText(orderDataPojo.getS_name());
        ((TextView) findViewById(R.id.s_phone)).setText(orderDataPojo.getS_phone());
        ((TextView) findViewById(R.id.s_address)).setText(orderDataPojo.getS_address());
        ((TextView) findViewById(R.id.s_complete_time)).setText(orderDataPojo.getS_complete_time());

        //點餐資訊
        ((TextView) findViewById(R.id.f_content)).setText(orderDataPojo.getF_content());
        ((TextView) findViewById(R.id.f_sum)).setText(orderDataPojo.getF_sum());

    }

    private void initButton() {
        Button button = findViewById(R.id.confirm);
        switch (status) {
            case "1":
                switch (gv.getType()) {
                    case 0:
                        button.setText("取消");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new OrderApi().order_cancel(orderDataPojo.getO_id(), gv.getToken());
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                            finish();
                                        }
                                        Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        break;
                    case 1:
                        button.setText("接受");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new OrderApi().order_accept(orderDataPojo.getO_id(), gv.getToken(),"23:35");
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                            finish();
                                        }
                                        Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        break;
                    case 2:

                        break;
                }

                break;
            case "2":
                switch (gv.getType()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        button.setText("接單");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new OrderApi().order_delivery_man(orderDataPojo.getO_id(), gv.getToken());
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                            finish();
                                        }
                                        Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        break;
                }

                break;
            case "3":
                switch (gv.getType()) {
                    case 0:

                        break;
                    case 1:
                        button.setText("確認轉移");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new OrderApi().order_gainFood(orderDataPojo.getO_id(), gv.getToken());
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                            finish();
                                        }
                                        Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        break;
                    case 2:
                        break;
                }

                break;
            case "4":
                switch (gv.getType()) {
                    case 0:
                        button.setText("確認取餐");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new OrderApi().order_complete(orderDataPojo.getO_id(), gv.getToken());
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                            finish();
                                        }
                                        Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        break;
                    case 1:
                        break;
                    case 2:

                        break;
                }

                break;
            case "5":
                break;
        }

    }
}
