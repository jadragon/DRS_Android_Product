package com.test.tw.wrokproduct;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import library.SQLiteDatabaseHandler;

public class ListVIewActivity extends ListActivity {
    private  Toolbar toolbar;
    private TextView toolbar_title;
    private String[] array;
    private  SQLiteDatabaseHandler db;
    private  String land, city, area, zipcode;
    private  Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        intent = getIntent();
        land = intent.getStringExtra("land");
        city = intent.getStringExtra("city");
        initToolbar();
        db = new SQLiteDatabaseHandler(this);
        if (land != null) {
            toolbar_title.setText("縣市");
            if (land.equals("0")) {
                array = db.getAllCity().toArray(new String[db.getInSideCity().size()]);
            } else if (land.equals("1")) {
                array = db.getInSideCity().toArray(new String[db.getInSideCity().size()]);
            } else if (land.equals("2")) {
                array = db.getOutSideCity().toArray(new String[db.getOutSideCity().size()]);
            }
        } else if (city != null) {
            toolbar_title.setText("鄉鎮市");
            array = db.getAreaByCity(city).toArray(new String[db.getAreaByCity(city).size()]);
        } else {
            toolbar_title.setText("銀行");
            ArrayList<Map<String,String>> arrayList=db.getBankDetail();
            array = new String[arrayList.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = arrayList.get(i).get("bcode") + " - " + arrayList.get(i).get("bname");
            }
        }
        ListAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                array);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (land != null) {
            intent.putExtra("city", array[position]);
            setResult(0, intent);
        } else if (city != null) {
            area = array[position];
            zipcode = db.getZipcodeByCityAndArea(city, area).get("zipcode");
            intent.putExtra("area", area);
            intent.putExtra("zipcode", zipcode);
            setResult(1, intent);
        } else {
            array = array[position].split("-");
            intent.putExtra("bcode", array[0].trim());
            intent.putExtra("bname", array[1].trim());
            setResult(2, intent);
        }
        ListVIewActivity.this.finish();
    }

    private void initToolbar() {
        //Toolbar 建立
        toolbar = findViewById(R.id.include_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar_title = findViewById(R.id.include_toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
