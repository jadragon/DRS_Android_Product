package com.example.alex.eip_product.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import static db.OrderDatabase.KEY_CheckMan;
import static db.OrderDatabase.KEY_CheckPass;
import static db.OrderDatabase.KEY_HasCompleted;
import static db.OrderDatabase.KEY_PONumber;
import static db.OrderDatabase.KEY_POVersion;
import static db.OrderDatabase.KEY_Reject;
import static db.OrderDatabase.KEY_Rework;
import static db.OrderDatabase.KEY_Special;
import static db.OrderDatabase.KEY_VendorName;
import static db.OrderDatabase.KEY_isOrderEdit;
import static db.OrderDatabase.KEY_isOrderUpdate;

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
        ContentValues cv, cv_checkpass;

        for (int i = 0; i < list.size(); i++) {
            cv = list.get(i);
            cv_checkpass = db.getOrderDetailsCountByPONumber(list.get(i).getAsString(KEY_PONumber));
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_inspect_content, null);
            TextView textView = view.findViewWithTag("PONumber");
            textView.setText(cv.getAsString(KEY_PONumber));
            textView = view.findViewWithTag("POVersion");
            textView.setText(cv.getAsString(KEY_POVersion));
            if (gv.getPermission() != null && gv.getPermission().equals("Edit")) {
                if (cv.getAsBoolean(KEY_HasCompleted)) {
                    textView = view.findViewWithTag("HasCompleted");
                    textView.setBackgroundColor(getResources().getColor(R.color.gray_purple));
                    textView.setText(getResources().getString(R.string.yes));

                    textView = view.findViewWithTag("CheckPass");
                    textView.setText(cv_checkpass.getAsString(KEY_CheckPass));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Special");
                    textView.setText(cv_checkpass.getAsString(KEY_Special));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Rework");
                    textView.setText(cv_checkpass.getAsString(KEY_Rework));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Reject");
                    textView.setText(cv_checkpass.getAsString(KEY_Reject));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("overview");
                    textView.setBackgroundColor(getResources().getColor(R.color.orange));
                    textView.setText(getResources().getString(R.string.preview));
                    textView.setOnClickListener(this);
                    textView.setTag(i);
                } else if (cv.getAsBoolean(KEY_isOrderUpdate)) {
                    textView = view.findViewWithTag("HasCompleted");
                    textView.setBackgroundColor(getResources().getColor(R.color.gray_purple));
                    textView.setText(getResources().getString(R.string.not_update));

                    textView = view.findViewWithTag("CheckPass");
                    textView.setText(cv_checkpass.getAsString(KEY_CheckPass));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Special");
                    textView.setText(cv_checkpass.getAsString(KEY_Special));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Rework");
                    textView.setText(cv_checkpass.getAsString(KEY_Rework));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Reject");
                    textView.setText(cv_checkpass.getAsString(KEY_Reject));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("overview");
                    textView.setBackgroundColor(getResources().getColor(R.color.orange));
                    textView.setText(getResources().getString(R.string.not_update));
                    textView.setOnClickListener(this);
                    textView.setTag(i);
                } else {

                    textView = view.findViewWithTag("HasCompleted");
                    textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    textView.setText(getResources().getString(R.string.no));

                    if (cv.getAsBoolean(KEY_isOrderEdit)) {
                        textView = view.findViewWithTag("CheckPass");
                        textView.setText(cv_checkpass.getAsString(KEY_CheckPass));
                        textView.setBackgroundColor(getResources().getColor(android.R.color.white));

                        textView = view.findViewWithTag("Special");
                        textView.setText(cv_checkpass.getAsString(KEY_Special));
                        textView.setBackgroundColor(getResources().getColor(android.R.color.white));

                        textView = view.findViewWithTag("Rework");
                        textView.setText(cv_checkpass.getAsString(KEY_Rework));
                        textView.setBackgroundColor(getResources().getColor(android.R.color.white));

                        textView = view.findViewWithTag("Reject");
                        textView.setText(cv_checkpass.getAsString(KEY_Reject));
                        textView.setBackgroundColor(getResources().getColor(android.R.color.white));

                        textView = view.findViewWithTag("overview");
                        textView.setText(getResources().getString(R.string.undone));
                        textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        textView = view.findViewWithTag("CheckPass");
                        textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                        textView.setText(getResources().getString(R.string.untest));

                        textView = view.findViewWithTag("Special");
                        textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                        textView.setText(getResources().getString(R.string.untest));

                        textView = view.findViewWithTag("Rework");
                        textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                        textView.setText(getResources().getString(R.string.untest));

                        textView = view.findViewWithTag("Reject");
                        textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                        textView.setText(getResources().getString(R.string.untest));

                        textView = view.findViewWithTag("overview");
                        textView.setText(getResources().getString(R.string.write));
                        textView.setBackgroundColor(getResources().getColor(R.color.blue));
                    }
                    textView.setOnClickListener(this);
                    textView.setTag(i);
                }
                tableLayout.addView(view);
            } else {
                if (cv.getAsBoolean(KEY_HasCompleted)) {
                    textView = view.findViewWithTag("HasCompleted");
                    textView.setBackgroundColor(getResources().getColor(R.color.gray_purple));
                    textView.setText(getResources().getString(R.string.yes));

                    textView = view.findViewWithTag("CheckPass");
                    textView.setText(cv_checkpass.getAsString(KEY_CheckPass));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Special");
                    textView.setText(cv_checkpass.getAsString(KEY_Special));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Rework");
                    textView.setText(cv_checkpass.getAsString(KEY_Rework));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));

                    textView = view.findViewWithTag("Reject");
                    textView.setText(cv_checkpass.getAsString(KEY_Reject));
                    textView.setBackgroundColor(getResources().getColor(R.color.light_green));
                } else {
                    textView = view.findViewWithTag("HasCompleted");
                    textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                    textView.setText(getResources().getString(R.string.no));

                    textView = view.findViewWithTag("CheckPass");
                    textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                    textView.setText(getResources().getString(R.string.untest));

                    textView = view.findViewWithTag("Special");
                    textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                    textView.setText(getResources().getString(R.string.untest));

                    textView = view.findViewWithTag("Rework");
                    textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                    textView.setText(getResources().getString(R.string.untest));

                    textView = view.findViewWithTag("Reject");
                    textView.setBackgroundColor(getResources().getColor(android.R.color.white));
                    textView.setText(getResources().getString(R.string.untest));
                }
                textView = view.findViewWithTag("overview");
                textView.setBackgroundColor(getResources().getColor(R.color.orange));
                textView.setText(getResources().getString(R.string.preview));
                textView.setOnClickListener(this);
                textView.setTag(i);
                tableLayout.addView(view);
            }
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
        if (gv.getPermission() != null && gv.getPermission().equals("Edit")) {
            if (list.get(position).getAsBoolean(KEY_HasCompleted) || list.get(position).getAsBoolean(KEY_isOrderUpdate)) {
                intent = new Intent(getContext(), PreviewInsepectOrderActivity.class);
            } else {
                intent = new Intent(getContext(), InsepectOrderActivity.class);
            }
        } else {
            intent = new Intent(getContext(), PreviewInsepectOrderActivity.class);
        }
        intent.putExtra(KEY_PONumber, list.get(position).getAsString(KEY_PONumber) + "");
        startActivity(intent);
    }
}
