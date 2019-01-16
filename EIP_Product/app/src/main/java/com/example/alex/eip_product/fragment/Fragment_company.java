package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.eip_product.GlobalVariable;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.adapter.CompanyListAdapter;

import java.util.Calendar;
import java.util.Date;

import db.OrderDatabase;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_company extends Fragment implements View.OnClickListener {
    private View v;
    private CompanyListAdapter companyListAdapter;
    private TextView title, prepage, nextpage;
    private GlobalVariable gv;
    private Button tw, cn, vn;
    private String VendorCode = "";
    private OrderDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_company, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        db = new OrderDatabase(getContext());
        initView();
        initListener();
        return v;
    }

    private void initView() {
        title = v.findViewById(R.id.company_txt_title);
        prepage = v.findViewById(R.id.company_prepage);
        nextpage = v.findViewById(R.id.company_nextpage);
        tw = v.findViewById(R.id.company_tw);
        cn = v.findViewById(R.id.company_cn);
        vn = v.findViewById(R.id.company_vn);
        //recyclerview
        RecyclerView recyclerview = v.findViewById(R.id.company_recyclerview);
        companyListAdapter = new CompanyListAdapter(getContext());
        recyclerview.setAdapter(companyListAdapter);
    }

    private void initListener() {
        prepage.setOnClickListener(this);
        nextpage.setOnClickListener(this);
        tw.setOnClickListener(this);
        cn.setOnClickListener(this);
        vn.setOnClickListener(this);
    }

    private void initData() {
        // title.setText(getArguments().getString("date"));
        title.setText(gv.getCurrent_date());
        companyListAdapter.setFilter(db.getOrdersByDateAndLikeVendorCode(gv.getCurrent_date(), VendorCode));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.company_prepage:
                gv.setCurrent_date(caculateDate(-1));
                title.setText(gv.getCurrent_date());
                companyListAdapter.setFilter(db.getOrdersByDateAndLikeVendorCode(gv.getCurrent_date(), VendorCode));
                break;
            case R.id.company_nextpage:
                gv.setCurrent_date(caculateDate(1));
                title.setText(gv.getCurrent_date());
                companyListAdapter.setFilter(db.getOrdersByDateAndLikeVendorCode(gv.getCurrent_date(), VendorCode));
                break;
            case R.id.company_tw:
                VendorCode = "TW";
                companyListAdapter.setFilter(db.getOrdersByDateAndLikeVendorCode(gv.getCurrent_date(), VendorCode));
                break;
            case R.id.company_cn:
                VendorCode = "CN";
                companyListAdapter.setFilter(db.getOrdersByDateAndLikeVendorCode(gv.getCurrent_date(), VendorCode));
                break;
            case R.id.company_vn:
                VendorCode = "VN";
                companyListAdapter.setFilter(db.getOrdersByDateAndLikeVendorCode(gv.getCurrent_date(), VendorCode));
                break;
        }
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private Date caculateDate(int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) gv.getCurrent_date(0));
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }
}
