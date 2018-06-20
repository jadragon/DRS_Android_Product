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

import adapter.recyclerview.ShipAddressRecyclerAdapter;

public class Fragment_shipAddress extends Fragment {
    View v;
    ShipAddressRecyclerAdapter adapter;
    RecyclerView recyclerView;
    JSONObject json;
    String token;
    int type;

    public void setType(int type) {
        this.type = type;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shipaddress_layout, container, false);
        GlobalVariable gv = (GlobalVariable) getActivity().getApplicationContext();
        token = gv.getToken();
        initRecyclerView();
        return v;
    }

    private void initRecyclerView() {
        recyclerView = v.findViewById(R.id.fragment_shipaddress_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShipAddressRecyclerAdapter(getActivity(), json, type);
        recyclerView.setAdapter(adapter);
    }

    public void setFilter(JSONObject json) {
        this.json = json;
        adapter.setFilter(json);
    }

}
