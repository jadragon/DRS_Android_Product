package com.example.alex.designateddriving_driver;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.alex.designateddriving_driver.API.Analyze.AnalyzeUtil;
import com.example.alex.designateddriving_driver.API.CustomerApi;
import com.example.alex.designateddriving_driver.RecyclerAdapter.NewsListAdapter;
import com.example.alex.designateddriving_driver.Utils.AsyncTaskUtils;
import com.example.alex.designateddriving_driver.Utils.IDataCallBack;

import org.json.JSONObject;



public class NewsActivity extends ToolbarActivity {
    private NewsListAdapter newsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initToolbar("最新消息", true);
        initSwipeLayout();
        initRecylcerView();
    }

    private void initSwipeLayout() {
        swipeRefreshLayout = findViewById(R.id.include_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        //設定刷新動作
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CustomerApi.news();
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                newsListAdapter.setFilter(jsonObject);
                            }
                        } else {
                            Toast.makeText(NewsActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }

        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.news();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        newsListAdapter.setFilter(jsonObject);
                    }
                } else {
                    Toast.makeText(NewsActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecylcerView() {
        RecyclerView recyclerView = findViewById(R.id.include_recyclerview);
        newsListAdapter = new NewsListAdapter(this);
        recyclerView.setAdapter(newsListAdapter);
    }


}
