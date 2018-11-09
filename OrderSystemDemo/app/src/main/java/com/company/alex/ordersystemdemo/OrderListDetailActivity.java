package com.company.alex.ordersystemdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.alex.ordersystemdemo.API.Analyze.AnalyzeUtil;
import com.company.alex.ordersystemdemo.API.Analyze.Pojo.OrderDataPojo;
import com.company.alex.ordersystemdemo.API.List.OrderApi;
import com.company.alex.ordersystemdemo.library.AsyncTaskUtils;
import com.company.alex.ordersystemdemo.library.IDataCallBack;

import org.json.JSONObject;

public class OrderListDetailActivity extends ToolbarAcitvity {
    private GlobalVariable gv;
    private OrderDataPojo orderDataPojo;
    private String status;
    private ProgressDialog progressDialog;

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
        ((TextView) findViewById(R.id.f_content)).setText("餐點內容:\n" + orderDataPojo.getF_content().replace(",", "\n"));
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
                                progressDialog = ProgressDialog.show(OrderListDetailActivity.this,
                                        "讀取中", "請稍後...", true);
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new OrderApi().order_cancel(orderDataPojo.getO_id(), gv.getToken());
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        progressDialog.dismiss();
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
                                initTimeSelector("請確認餐點完成時間", 0);
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

                                initTimeSelector("請確認餐點送達時間", 1);

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
                                progressDialog = ProgressDialog.show(OrderListDetailActivity.this,
                                        "讀取中", "請稍後...", true);
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new OrderApi().order_gainFood(orderDataPojo.getO_id(), gv.getToken());
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        progressDialog.dismiss();
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
                                progressDialog = ProgressDialog.show(OrderListDetailActivity.this,
                                        "讀取中", "請稍後...", true);
                                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                                    @Override
                                    public JSONObject onTasking(Void... params) {
                                        return new OrderApi().order_complete(orderDataPojo.getO_id(), gv.getToken());
                                    }

                                    @Override
                                    public void onTaskAfter(JSONObject jsonObject) {
                                        progressDialog.dismiss();
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

    private String[] times = {"0分鐘", "5分鐘", "10分鐘", "15分鐘", "20分鐘", "25分鐘", "30分鐘", "35分鐘",
            "40分鐘", "45分鐘", "50分鐘", "55分鐘", "60分鐘", "65分鐘", "70分鐘", "75分鐘", "80分鐘",
            "85分鐘", "90分鐘", "95分鐘", "100分鐘", "105分鐘", "110分鐘", "115分鐘", "120分鐘", "125分鐘",
            "130分鐘", "135分鐘", "140分鐘", "145分鐘", "150分鐘"};

    private void initTimeSelector(String title, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderListDetailActivity.this);
        View view = LayoutInflater.from(OrderListDetailActivity.this).inflate(R.layout.dialog_time_selector, null);
        ListView listView = view.findViewWithTag("listview");
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_textview, times));
        ((TextView) view.findViewWithTag("title")).setText("注意\n" + title);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog.dismiss();
                progressDialog = ProgressDialog.show(OrderListDetailActivity.this,
                        "讀取中", "請稍後...", true);
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        if (type == 0) {
                            return new OrderApi().order_accept(orderDataPojo.getO_id(), gv.getToken(), times[position].replace("分鐘", ""));
                        } else if (type == 1) {
                            return new OrderApi().order_delivery_man(orderDataPojo.getO_id(), gv.getToken(), times[position].replace("分鐘", ""));
                        } else {
                            return null;
                        }
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        progressDialog.dismiss();
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

        view.findViewWithTag("cancel").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //dialog.getWindow().setBackgroundDrawableResource(R.color.invisible);
        dialog.show();

    }
}
