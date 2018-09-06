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

import com.example.alex.posdemo.MainActivity;
import com.example.alex.posdemo.R;
import com.example.alex.posdemo.adapter.recylclerview.ProductfileListAdapter;
import com.example.alex.posdemo.adapter.recylclerview.Util.EndLessOnScrollListener;

import org.json.JSONObject;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.APIpojo.All_Store_BrandPojo;
import library.AnalyzeJSON.Analyze_StockInfo;
import library.JsonApi.ProductfileApi;
import library.JsonApi.StockApi;

/**
 * Created by user on 2017/5/30.
 */

public class Fragment_productfile extends Fragment {
    private View v;
    private RecyclerView productfile_recyclerview;
    private ProductfileListAdapter productfileListAdapter;
    private Spinner productfile_spinner_brand;
    private EndLessOnScrollListener endLessOnScrollListener;
    int nextpage = 1;
    All_Store_BrandPojo all_store_brandPojo;
    ProductfileApi productfileApi;
    EditText productfile_edit_name, productfile_edit_pcode, productfile_edit_title;
    Button productfile_btn_add, productfile_btn_search;
    String name = "", pcode = "", title = "";
    boolean isINIT_RECYCLERVIEW;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_productfile_layout, container, false);
        productfileApi = new ProductfileApi();
        initEditTextAndButton();
        initSpinner();

        return v;
    }

    private void initEditTextAndButton() {
        productfile_edit_name = v.findViewById(R.id.productfile_edit_name);
        productfile_edit_pcode = v.findViewById(R.id.productfile_edit_pcode);
        productfile_edit_title = v.findViewById(R.id.productfile_edit_title);
        productfile_btn_search = v.findViewById(R.id.productfile_btn_search);
        productfile_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = productfile_edit_name.getText().toString();
                pcode = productfile_edit_pcode.getText().toString();
                title = productfile_edit_title.getText().toString();
                resetData(false);
            }
        });

        productfile_btn_add = v.findViewById(R.id.productfile_btn_add);
        productfile_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_newproduct fragmentNewproduct = new Fragment_newproduct();
                Bundle bundle = new Bundle();
                //   bundle.putString("a_no", list.get(getAdapterPosition()).getA_no());
                //     fragmentNewproduct.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .hide(getActivity().getSupportFragmentManager().findFragmentByTag("productfile")).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.content, fragmentNewproduct, "newproduct").commit();
                ((MainActivity) getActivity()).setMAIN_FRAGMENT_TAG("productfile");
                ((MainActivity) getActivity()).setSUB_FRAGMENT_TAG("newproduct");
                ((MainActivity) getActivity()).showBack(true);
            }
        });
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
                productfile_spinner_brand = v.findViewById(R.id.productfile_spinner_brand);
                productfile_spinner_brand.setAdapter(new ArrayAdapter(getContext(),
                        R.layout.item_spinner, all_store_brandPojo.getTitle()));
                productfile_spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                return productfileApi.product_filing(all_store_brandPojo.getPb_no().get(productfile_spinner_brand.getSelectedItemPosition()),
                        name, pcode, title, 0);
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                nextpage = 1;
                if (endLessOnScrollListener != null)
                    endLessOnScrollListener.reset();
                initRecyclerView(jsonObject);
                if (init) {
                    isINIT_RECYCLERVIEW = true;
                }
            }
        });

    }

    private void initRecyclerView(JSONObject jsonObject) {
        if (productfile_recyclerview != null && productfileListAdapter != null) {
            productfileListAdapter.setFilter(jsonObject);
            return;
        }
        productfile_recyclerview = v.findViewById(R.id.productfile_recyclerview);
        productfileListAdapter = new ProductfileListAdapter(getContext(), jsonObject);
        productfile_recyclerview.setAdapter(productfileListAdapter);
        endLessOnScrollListener = new EndLessOnScrollListener((LinearLayoutManager) productfile_recyclerview.getLayoutManager()) {
            @Override
            public void onLoadMore(int currentPage) {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {

                    @Override
                    public JSONObject onTasking(Void... params) {
                        return productfileApi.product_filing(all_store_brandPojo.getPb_no().get(productfile_spinner_brand.getSelectedItemPosition()),
                                name, pcode, title, nextpage);
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (productfileListAdapter != null && productfileListAdapter.setFilterMore(jsonObject)) {
                            nextpage++;
                        }
                    }
                });
            }
        };
        productfile_recyclerview.addOnScrollListener(endLessOnScrollListener);
    }

}
