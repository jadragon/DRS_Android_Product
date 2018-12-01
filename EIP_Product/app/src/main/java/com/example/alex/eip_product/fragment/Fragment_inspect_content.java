package com.example.alex.eip_product.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.alex.eip_product.GlobalVariable;
import com.example.alex.eip_product.InsepectOrderActivity;
import com.example.alex.eip_product.R;

import java.util.ArrayList;
import java.util.Map;

import db.OrderDatabase;

import static db.OrderDatabase.KEY_HasCompleted;
import static db.OrderDatabase.KEY_PONumber;
import static db.OrderDatabase.KEY_POVersion;
import static db.OrderDatabase.KEY_VendorName;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_inspect_content extends Fragment implements View.OnClickListener {
    private View v;
    private TextView title, company_name;
    private TableLayout tableLayout;
    private OrderDatabase db;
    private ArrayList<Map<String, String>> list;
    private GlobalVariable gv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inspect_content, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        db = new OrderDatabase(getContext());
        list = db.getOrdersByDate(gv.getCurrent_date());
        initTextView();
        return v;
    }

    private void initTextView() {
        //標題
        title = v.findViewById(R.id.inspect_content_txt_title);

        title.setText(list.get(0).get(KEY_VendorName) + "驗貨內容");
        //廠商名稱
        company_name = v.findViewById(R.id.company_name);
        company_name.setText(list.get(0).get(KEY_VendorName));
        tableLayout = v.findViewById(R.id.inspect_content_tableLayout);
        View view;
        for (Map<String, String> map : list) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_inspect_content, null);
            ((TextView) view.findViewWithTag("PONumber")).setText(map.get(KEY_PONumber));
            ((TextView) view.findViewWithTag("POVersion")).setText(map.get(KEY_POVersion));
            if (map.get(KEY_HasCompleted).equals("true")) {
                ((TextView) view.findViewWithTag("HasCompleted")).setText("是");
                ((TextView) view.findViewWithTag("CanShipping")).setText("是");
                ((TextView) view.findViewWithTag("overview")).setText("(預覽)");
            } else {
                ((TextView) view.findViewWithTag("HasCompleted")).setText("否");
                ((TextView) view.findViewWithTag("CanShipping")).setText("未驗");
                ((TextView) view.findViewWithTag("overview")).setText("(填寫驗表)");
                view.findViewWithTag("overview").setOnClickListener(this);
                view.findViewWithTag("overview").setTag(map.get(KEY_PONumber));
            }

            tableLayout.addView(view);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            title.setText(list.get(0).get(KEY_VendorName) + "驗貨內容");
        }
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), InsepectOrderActivity.class);
        intent.putExtra(KEY_PONumber, v.getTag() + "");
        startActivity(intent);
    }
}
