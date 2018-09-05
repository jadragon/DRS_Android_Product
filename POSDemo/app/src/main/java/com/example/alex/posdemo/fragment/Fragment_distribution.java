package com.example.alex.posdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.posdemo.GlobalVariable.UserInfo;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.DistributionListAdapter;
import com.example.alex.posdemo.adapter.recylclerview.Util.EndLessOnScrollListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.All_Store_BrandPojo;
import library.AnalyzeJSON.APIpojo.Distribution2Pojo;
import library.AnalyzeJSON.Analyze_DistributionInfo;
import library.AnalyzeJSON.Analyze_StockInfo;
import library.JsonApi.DistributionApi;
import library.JsonApi.StockApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_distribution extends Fragment {
    View v;
    private UserInfo userInfo;
    RecyclerView distribution_recyclerview;
    DistributionListAdapter distributionListAdapter;
    Spinner distribution_spinner_brand;
    LinearLayout fragment_distribution_layout1, fragment_distribution_layout2;
    EndLessOnScrollListener endLessOnScrollListener;
    DistributionApi distributionApi;
    DisplayMetrics dm;
    TextView distribution_sum;
    int nextpage = 1;
    All_Store_BrandPojo all_store_brandPojo;
    EditText distribution_edit_name, distribution_edit_pcode;
    Button distribution_btn_search;
    String name = "", pcode = "";
    boolean isINIT_RECYCLERVIEW;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_distribution_layout, container, false);
        userInfo = (UserInfo) getContext().getApplicationContext();
        dm = getResources().getDisplayMetrics();
        distributionApi = new DistributionApi();
        initLayoutSize();
        initEditTextAndButton();
        initSpinner();
        return v;
    }

    private void initEditTextAndButton() {
        distribution_edit_name = v.findViewById(R.id.distribution_edit_name);
        distribution_edit_pcode = v.findViewById(R.id.distribution_edit_pcode);
        distribution_btn_search = v.findViewById(R.id.distribution_btn_search);
        distribution_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = distribution_edit_name.getText().toString();
                pcode = distribution_edit_pcode.getText().toString();
                resetData(false);
            }
        });
    }

    private void initLayoutSize() {
        int width = (int) (getActivity().findViewById(R.id.content).getWidth() - 90 * dm.density * 4);
        v.findViewById(R.id.fragment_distribution_titlelayout).getLayoutParams().width = width;
        v.findViewById(R.id.distribution_total_label).getLayoutParams().width = width;
    }

    private void initTitleView(JSONObject json) {
        if (fragment_distribution_layout1 == null || fragment_distribution_layout2 == null) {
            fragment_distribution_layout1 = v.findViewById(R.id.fragment_distribution_layout1);
            fragment_distribution_layout2 = v.findViewById(R.id.fragment_distribution_layout2);
            distribution_sum = v.findViewById(R.id.distribution_sum);
        }
        fragment_distribution_layout1.removeAllViews();
        fragment_distribution_layout2.removeAllViews();
        ArrayList<Distribution2Pojo> storeList = new Analyze_DistributionInfo().getAll_store_sum(json);
        if (storeList.size() > 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (80 * dm.density), LinearLayout.LayoutParams.MATCH_PARENT);
            TextView textView;
            for (int i = 0; i < storeList.size(); i++) {
                textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getContext().getResources().getColor(R.color.black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                textView.setLayoutParams(params);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setText(storeList.get(i).getStore());
                fragment_distribution_layout1.addView(textView);
                textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getContext().getResources().getColor(R.color.black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                textView.setLayoutParams(params);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setText(storeList.get(i).getTotal() + "");
                fragment_distribution_layout2.addView(textView);
            }
            try {
                distribution_sum.setText(json.getJSONObject("Data").getString("all_sum"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                distribution_spinner_brand = v.findViewById(R.id.distribution_spinner_brand);
                distribution_spinner_brand.setAdapter(new ArrayAdapter(getContext(),
                        R.layout.item_spinner, all_store_brandPojo.getTitle()));
                distribution_spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (isINIT_RECYCLERVIEW) {
                            resetData(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                resetData(true);
            }
        });
    }

    private void resetData(final boolean init) {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return distributionApi.distribution(all_store_brandPojo.getPb_no().get(distribution_spinner_brand.getSelectedItemPosition()), name,
                        pcode, 0);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                nextpage = 1;
                if (endLessOnScrollListener != null)
                    endLessOnScrollListener.reset();
                initTitleView(jsonObject);
                initRecyclerView(jsonObject);
                if (init) {
                    isINIT_RECYCLERVIEW = true;
                }
            }
        });

    }

    private void initRecyclerView(JSONObject jsonObject) {
        if (distribution_recyclerview != null && distributionListAdapter != null) {
            distributionListAdapter.setFilter(jsonObject);
            return;
        }
        distribution_recyclerview = v.findViewById(R.id.distribution_recyclerview);
        distributionListAdapter = new DistributionListAdapter(getContext(), jsonObject);
        distribution_recyclerview.setAdapter(distributionListAdapter);
        endLessOnScrollListener = new EndLessOnScrollListener((LinearLayoutManager) distribution_recyclerview.getLayoutManager()) {
            @Override
            public void onLoadMore(int currentPage) {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return distributionApi.distribution(all_store_brandPojo.getPb_no().get(distribution_spinner_brand.getSelectedItemPosition()), name,
                                pcode, nextpage);
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (distributionListAdapter != null && distributionListAdapter.setFilterMore(jsonObject)) {
                            nextpage++;
                        }
                    }
                });
            }
        };
    }

}
