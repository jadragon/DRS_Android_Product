package tw.com.lccnet.app.designateddriving.RecyclerAdapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.lccnet.app.designateddriving.R;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.RecycleHolder> {

    private Context ctx;
    private List<Map<String, String>> list;

    public OrderListAdapter(Context ctx) {
        this.ctx = ctx;
        list = new ArrayList<>();
    }


    @NonNull
    @Override
    public OrderListAdapter.RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent, false);
        return new OrderListAdapter.RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, final int position) {
    }


    @Override
    public int getItemCount() {
        return 20;
    }

    class RecycleHolder extends RecyclerView.ViewHolder {

        public RecycleHolder(View view) {
            super(view);

        }


    }

    public void setFilter(JSONObject jsonObject) {

        try {
            list = new ArrayList<>();
            Map<String, String> map = null;
            JSONArray jsonArray = jsonObject.getJSONArray("Data");
            JSONObject object = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                map = new HashMap<>();
                object = jsonArray.getJSONObject(i);
                map.put("rtime", object.getString("rtime"));
                map.put("title", object.getString("title"));
                map.put("content", object.getString("content"));
                list.add(map);
            }
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
