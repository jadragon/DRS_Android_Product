package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import library.Component.ToolbarActivity;

public class GoldFlowCompeleteActivity extends ToolbarActivity {
    Button check_order_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldflow_compelete);
        initToolbar(false, "交易完成");
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
