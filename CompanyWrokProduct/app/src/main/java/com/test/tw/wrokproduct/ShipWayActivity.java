package com.test.tw.wrokproduct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.listview.OneExpandAdapter;

public class ShipWayActivity extends AppCompatActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipway);
        token = "zI6OIYlbhfPKyhbchdOiGg==";
        requestData();
    }
    private void requestData() {
        ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();
        for(int i = 1; i <= 10; i++){
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("phoneType", "HTC-M" + i + "");
            item.put("discount", "9");
            item.put("price", (2000 + i) + "");
            item.put("time", "2016020" + i);
            item.put("num", (300 - i) + "");
            datas.add(item);
        }

        ListView lvProduct = (ListView) findViewById(R.id.lv_products);
        OneExpandAdapter adapter = new OneExpandAdapter(this, datas);
        lvProduct.setAdapter(adapter);
    }
}
