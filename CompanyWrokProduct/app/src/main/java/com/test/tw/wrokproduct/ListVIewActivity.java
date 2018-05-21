package com.test.tw.wrokproduct;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import library.SQLiteDatabaseHandler;

public class ListVIewActivity extends ListActivity {
    String[] array;
    SQLiteDatabaseHandler db;
    String land, city,area,zipcode;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        intent = getIntent();
        land = intent.getStringExtra("land");
        city = intent.getStringExtra("city");
        db = new SQLiteDatabaseHandler(this);
        if (land != null) {
            if (land.equals("1"))
                array = db.getInSideCity().toArray(new String[db.getInSideCity().size()]);
            else
                array = db.getOutSideCity().toArray(new String[db.getOutSideCity().size()]);
        } else {
            array = db.getAreaByCity(city).toArray(new String[db.getAreaByCity(city).size()]);
        }
        ListAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                array);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(land!=null) {
            intent.putExtra("city", array[position]);
            setResult(0, intent);
        }else{
            area=array[position];
            zipcode=db.getZipcodeByCityAndArea(city,area).get("zipcode");
            intent.putExtra("area", area);
            intent.putExtra("zipcode", zipcode);
            setResult(1, intent);
        }
        ListVIewActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
