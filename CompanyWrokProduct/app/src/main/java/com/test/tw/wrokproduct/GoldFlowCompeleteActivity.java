package com.test.tw.wrokproduct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.OrderInfoActivity;

import library.Component.ToolbarActivity;

public class GoldFlowCompeleteActivity extends ToolbarActivity implements View.OnClickListener {
    private Button back_home, check_order_list;
    private  String msg, success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldflow_compelete);
        success = getIntent().getStringExtra("success");
        msg = getIntent().getStringExtra("msg");
        initToolbar(false, "交易完成");
        ((TextView) findViewById(R.id.goldflowcompelete_msg)).setText(msg);
        if (success.equals("true")) {
            ((ImageView) findViewById(R.id.goldflowcompelete_success)).setImageResource(R.drawable.warning_success);
        } else {
            ((ImageView) findViewById(R.id.goldflowcompelete_success)).setImageResource(R.drawable.warning_fail);
        }
        back_home = findViewById(R.id.back_home);
        back_home.setOnClickListener(this);
        check_order_list = findViewById(R.id.check_order_list);
        check_order_list.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_order_list:
                Intent  intent = new Intent(GoldFlowCompeleteActivity.this, OrderInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
            case R.id.back_home:
                intent = new Intent(GoldFlowCompeleteActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }
}
