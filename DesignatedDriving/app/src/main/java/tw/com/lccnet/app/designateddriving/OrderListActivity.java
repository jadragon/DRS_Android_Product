package tw.com.lccnet.app.designateddriving;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.RecyclerAdapter.OrderListAdapter;
import tw.com.lccnet.app.designateddriving.Utils.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Utils.IDataCallBack;

public class OrderListActivity extends ToolbarActivity {
    private OrderListAdapter orderListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initToolbar("我的訂單", true);
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
                // setFilter();
            }

        });
    }

    private void initRecylcerView() {
        RecyclerView recyclerView = findViewById(R.id.include_recyclerview);
        orderListAdapter = new OrderListAdapter(this);
        recyclerView.setAdapter(orderListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setFilter();
    }

    private void setFilter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.news();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (AnalyzeUtil.checkSuccess(jsonObject)) {
                        orderListAdapter.setFilter(jsonObject);
                    }
                } else {
                    Toast.makeText(OrderListActivity.this, "連線異常", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
