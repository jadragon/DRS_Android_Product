package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import library.Component.ToolbarActivity;

public class GoldFlowCompeleteActivity extends ToolbarActivity {
    Button check_order_list;
    String msg, success;

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
        check_order_list = findViewById(R.id.check_order_list);
        check_order_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
