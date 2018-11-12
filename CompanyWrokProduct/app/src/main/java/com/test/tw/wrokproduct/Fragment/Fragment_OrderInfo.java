package com.test.tw.wrokproduct.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import adapter.recyclerview.OrderInfoRecyclerViewAdapter;
import adapter.recyclerview.ProductOrderRecyclerViewAdapter;
import library.AnalyzeJSON.AnalyzeUtil;
import library.BasePageFragment;
import library.EndLessOnScrollListener;
import library.GetJsonData.OrderInfoJsonData;
import library.GetJsonData.StoreJsonData;
import library.LoadingView;

public class Fragment_OrderInfo extends BasePageFragment {
    private View v;
    private OrderInfoRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private JSONObject json;
    private int type, index;
    private library.Component.MySwipeRefreshLayout mSwipeLayout;
    private int nextpage = 2;
    private EndLessOnScrollListener endLessOnScrollListener;
    private GlobalVariable gv;
    private String token = "LpESIVhpKXZ5ZxJQyl19Ug==";
    private View no_data;

    public void setType(int type) {
        this.type = type;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        initRecyclerView();
        initSwipeLayout();
        return v;
    }

    private void initSwipeLayout() {
        mSwipeLayout = v.findViewById(R.id.include_swipe_refresh);
        mSwipeLayout.setColorSchemeColors(Color.RED);
        //設定靈敏度
        //mSwipeLayout.setTouchSlop(400);
        //設定刷新動作
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadingView.show(v);
                prepareFetchData(true);
                nextpage = 2;
                endLessOnScrollListener.reset();
            }

        });
    }

    private void initRecyclerView() {
        no_data = v.findViewById(R.id.include_no_data);
        recyclerView = v.findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (type == 0) {
            adapter = new OrderInfoRecyclerViewAdapter(getContext(), json, index);
        } else if (type == 1) {
            adapter = new ProductOrderRecyclerViewAdapter(getContext(), json, index);
            ((ProductOrderRecyclerViewAdapter) adapter).setToken(token);
        }

        recyclerView.setAdapter(adapter);

        endLessOnScrollListener = new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {

                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        if (type == 0) {
                            return new OrderInfoJsonData().getMemberOrder(gv.getToken(), index, nextpage);
                        } else if (type == 1) {
                            return new StoreJsonData().getStoreOrder(token, index, nextpage);
                        }
                        return null;
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if(AnalyzeUtil.checkSuccess(jsonObject)) {
                            if (adapter.setFilterMore(jsonObject)) {
                                nextpage++;
                            }
                        }
                    }
                });

            }
        };
        recyclerView.addOnScrollListener(endLessOnScrollListener);
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        adapter.setFilter(json);
    }


    @Override
    public void fetchData() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                if (type == 0) {
                    return new OrderInfoJsonData().getMemberOrder(gv.getToken(), index, 1);
                } else if (type == 1) {
                    return new StoreJsonData().getStoreOrder(token, index, 1);
                } else {
                    return null;
                }
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if(AnalyzeUtil.checkSuccess(jsonObject)) {
                    adapter.setFilter(jsonObject);
                    if (adapter.getItemCount() > 0) {
                        no_data.setVisibility(View.INVISIBLE);
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                }
                mSwipeLayout.setRefreshing(false);
                LoadingView.hide();
            }
        });
    }
}
