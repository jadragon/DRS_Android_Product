package com.example.alex.ordersystemdemo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
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
    private String time = "0";

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
        ((TextView) findViewById(R.id.order_number)).setText("訂單單號:    " + orderDataPojo.getO_number());
        ((TextView) findViewById(R.id.m_name)).setText("學生姓名:    " + orderDataPojo.getM_name());
        ((TextView) findViewById(R.id.m_phone)).setText("學生手機:    " + orderDataPojo.getM_phone());
        ((TextView) findViewById(R.id.m_address)).setText("學生地址:    " + orderDataPojo.getM_address());
        //外送員
        ((TextView) findViewById(R.id.d_name)).setText("外送員姓名:    " + orderDataPojo.getD_name());
        ((TextView) findViewById(R.id.d_phone)).setText("外送員手機:    " + orderDataPojo.getD_phone());
        ((TextView) findViewById(R.id.s_time)).setText("預計送達時間:    " + orderDataPojo.getD_time());
        //商家
        ((TextView) findViewById(R.id.s_name)).setText("商家名稱:    " + orderDataPojo.getS_name());
        ((TextView) findViewById(R.id.s_phone)).setText("商家手機:    " + orderDataPojo.getS_phone());
        ((TextView) findViewById(R.id.s_address)).setText("商家地址:    " + orderDataPojo.getS_address());
        ((TextView) findViewById(R.id.s_complete_time)).setText("預計完成時間:    " + orderDataPojo.getS_complete_time());

        //點餐資訊
        ((TextView) findViewById(R.id.f_content)).setText("餐點內容:\n" + orderDataPojo.getF_content().replace(",","\n"));
        ((TextView) findViewById(R.id.f_sum)).setText(orderDataPojo.getF_sum());
        ((TextView) findViewById(R.id.m_note)).setText("備註:\n" + orderDataPojo.getM_note());

    }

    private void initButton() {
        Button button = findViewById(R.id.confirm);
        switch (status) {
            case "1":
                switch (gv.getType()) {
                    case 0:
                        button.setText("取消");
                        button.setBackgroundColor(getResources().getColor(R.color.red));
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
                                        if (jsonObject != null) {
                                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                setResult(100);
                                                finish();
                                            }
                                            Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(OrderListDetailActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        break;
                    case 1:
                        button.setText("接受");
                        button.setBackgroundColor(getResources().getColor(R.color.green));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                 AlertDialog.Builder builder = new AlertDialog.Builder(OrderListDetailActivity.this);
                                View view = LayoutInflater.from(OrderListDetailActivity.this).inflate(R.layout.dialog_time_selector, null);
                                View.OnClickListener onClickListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        switch (v.getTag().toString()) {
                                            case "5min":
                                                time = "5";
                                                break;
                                            case "10min":
                                                time = "10";
                                                break;
                                            case "15min":
                                                time = "15";
                                                break;
                                            case "20min":
                                                time = "20";
                                                break;
                                        }
                                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                            @Override
                                            public JSONObject onTasking(Void... params) {
                                                return new OrderApi().order_accept(orderDataPojo.getO_id(), gv.getToken(), time);
                                            }

                                            @Override
                                            public void onTaskAfter(JSONObject jsonObject) {
                                                if (jsonObject != null) {
                                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                        setResult(100);
                                                        finish();
                                                    }
                                                    Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(OrderListDetailActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                };
                                ((TextView)view.findViewWithTag("title")).setText("注意\n請選擇預計餐點完成時間");
                                view.findViewWithTag("5min").setOnClickListener(onClickListener);
                                view.findViewWithTag("10min").setOnClickListener(onClickListener);
                                view.findViewWithTag("15min").setOnClickListener(onClickListener);
                                view.findViewWithTag("20min").setOnClickListener(onClickListener);
                                builder.setView(view);
                                AlertDialog dialog=builder.create();
                                //dialog.getWindow().setBackgroundDrawableResource(R.color.invisible);
                                dialog.show();
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
                        button.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        button.setText("接單");
                        button.setBackgroundColor(getResources().getColor(R.color.green));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderListDetailActivity.this);
                                View view = LayoutInflater.from(OrderListDetailActivity.this).inflate(R.layout.dialog_time_selector, null);
                                View.OnClickListener onClickListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        switch (v.getTag().toString()) {
                                            case "5min":
                                                time = "5";
                                                break;
                                            case "10min":
                                                time = "10";
                                                break;
                                            case "15min":
                                                time = "15";
                                                break;
                                            case "20min":
                                                time = "20";
                                                break;
                                        }
                                        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                            @Override
                                            public JSONObject onTasking(Void... params) {
                                                return new OrderApi().order_delivery_man(orderDataPojo.getO_id(), gv.getToken(),time);
                                            }

                                            @Override
                                            public void onTaskAfter(JSONObject jsonObject) {
                                                if (jsonObject != null) {
                                                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                        setResult(100);
                                                        finish();
                                                    }
                                                    Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(OrderListDetailActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                };
                                ((TextView)view.findViewWithTag("title")).setText("注意\n請選擇預計送達時間");
                                view.findViewWithTag("5min").setOnClickListener(onClickListener);
                                view.findViewWithTag("10min").setOnClickListener(onClickListener);
                                view.findViewWithTag("15min").setOnClickListener(onClickListener);
                                view.findViewWithTag("20min").setOnClickListener(onClickListener);
                                builder.setView(view);
                                AlertDialog dialog=builder.create();
                                //dialog.getWindow().setBackgroundDrawableResource(R.color.invisible);
                                dialog.show();







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
                        button.setBackgroundColor(getResources().getColor(R.color.red));
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
                                        if (jsonObject != null) {
                                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                setResult(100);
                                                finish();
                                            }
                                            Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(OrderListDetailActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        break;
                    case 2:
                        button.setVisibility(View.INVISIBLE);
                        break;
                }

                break;
            case "4":
                switch (gv.getType()) {
                    case 0:
                        button.setText("確認取餐");
                        button.setBackgroundColor(getResources().getColor(R.color.red));
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
                                        if (jsonObject != null) {
                                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                                setResult(100);
                                                finish();
                                            }
                                            Toast.makeText(OrderListDetailActivity.this, AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(OrderListDetailActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        break;
                    case 1:
                        break;
                    case 2:
                        button.setVisibility(View.INVISIBLE);
                        break;
                }

                break;
            case "5":
                button.setVisibility(View.INVISIBLE);
                break;
        }

    }
}
