package com.example.alex.designateddriving_driver;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.alex.designateddriving_driver.RecyclerAdapter.MyEvaluationRecyclerAdapter;

public class MyEvaluationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyEvaluationRecyclerAdapter myEvaluationRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evaluation);
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecylcerView() {
        RecyclerView recyclerView = findViewById(R.id.include_recyclerview);
        myEvaluationRecyclerAdapter = new MyEvaluationRecyclerAdapter(this);
        recyclerView.setAdapter(myEvaluationRecyclerAdapter);
    }

}
