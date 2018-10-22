package com.example.alex.ordersystemdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OrderListDetailActivity extends ToolbarAcitvity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_detail);
        initToolbar("食富通超坑錢飯館",true,false);
        initButton();
    }

    private void initButton() {
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
