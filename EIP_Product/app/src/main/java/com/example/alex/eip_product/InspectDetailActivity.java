package com.example.alex.eip_product;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import db.OrderDatabase;

import static db.OrderDatabase.KEY_Comment;
import static db.OrderDatabase.KEY_ItemNo;
import static db.OrderDatabase.KEY_Notes;
import static db.OrderDatabase.KEY_PONumber;
import static db.OrderDatabase.KEY_POVersion;
import static db.OrderDatabase.KEY_VendorCode;
import static db.OrderDatabase.KEY_VendorName;


public class InspectDetailActivity extends AppCompatActivity {
    private TextView OrderComments, ItemNo, OrderItemComments;
    private TextView PONumber, POVersion, VendorCode, VendorName, Notes;
    private String key_ponumber;
    private OrderDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_detail);
        db = new OrderDatabase(this);
        key_ponumber = getIntent().getStringExtra(KEY_PONumber);
        initView();
        initData();
    }

    private void initView() {
        PONumber = findViewById(R.id.PONumber);
        POVersion = findViewById(R.id.POVersion);
        VendorCode = findViewById(R.id.VendorCode);
        VendorName = findViewById(R.id.VendorName);
        Notes = findViewById(R.id.Notes);
        OrderComments = findViewById(R.id.OrderComments);
        ItemNo = findViewById(R.id.ItemNo);
        OrderItemComments = findViewById(R.id.OrderItemComments);
    }

    private void initData() {
        ContentValues contentValues = db.getOrdersByPONumber(key_ponumber);
        if (contentValues != null) {
            PONumber.setText(contentValues.getAsString(KEY_PONumber));
            POVersion.setText(contentValues.getAsString(KEY_POVersion));
            VendorCode.setText(contentValues.getAsString(KEY_VendorCode));
            VendorName.setText(contentValues.getAsString(KEY_VendorName));
            Notes.setText(contentValues.getAsString(KEY_Notes));
        }
        ArrayList<ContentValues> list = db.getOrderCommentsByPONumber(key_ponumber);
        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();
        for (ContentValues cv : list) {
            builder1.append(cv.getAsString(KEY_Comment) + "\n");
        }
        OrderComments.setText(builder1.toString());

        list = db.getOrderItemCommentsByPONumber(key_ponumber);
        builder1.delete(0, builder1.length());
        for (ContentValues cv : list) {
            builder1.append(cv.getAsString(KEY_ItemNo) + "\n");
            builder2.append(cv.getAsString(KEY_Comment) + "\n");
        }
        ItemNo.setText(builder1.toString());
        OrderItemComments.setText(builder2.toString());
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
