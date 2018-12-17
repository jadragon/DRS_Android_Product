package tw.com.lccnet.app.designateddriving.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import tw.com.lccnet.app.designateddriving.API.Analyze.AnalyzeUtil;
import tw.com.lccnet.app.designateddriving.API.CustomerApi;
import tw.com.lccnet.app.designateddriving.R;
import tw.com.lccnet.app.designateddriving.RecyclerAdapter.GetCouponRecyclerAdapter;
import tw.com.lccnet.app.designateddriving.Thread.AsyncTaskUtils;
import tw.com.lccnet.app.designateddriving.Thread.IDataCallBack;

public class Fragment_getCoupon extends Fragment {
    private View v;
    private GetCouponRecyclerAdapter getCouponRecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_coupon, container, false);
        initSwipeLayout();
        initRecylcerView();
        return v;

    }

    private void initSwipeLayout() {
        swipeRefreshLayout = v.findViewById(R.id.include_swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        //設定刷新動作
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                    @Override
                    public JSONObject onTasking(Void... params) {
                        return CustomerApi.couponList();
                    }

                    @Override
                    public void onTaskAfter(JSONObject jsonObject) {
                        if (AnalyzeUtil.checkSuccess(jsonObject)) {
                            getCouponRecyclerAdapter.setFilter(jsonObject);
                        } else {
                            Toast.makeText(getContext(), AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }

        });
    }

    private void initRecylcerView() {
        RecyclerView recyclerView = v.findViewById(R.id.include_recyclerview);
        getCouponRecyclerAdapter = new GetCouponRecyclerAdapter(getContext());
        recyclerView.setAdapter(getCouponRecyclerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setFilter();

    }

    public void setFilter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                return CustomerApi.couponList();
            }

            @Override
            public void onTaskAfter(JSONObject jsonObject) {

                if (AnalyzeUtil.checkSuccess(jsonObject)) {
                    getCouponRecyclerAdapter.setFilter(jsonObject);
                } else {
                    Toast.makeText(getContext(), AnalyzeUtil.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
