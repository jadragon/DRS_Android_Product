package com.test.tw.wrokproduct;

import android.os.Bundle;

import library.Component.ToolbarActivity;

public class GoldFlowCompeleteActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldflow_compelete);
        initToolbar(false,"交易完成");
    }
}
