package com.test.tw.wrokproduct.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.MyAppreciateActivity;
import com.test.tw.wrokproduct.我的帳戶.訂單管理.訂單資訊.pojo.MyCommenttopPojo;

import org.json.JSONObject;

import Util.AsyncTaskUtils;
import Util.IDataCallBack;
import adapter.recyclerview.MyAppraiseRecyclerViewAdapter;
import library.AnalyzeJSON.AnalyzeComment;
import library.GetJsonData.OrderInfoJsonData;
import library.GetJsonData.StoreJsonData;

public class Fragment_Myappreciate extends Fragment {
    private  View v;
    private int type, rule;
    private  GlobalVariable gv;
    private MyAppraiseRecyclerViewAdapter adapter;
    private  RecyclerView recyclerView;
    String token = "LpESIVhpKXZ5ZxJQyl19Ug==";


    public void setType(int type) {
        this.type = type;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_refresh_recycler, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        initRecyclerView();
        return v;
    }

    private void initRecyclerView() {
        v.findViewById(R.id.include_swipe_refresh).setEnabled(false);
        recyclerView = v.findViewById(R.id.include_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAppraiseRecyclerViewAdapter(getContext(), null);
        adapter.setType(type);
        recyclerView.setAdapter(adapter);
        setFilter();
    }

    public void setFilter() {
        AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
            @Override
            public JSONObject onTasking(Void... params) {
                if (type == 0)
                    return new OrderInfoJsonData().getMyComment(gv.getToken(), rule);
                else
                    return new StoreJsonData().getStoreComment(token, rule);
            }

            @Override
            public void onTaskAfter(JSONObject json) {
                adapter.setFilter(json);
                if (rule == 1) {
                    initTopInfo(json);
                }
            }
        });

    }


    public void initTopInfo(JSONObject json) {
        MyCommenttopPojo myCommenttopPojo = new AnalyzeComment().getMyCommenttopPojo(json);
        ((TextView) getActivity().findViewById(R.id.myappraise_txt_tscore)).setText(String.format("%.01f", myCommenttopPojo.getTscore()));
        ((ImageView) getActivity().findViewById(R.id.myappraise_img_tscore)).setImageResource(getResources().obtainTypedArray(R.array.stars).getResourceId((int) myCommenttopPojo.getTscore(), 0));
        float good = ((float) myCommenttopPojo.getNum1() + myCommenttopPojo.getNum2() * 2 + myCommenttopPojo.getNum3() * 3 + myCommenttopPojo.getNum4() * 4 + myCommenttopPojo.getNum5() * 5) / (5 * myCommenttopPojo.getTnum());
        ((TextView) getActivity().findViewById(R.id.myappraise_txt_good)).setText("好評率:" + (int) (good * 100) + "%");
        ((TextView) getActivity().findViewById(R.id.myappraise_txt_tnum)).setText("累計評價:" + myCommenttopPojo.getTnum());
        ((MyAppreciateActivity) getActivity()).setmTabtitle(new String[]{"  全部(" + myCommenttopPojo.getTnum() + ")  ", "  五顆星(" + myCommenttopPojo.getNum5() + ")  ", "  四顆星(" + myCommenttopPojo.getNum4() + ")  ", "  三顆星(" + myCommenttopPojo.getNum3() + ")  ", "  二顆星(" + myCommenttopPojo.getNum2() + ")  ", "  一顆星(" + myCommenttopPojo.getNum1() + ")  "});
        ((MyAppreciateActivity) getActivity()).setFilter();
    }


}
