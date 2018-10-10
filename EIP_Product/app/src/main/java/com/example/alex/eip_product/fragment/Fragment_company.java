package com.example.alex.eip_product.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.eip_product.MainActivity;
import com.example.alex.eip_product.R;
import com.example.alex.eip_product.adapter.CompanyListAdapter;

import java.util.Calendar;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_company extends Fragment {
    private View v;
    private RecyclerView recyclerview;
    private CompanyListAdapter companyListAdapter;
    private TextView title, prepage, nextpage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_company, container, false);
        initButton();
        initTextView();
        initRecylcerView();
        return v;
    }

    private void initButton() {
        prepage = v.findViewById(R.id.company_prepage);
        prepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                //  cal.setTime(new Date());
                cal.add(Calendar.DATE, -1);

            }
        });
        nextpage = v.findViewById(R.id.company_nextpage);
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initTextView() {
        title = v.findViewById(R.id.company_txt_title);
        title.setText(getArguments().getString("date") + "驗貨行程");

    }

    private void initRecylcerView() {
        recyclerview = v.findViewById(R.id.company_recyclerview);
        companyListAdapter = new CompanyListAdapter(getContext());
        recyclerview.setAdapter(companyListAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            title.setText(getArguments().getString("date") + "驗貨行程");
        }
    }
}
