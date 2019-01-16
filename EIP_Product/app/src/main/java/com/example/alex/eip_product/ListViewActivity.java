package com.example.alex.eip_product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.OrderDatabase;

import static db.OrderDatabase.KEY_Item;

public class ListViewActivity extends AppCompatActivity {
    private ListView listView;
    private String PONumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = findViewById(R.id.listView);
        PONumber = getIntent().getStringExtra("PONumber");
        OrderDatabase db = new OrderDatabase(this);
        final List<String> list = db.getFileNameByItem(PONumber);
        db.close();
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListViewActivity.this, PDFFromServerActivity.class);
                intent.putExtra("file_name", list.get(i));
                startActivity(intent);
            }
        });
    }
}
