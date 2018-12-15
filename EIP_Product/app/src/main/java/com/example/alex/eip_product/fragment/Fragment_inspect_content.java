package com.example.alex.eip_product.fragment;

import android.content.ContentValues;
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

import db.OrderDatabase;

import static db.OrderDatabase.KEY_HasCompleted;
import static db.OrderDatabase.KEY_PONumber;
import static db.OrderDatabase.KEY_POVersion;
import static db.OrderDatabase.KEY_VendorName;
import static db.OrderDatabase.KEY_isOrderEdit;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_inspect_content extends Fragment implements View.OnClickListener {
    private View v;
    private TextView title, company_name;
    private TableLayout tableLayout;
    private OrderDatabase db;
    private ArrayList<ContentValues> list;
    private GlobalVariable gv;
    private String VendorName;

    public static Fragment_inspect_content newInstance(int index) {
        Fragment_inspect_content f = new Fragment_inspect_content();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_inspect_content, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        VendorName = getArguments().getString(KEY_VendorName);
        db = new OrderDatabase(getContext());
        initView();
        return v;
    }

    private void initView() {
        title = v.findViewById(R.id.inspect_content_txt_title);
        company_name = v.findViewById(R.id.company_name);
        tableLayout = v.findViewById(R.id.inspect_content_tableLayout);
    }

    private void initData() {
        list = db.getOrdersByDateAndVendorName(gv.getCurrent_date(), VendorName);
        //標題
        title.setText(VendorName);
        //廠商名稱
        company_name.setText(VendorName);
        tableLayout.removeAllViews();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_insepect_content_tablelayout_header, null);
        tableLayout.addView(view);
        for (ContentValues cv : list) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_inspect_content, null);
            ((TextView) view.findViewWithTag("PONumber")).setText(cv.getAsString(KEY_PONumber));
            ((TextView) view.findViewWithTag("POVersion")).setText(cv.getAsString(KEY_POVersion));
            if (cv.getAsBoolean(KEY_HasCompleted)) {
                ((TextView) view.findViewWithTag("HasCompleted")).setText("是");
                ((TextView) view.findViewWithTag("CanShipping")).setText("是");
                ((TextView) view.findViewWithTag("overview")).setText("(預覽)");
            } else {
                ((TextView) view.findViewWithTag("HasCompleted")).setText("否");
                ((TextView) view.findViewWithTag("CanShipping")).setText("未驗");
                if (cv.getAsBoolean(KEY_isOrderEdit)) {
                    ((TextView) view.findViewWithTag("overview")).setText("(未完成)");
                } else {
                    ((TextView) view.findViewWithTag("overview")).setText("(填寫驗表)");
                }
                view.findViewWithTag("overview").setOnClickListener(this);
                view.findViewWithTag("overview").setTag(cv.getAsString(KEY_PONumber));
            }
            tableLayout.addView(view);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
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
