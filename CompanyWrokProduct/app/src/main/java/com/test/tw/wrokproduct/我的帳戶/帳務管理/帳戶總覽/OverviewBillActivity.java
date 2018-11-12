package com.test.tw.wrokproduct.我的帳戶.帳務管理.帳戶總覽;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.帳務管理.帳戶總覽.pojo.GetBillingPojo;
import com.test.tw.wrokproduct.我的帳戶.帳務管理.波克點值and庫瓦點值and雙閃幣and電子錢包.PointActivity;
import com.test.tw.wrokproduct.我的帳戶.帳務管理.現金折價券.CouponActivity;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import Util.StringUtil;
import library.AnalyzeJSON.AnalyzeBill;
import library.Component.ToolbarActivity;
import library.GetJsonData.BillJsonData;

public class OverviewBillActivity extends ToolbarActivity implements View.OnClickListener {
    private GlobalVariable gv;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_bill);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar(true, "帳戶總覽");

        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return new BillJsonData().getBilling(gv.getToken());
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                setText(jsonObject);
            }
        });
    }

    private void setText(JSONObject json) {
        GetBillingPojo getBillingPojo = new AnalyzeBill().getBillingPojo(json);
        ((TextView) findViewById(R.id.overview_bill_buname)).setText("用戶名稱:" + getBillingPojo.getBuname());
        ((TextView) findViewById(R.id.overview_bill_bname)).setText("銀行名稱:" + getBillingPojo.getBname());
        ((TextView) findViewById(R.id.overview_bill_bcard)).setText("銀行帳號:" + getBillingPojo.getBcard());
        ((TextView) findViewById(R.id.overview_bill_xpoint)).setText("" + StringUtil.getDeciamlString(getBillingPojo.getXpoint()));
        ((TextView) findViewById(R.id.overview_bill_ypoint)).setText("" + StringUtil.getDeciamlString(getBillingPojo.getYpoint()));
        ((TextView) findViewById(R.id.overview_bill_acoin)).setText("" + StringUtil.getDeciamlString(getBillingPojo.getAcoin()));
        ((TextView) findViewById(R.id.overview_bill_ewallet)).setText("" + StringUtil.getDeciamlString(getBillingPojo.getEwallet()));
        ((TextView) findViewById(R.id.overview_bill_coupon)).setText("" + getBillingPojo.getCoupon());
        findViewById(R.id.overview_bill_group1).setOnClickListener(this);
        findViewById(R.id.overview_bill_group2).setOnClickListener(this);
        findViewById(R.id.overview_bill_group3).setOnClickListener(this);
        findViewById(R.id.overview_bill_group4).setOnClickListener(this);
        findViewById(R.id.overview_bill_group5).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.overview_bill_group1:
                intent = new Intent(OverviewBillActivity.this, PointActivity.class);
                intent.putExtra("point_type", 1);
                startActivity(intent);
                break;
            case R.id.overview_bill_group2:
                intent = new Intent(OverviewBillActivity.this, PointActivity.class);
                intent.putExtra("point_type", 2);
                startActivity(intent);
                break;
            case R.id.overview_bill_group3:
                intent = new Intent(OverviewBillActivity.this, PointActivity.class);
                intent.putExtra("point_type", 3);
                startActivity(intent);
                break;
            case R.id.overview_bill_group4:
                intent = new Intent(OverviewBillActivity.this, PointActivity.class);
                intent.putExtra("point_type", 4);
                startActivity(intent);
                break;
            case R.id.overview_bill_group5:
                intent = new Intent(OverviewBillActivity.this, CouponActivity.class);
                startActivity(intent);
                break;
        }
    }
}
