package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.recyclerview.PointRecyclerAdapter;

public class Fragment_Point extends Fragment {
    View v;
    PointRecyclerAdapter adapter;
    RecyclerView recyclerView;
    JSONObject json;
    int type, point_type;
    GlobalVariable gv;

    public void setPoint_type(int point_type) {
        this.point_type = point_type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.include_recylcerview, container, false);
        gv = (GlobalVariable) getContext().getApplicationContext();
        v.findViewById(R.id.swipe_refresh).setEnabled(false);
        initRecyclerView();
        return v;
    }

    private void initRecyclerView() {
        recyclerView = v.findViewById(R.id.fragment_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PointRecyclerAdapter(getContext(), null, type, point_type);
        recyclerView.setAdapter(adapter);
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        adapter.setFilter(json);
    }
/*
    @Override
    public void fetchData() {
        new JsonDataThread() {
            @Override
            public JSONObject getJsonData() {
                switch (point_type) {
                    case 1:
                        return new BillJsonData().getPolk(gv.getToken(), type);
                    case 2:
                        return new BillJsonData().getKuva(gv.getToken(), type);
                    case 3:
                        return new BillJsonData().getAcoin(gv.getToken(), type);
                    case 4:
                        return new BillJsonData().getEwallet(gv.getToken(), type);
                    default:
                        return new BillJsonData().getPolk(gv.getToken(), type);
                }


            }

            @Override
            public void runUiThread(JSONObject json) {
                try {
                    int point = json.getJSONObject("Data").getInt("point");
                    TextView textView = ((Activity) getContext()).findViewById(R.id.activity_pok_point);
                    textView.setText(StringUtil.getDeciamlString(point) + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.setFilter(json);
            }
        }.start();
    }
    */
}
