package com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MOrderPayPojo;

import org.json.JSONObject;

import java.util.ArrayList;

import Util.StringUtil;
import library.AnalyzeJSON.AnalyzeOrderInfo;
import library.GetJsonData.OrderInfoJsonData;

public class OrderPayDetailActivity extends AppCompatActivity {
    JSONObject json;
    String token, mono;
    MOrderPayPojo mOrderPayPojo;
    LinearLayout order_pay_detail_layout;
    TextView textView;
DisplayMetrics dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay_detail);
        dm=getResources().getDisplayMetrics();
        token = ((GlobalVariable) getApplicationContext()).getToken();
        mono = getIntent().getStringExtra("mono");
        initToolbar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new OrderInfoJsonData().getMOrderPay(token, mono);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOrderPayPojo = AnalyzeOrderInfo.getMOrderPay(json);
                        ((TextView) findViewById(R.id.include_toolbar_title)).setText(mOrderPayPojo.getPinfo() + "(合併結帳)");
                        initHeader();
                        initListLayout();
                    }
                });

            }
        }).start();

    }

    private void initHeader() {
        textView = findViewById(R.id.order_pay_detail_deadline);
        textView.setText(mOrderPayPojo.getDeadline());
        textView = findViewById(R.id.order_pay_detail_allpay);
        textView.setText("$" + StringUtil.getDeciamlString(mOrderPayPojo.getAllpay()));
    }

    private void initListLayout() {
        order_pay_detail_layout = findViewById(R.id.order_pay_detail_layout);
        for (String ordernum : mOrderPayPojo.getOrdernum()) {
            textView = new TextView(this);
            textView.setText(ordernum);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            textView.setTextColor(Color.BLACK);
            textView.setPadding((int)(10*dm.density),(int)(5*dm.density),(int)(10*dm.density),(int)(5*dm.density));
            order_pay_detail_layout.addView(textView);
        }
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
