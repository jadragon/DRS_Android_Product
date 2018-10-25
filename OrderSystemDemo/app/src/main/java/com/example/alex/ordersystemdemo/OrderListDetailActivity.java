package com.example.alex.ordersystemdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class OrderListDetailActivity extends ToolbarAcitvity {
    private GlobalVariable gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_detail);
        gv = (GlobalVariable) getApplicationContext();
        initToolbar("食富通超坑錢飯館",true,false);
        initButton();
    }

    private void initButton() {
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderListDetailActivity.this, gv.getToken(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
