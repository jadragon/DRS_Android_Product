package tw.com.lccnet.app.designateddriving;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.RecyclerAdapter.NewsListAdapter;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;

public class NewsActivity extends AppCompatActivity {
    private NewsListAdapter newsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initToolbar("最新消息");
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


    private void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
    }


}
