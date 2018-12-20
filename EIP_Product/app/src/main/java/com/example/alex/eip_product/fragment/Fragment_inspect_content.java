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
import com.example.alex.eip_product.PreviewInsepectOrderActivity;
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
        ContentValues cv;
        for (int i = 0; i < list.size(); i++) {
            cv = list.get(i);
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_inspect_content, null);
            TextView textView = view.findViewWithTag("PONumber");
            textView.setText(cv.getAsString(KEY_PONumber));
            textView = view.findViewWithTag("POVersion");
            textView.setText(cv.getAsString(KEY_POVersion));
            if (cv.getAsBoolean(KEY_HasCompleted)) {
                textView = view.findViewWithTag("HasCompleted");
                textView.setBackgroundColor(getResources().getColor(R.color.gray_purple));
                textView.setText("是");
                textView = view.findViewWithTag("CanShipping");
                textView.setBackgroundColor(getResources().getColor(R.color.light_green));
                textView.setText("是");
                textView = view.findViewWithTag("overview");
                textView.setBackgroundColor(getResources().getColor(R.color.orange));
                textView.setText("(預覽)");
            } else {
                textView = view.findViewWithTag("HasCompleted");
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setText("否");
                textView = view.findViewWithTag("CanShipping");
                textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                textView.setText("未驗");

                textView = view.findViewWithTag("overview");
                if (cv.getAsBoolean(KEY_isOrderEdit)) {
                    textView.setText("(未完成)");
                    textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    textView.setText("(填寫驗表)");
                    textView.setBackgroundColor(getResources().getColor(R.color.blue));
                }
                textView.setOnClickListener(this);
                textView.setTag(i);
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
        int position = (int) v.getTag();
        Intent intent;
        if (list.get(position).getAsBoolean(KEY_HasCompleted)) {
            intent = new Intent(getContext(), PreviewInsepectOrderActivity.class);
        } else {
            intent = new Intent(getContext(), InsepectOrderActivity.class);
        }
        intent.putExtra(KEY_PONumber, list.get(position).getAsString(KEY_PONumber) + "");
        startActivity(intent);
    }
}
