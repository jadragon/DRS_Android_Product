package com.example.alex.designateddriving_driver;

import android.os.Bundle;

public class StandardFareActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_fare);
        initToolbar("收費標準", true);
    }
}
