package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import library.AnalyzeJSON.AnalyzeOrderInfo;
import library.GetJsonData.OrderInfoJsonData;

public class OrderPayDetailActivity extends AppCompatActivity {
    JSONObject json;
    String token, mono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay_detail);
        token = ((GlobalVariable) getApplicationContext()).getToken();
        mono = getIntent().getStringExtra("mono");
        initToolbar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new OrderInfoJsonData().getMOrderPay(token, mono);
                Log.e("PayInfo", AnalyzeOrderInfo.getMOrderPay(json)+"");
            }
        }).start();
        ((TextView) findViewById(R.id.include_toolbar_title)).setText("(合併結帳)");
    }

    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
