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

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_company extends Fragment implements View.OnClickListener {
    private View v;
    private RecyclerView recyclerview;
    private CompanyListAdapter companyListAdapter;
    private TextView title, prepage, nextpage;
    private GlobalVariable gv;
    private Button tw, cn, vn;
    private String VendorCode = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_company, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        initButton();
        initTextView();
        initRecylcerView();
        return v;
    }

    private void initButton() {
        prepage = v.findViewById(R.id.company_prepage);
        prepage.setOnClickListener(this);
        nextpage = v.findViewById(R.id.company_nextpage);
        nextpage.setOnClickListener(this);
        tw = v.findViewById(R.id.company_tw);
        tw.setOnClickListener(this);
        cn = v.findViewById(R.id.company_cn);
        cn.setOnClickListener(this);
        vn = v.findViewById(R.id.company_vn);
        vn.setOnClickListener(this);
    }

    private void initTextView() {
        title = v.findViewById(R.id.company_txt_title);
        // title.setText(getArguments().getString("date"));
        title.setText(gv.getCurrent_date());

    }

    private void initRecylcerView() {
        recyclerview = v.findViewById(R.id.company_recyclerview);
        companyListAdapter = new CompanyListAdapter(getContext(), VendorCode);
        recyclerview.setAdapter(companyListAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            title.setText(gv.getCurrent_date());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.company_prepage:
                Calendar cal = Calendar.getInstance();
                cal.setTime((Date) gv.getCurrent_date(0));
                cal.add(Calendar.DATE, -1);
                gv.setCurrent_date(cal.getTime());
                title.setText(gv.getCurrent_date());
                companyListAdapter.setFilter(VendorCode);
                break;
            case R.id.company_nextpage:
                cal = Calendar.getInstance();
                cal.setTime((Date) gv.getCurrent_date(0));
                cal.add(Calendar.DATE, +1);
                gv.setCurrent_date(cal.getTime());
                title.setText(gv.getCurrent_date());
                companyListAdapter.setFilter(VendorCode);
                break;
            case R.id.company_tw:
                VendorCode = "TW";
                companyListAdapter.setFilter(VendorCode);
                break;
            case R.id.company_cn:
                VendorCode = "CN";
                companyListAdapter.setFilter(VendorCode);
                break;
            case R.id.company_vn:
                VendorCode = "VN";
                companyListAdapter.setFilter(VendorCode);
                break;
        }
    }
}
