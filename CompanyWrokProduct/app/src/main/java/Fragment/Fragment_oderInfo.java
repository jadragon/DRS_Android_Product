package Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import adapter.recyclerview.OrderInfoRecyclerViewAdapter;
import library.BasePageFragment;
import library.EndLessOnScrollListener;
import library.GetJsonData.OrderInfoJsonData;
import library.LoadingView;

public class Fragment_oderInfo extends BasePageFragment {
    View v;
    OrderInfoRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    JSONObject json;
    int index;
    library.Component.MySwipeRefreshLayout mSwipeLayout;
    int nextpage = 2;
    EndLessOnScrollListener endLessOnScrollListener;
    GlobalVariable gv;
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
        mSwipeLayout.setTouchSlop(400);
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
        recyclerView = v.findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderInfoRecyclerViewAdapter(getContext(), json, index);
        recyclerView.setAdapter(adapter);

        endLessOnScrollListener = new EndLessOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        json = new OrderInfoJsonData().getMemberOrder(gv.getToken(), index, nextpage);
                        nextpage++;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (!adapter.setFilterMore(json)) {
                                    nextpage--;
                                }
                            }
                        });
                    }
                }).start();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                json = new OrderInfoJsonData().getMemberOrder(gv.getToken(), index, 1);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setFilter(json);
                        mSwipeLayout.setRefreshing(false);
                        LoadingView.hide();
                    }
                });
            }
        }).start();
    }
}
