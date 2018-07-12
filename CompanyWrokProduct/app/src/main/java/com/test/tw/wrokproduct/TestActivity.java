package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import library.SQLiteDatabaseHandler;

public class TestActivity extends AppCompatActivity {
    TextView test_textview;
    SQLiteDatabaseHandler db;
    ArrayList<Map<String, String>> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }



}
