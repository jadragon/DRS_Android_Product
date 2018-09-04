package com.example.alex.posdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.StockListAdapter;
import com.example.alex.posdemo.adapter.recylclerview.Util.EndLessOnScrollListener;

import org.json.JSONObject;

import java.util.Map;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.All_Store_BrandPojo;
import library.AnalyzeJSON.Analyze_StockInfo;
import library.JsonApi.StockApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_product extends Fragment {
    private View v;
    private UserInfo userInfo;
    private RecyclerView stock_recyclerview;
    private StockListAdapter stockListAdapter;
    private Spinner stock_spinner_store, stock_spinner_brand, stock_spinner_total_type;
    private TextView fragment_stock_total, fragment_stock_stotal, fragment_stock_ftotal;
    private EndLessOnScrollListener endLessOnScrollListener;
    int nextpage = 1;
    All_Store_BrandPojo all_store_brandPojo;
    EditText stock_edit_name, stock_edit_pcode;
    Button stock_btn_search;
    String name = "", pcode = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_product_layout, container, false);
        userInfo = (UserInfo) getContext().getApplicationContext();
        initEditTextAndButton();
        initSpinner();

        return v;
    }

    private void initEditTextAndButton() {
        stock_edit_name = v.findViewById(R.id.stock_edit_name);
        stock_edit_pcode = v.findViewById(R.id.stock_edit_pcode);
        stock_btn_search = v.findViewById(R.id.stock_btn_search);
        stock_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = stock_edit_name.getText().toString();
                pcode = stock_edit_pcode.getText().toString();
                resetData();
            }
        });
    }

    private void initTotalView(JSONObject jsonObject) {
        if (fragment_stock_total == null || fragment_stock_stotal == null || fragment_stock_ftotal == null) {
            fragment_stock_total = v.findViewById(R.id.fragment_stock_total);
            fragment_stock_stotal = v.findViewById(R.id.fragment_stock_stotal);
            fragment_stock_ftotal = v.findViewById(R.id.fragment_stock_ftotal);
        }
        Map<String, Integer> map = new Analyze_StockInfo().getStock_dataTotal(jsonObject);
        if (map != null) {
            fragment_stock_total.setText(map.get("total") + "");
            fragment_stock_stotal.setText(map.get("stotal") + "");
            fragment_stock_ftotal.setText(map.get("flaw_total") + "");
        }
    }

    private void initSpinner() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

            @Override
            public JSONObject onTasking(Void... params) {
                return new StockApi().all_store_brand();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                all_store_brandPojo = new Analyze_StockInfo().getAll_store_brand(jsonObject);
                stock_spinner_store = v.findViewById(R.id.stock_spinner_store);
                stock_spinner_store.setAdapter(new ArrayAdapter(getContext(),
                        R.layout.item_spinner, all_store_brandPojo.getStore()));
                stock_spinner_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     //   resetData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                stock_spinner_brand = v.findViewById(R.id.stock_spinner_brand);
                stock_spinner_brand.setAdapter(new ArrayAdapter(getContext(),
                        R.layout.item_spinner, all_store_brandPojo.getTitle()));
                stock_spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       // resetData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                stock_spinner_total_type = v.findViewById(R.id.stock_spinner_total_type);
                stock_spinner_total_type.setAdapter(new ArrayAdapter(getContext(),
                        R.layout.item_spinner, getResources().getStringArray(R.array.total_type)));
                stock_spinner_total_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                      //  resetData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                resetData();
            }
        });
    }

    private void resetData() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

            @Override
            public JSONObject onTasking(Void... params) {
                return new StockApi().stock_data(all_store_brandPojo.getS_no().get(stock_spinner_store.getSelectedItemPosition()),
                        all_store_brandPojo.getPb_no().get(stock_spinner_brand.getSelectedItemPosition()),
                        name, pcode, stock_spinner_total_type.getSelectedItemPosition(), 0);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                nextpage = 1;
                if (endLessOnScrollListener != null)
                    endLessOnScrollListener.reset();
                initTotalView(jsonObject);
                initRecyclerView(jsonObject);
            }
        });

    }

    private void initRecyclerView(JSONObject jsonObject) {
        if (stock_recyclerview != null && stockListAdapter != null) {
            stockListAdapter.setFilter(jsonObject);
            return;
        }
        stock_recyclerview = v.findViewById(R.id.stock_recyclerview);
        stockListAdapter = new StockListAdapter(getContext(), jsonObject);
        stock_recyclerview.setAdapter(stockListAdapter);
        endLessOnScrollListener = new EndLessOnScrollListener((LinearLayoutManager) stock_recyclerview.getLayoutManager()) {
            @Override
            public void onLoadMore(int currentPage) {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

                    @Override
                    public JSONObject onTasking(Void... params) {
                        return new StockApi().stock_data(all_store_brandPojo.getS_no().get(stock_spinner_store.getSelectedItemPosition()),
                                all_store_brandPojo.getPb_no().get(stock_spinner_brand.getSelectedItemPosition()),
                                name, pcode, stock_spinner_total_type.getSelectedItemPosition(), nextpage);
                    }
                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (stockListAdapter != null && stockListAdapter.setFilterMore(jsonObject)) {
                            nextpage++;
                        }
                    }
                });
            }
        };
        stock_recyclerview.addOnScrollListener(endLessOnScrollListener);
    }

}
