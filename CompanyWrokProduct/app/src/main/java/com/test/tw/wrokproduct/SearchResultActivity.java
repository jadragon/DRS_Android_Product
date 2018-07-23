package com.test.tw.wrokproduct;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import Util.StringUtil;
import adapter.recyclerview.ShopRecyclerViewAdapter;
import library.Component.MySwipeRefreshLayout;
import library.EndLessOnScrollListener;
import library.GetJsonData.SearchJsonData;
import library.LoadingView;

public class SearchResultActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ShopRecyclerViewAdapter adapter;
    String keyword,unicode;
    JSONObject json;
    MySwipeRefreshLayout mSwipeLayout;
    int nextpage = 2;
    EndLessOnScrollListener endLessOnScrollListener;
GlobalVariable gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        gv=(GlobalVariable)getApplicationContext();
        keyword = getIntent().getStringExtra("keyword");
        initSwipeLayout();
        initToolbar();
        initRecyclerView();
    }

    private void initSwipeLayout() {
        mSwipeLayout = findViewById(R.id.include_swipe_refresh);
        mSwipeLayout.setColorSchemeColors(Color.RED);
        //設定靈敏度
        mSwipeLayout.setTouchSlop(400);
        //設定刷新動作
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //LoadingView.show(v);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        unicode=StringUtil.convertStringToUTF8(keyword);
                        json = new SearchJsonData().search_list(gv.getToken(),unicode, 1);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setFilter(json);
                                mSwipeLayout.setRefreshing(false);
                                LoadingView.hide();
                                nextpage = 2;
                                endLessOnScrollListener.reset();
                            }
                        });
                    }
                }).start();
            }

        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopRecyclerViewAdapter(this, null, ShopRecyclerViewAdapter.HIDE_BANNER);
        recyclerView.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SearchJsonData().search_click(unicode);
                unicode=StringUtil.convertStringToUTF8(keyword);
                json = new SearchJsonData().search_list(gv.getToken(),unicode, 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setFilter(json);
                    }
                });

            }
        }).start();
        endLessOnScrollListener = new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                //     LoadingView.show(v);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        unicode=StringUtil.convertStringToUTF8(keyword);
                        json = new SearchJsonData().search_list(gv.getToken(),unicode, nextpage);
                        nextpage++;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (!adapter.setFilterMore(json)) {
                                    nextpage--;
                                }
                                //    LoadingView.hide();
                            }
                        });
                    }
                }).start();
            }
        };
        recyclerView.addOnScrollListener(endLessOnScrollListener);
    }

    private void initToolbar() {
        //Toolbar 建立
        Toolbar toolbar = findViewById(R.id.include_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.include_toolbar_title)).setText(keyword);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
